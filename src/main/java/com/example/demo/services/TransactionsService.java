package com.example.demo.services;

import com.example.demo.entities.TransactionPayload;
import com.example.demo.entities.TransactionResponse;
import com.example.demo.entities.User;
import com.example.demo.exceptions.InsufficientBalanceException;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.exceptions.SelfTransferException;
import com.example.demo.exceptions.TransferNotAuthorizedException;
import com.example.demo.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@EnableTransactionManagement
public class TransactionsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private APIService apiService;

    @Transactional
    public TransactionResponse processTransaction(TransactionPayload transactionPayload) {
        UUID payeeId = transactionPayload.getPayeeId();
        BigDecimal amount = transactionPayload.getAmount();

        User user = getCurrentUser();

        if (user.getId().equals(payeeId)){
            throw new SelfTransferException();
        }

        User payee = userRepository.findById(payeeId)
                .orElseThrow(() -> new ResourceNotFoundException(payeeId));
        transferMoney(user, payee, amount);

        if(!apiService.isTransferAuthorized()){
            throw new TransferNotAuthorizedException();
        }

        String emailResponseMessage = "Email sent successfully to the user.";
        if(!apiService.isEmailSentSuccessfully()){
            emailResponseMessage = "There was an issue with the email service. " +
                    "The email will be sent later.";
        }

        return TransactionResponse.builder()
                .amountTransferred(amount)
                .userBalance(user.getBalance())
                .payeeBalance(payee.getBalance())
                .emailResponse(emailResponseMessage)
                .build();
    }

    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private void transferMoney(User payer, User payee, BigDecimal amount) {
        if (payer.getBalance().compareTo(amount) < 0){
            throw new InsufficientBalanceException(payer.getBalance());
        }
        payer.setBalance(payer.getBalance().subtract(amount));
        payee.setBalance(payee.getBalance().add(amount));
        userRepository.save(payer);
        userRepository.save(payee);
    }

}
