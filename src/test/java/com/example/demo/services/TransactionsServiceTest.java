package com.example.demo.services;

import com.example.demo.ApplicationConfigTest;
import com.example.demo.controllers.utils.TestDataBuilder;
import com.example.demo.entities.TransactionPayload;
import com.example.demo.entities.TransactionResponse;
import com.example.demo.entities.User;
import com.example.demo.exceptions.InsufficientBalanceException;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.exceptions.TransferNotAuthorizedException;
import com.example.demo.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TransactionsServiceTest extends ApplicationConfigTest {

    @Autowired
    private TransactionsService transactionsService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private APIService apiService;

    User sourceUser = TestDataBuilder.buildUser();
    User targetUser = TestDataBuilder.buildUser();
    TransactionPayload transactionPayload = TestDataBuilder.buildTransactionPayload(targetUser.getId());

    private Authentication authentication;
    private SecurityContext securityContext;

    @BeforeEach
    void setup() {
        // simulates getCurrentUser method
        authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(sourceUser);
        securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void processTransaction_whenTransactionSuccess_shouldReturnTransactionResponse() {
        when(userRepository.findById(targetUser.getId()))
                .thenReturn(Optional.ofNullable(targetUser));
        when(apiService.isTransferAuthorized()).thenReturn(true);
        when(apiService.isEmailSentSuccessfully()).thenReturn(true);

        TransactionResponse result = transactionsService.processTransaction(transactionPayload);

        assertEquals(sourceUser.getBalance(), result.getUserBalance());
        assertEquals(targetUser.getBalance(), result.getPayeeBalance());
        assertEquals(transactionPayload.getAmount(), result.getAmountTransferred());
        assertNotNull(result.getEmailResponse());
    }

    @Test
    void processTransaction_whenPayeeNotFound_shouldThrowResourceNotFoundException() {
        when(userRepository.findById(targetUser.getId()))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                transactionsService.processTransaction(transactionPayload));
    }

    @Test
    void processTransaction_whenInsufficientBalance_shouldThrowInsufficientBalanceException() {
        sourceUser.setBalance(BigDecimal.ONE);

        when(userRepository.findById(targetUser.getId()))
                .thenReturn(Optional.ofNullable(targetUser));

        assertThrows(InsufficientBalanceException.class, () ->
                transactionsService.processTransaction(transactionPayload));
    }

    @Test
    void processTransaction_whenTransferNotAuthorized_shouldThrowTransferNotAuthorizedException() {
        when(userRepository.findById(targetUser.getId()))
                .thenReturn(Optional.ofNullable(targetUser));
        when(apiService.isTransferAuthorized()).thenReturn(false);

        assertThrows(TransferNotAuthorizedException.class, () ->
                transactionsService.processTransaction(transactionPayload));
    }

    @Test
    void processTransaction_whenEmailSendingFails_shouldStillReturnTransactionResponse() {
        final String emailResponseMessage = "There was an issue with the email service. " +
                "The email will be sent later.";

        when(userRepository.findById(targetUser.getId()))
                .thenReturn(Optional.ofNullable(targetUser));
        when(apiService.isTransferAuthorized()).thenReturn(true);
        when(apiService.isEmailSentSuccessfully()).thenReturn(false);

        TransactionResponse result = transactionsService.processTransaction(transactionPayload);

        assertEquals(sourceUser.getBalance(), result.getUserBalance());
        assertEquals(targetUser.getBalance(), result.getPayeeBalance());
        assertEquals(transactionPayload.getAmount(), result.getAmountTransferred());
        assertEquals(emailResponseMessage, result.getEmailResponse());
    }

}