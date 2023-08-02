package com.example.demo.controllers;

        import com.example.demo.ApplicationConfigTest;
        import com.example.demo.controllers.utils.TestDataBuilder;
        import com.example.demo.entities.TransactionPayload;
        import com.example.demo.entities.TransactionResponse;
        import com.example.demo.enums.Role;
        import com.example.demo.exceptions.ApiErrorException;
        import com.example.demo.exceptions.ResourceNotFoundException;
        import com.example.demo.exceptions.TransferNotAuthorizedException;
        import com.example.demo.services.TransactionsService;
        import com.fasterxml.jackson.databind.ObjectMapper;
        import org.junit.jupiter.api.Test;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.boot.test.mock.mockito.MockBean;
        import org.springframework.http.MediaType;
        import org.springframework.security.access.AccessDeniedException;
        import org.springframework.test.web.servlet.MockMvc;
        import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
        import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
        import org.springframework.security.test.context.support.WithMockUser;
        import org.springframework.web.bind.MethodArgumentNotValidException;

        import java.math.BigDecimal;
        import java.util.UUID;

        import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.ArgumentMatchers.any;
        import static org.mockito.Mockito.*;
        import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TransactionsControllerTest extends ApplicationConfigTest {
    private static final String PATH = "/transactions";

    @MockBean
    private TransactionsService transactionsService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    TransactionPayload transactionPayload = TestDataBuilder.buildTransactionPayload();

    TransactionResponse transactionResponse = TestDataBuilder.buildTransactionResponse();

    @Test
    @WithMockUser(authorities = "USER")
    void processTransaction_givenSuccessfulTransaction_shouldReturnTransactionResponse() throws Exception {
        when(transactionsService.processTransaction(any(TransactionPayload.class)))
                .thenReturn(transactionResponse);
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .post(PATH + "/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionPayload));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(transactionResponse)));

        verify(transactionsService, times(1))
                .processTransaction(any(TransactionPayload.class));
    }

    @Test
    @WithMockUser(authorities = "USER")
    void processTransaction_givenInvalidRequestBody_shouldHandleMethodArgumentNotValidException() throws Exception {
        TransactionPayload transactionPayload = new TransactionPayload();
        when(transactionsService.processTransaction(any(TransactionPayload.class)))
                .thenReturn(transactionResponse);
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .post(PATH + "/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionPayload));

        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertTrue(result.getResolvedException()
                                instanceof MethodArgumentNotValidException));

        verify(transactionsService, never())
                .processTransaction(any(TransactionPayload.class));
    }

    @Test
    void processTransaction_givenNoUser_shouldReturnStatus403Forbidden() throws Exception {
        when(transactionsService.processTransaction(any(TransactionPayload.class)))
                .thenReturn(transactionResponse);
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .post(PATH + "/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionPayload));

        mockMvc.perform(mockRequest)
                .andExpect(status().isForbidden())
                .andExpect(result ->
                        assertEquals("Access Denied",
                                result.getResponse().getErrorMessage()));

        verify(transactionsService, never())
                .processTransaction(any(TransactionPayload.class));
    }

    @Test
    @WithMockUser(authorities = "random")
    void processTransaction_givenInvalidUserAuthority_shouldReturn401Unauthorized() throws Exception {
        when(transactionsService.processTransaction(any(TransactionPayload.class)))
                .thenReturn(transactionResponse);
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .post(PATH + "/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionPayload));

        mockMvc.perform(mockRequest)
                .andExpect(status().isUnauthorized())
                .andExpect(result ->
                        assertTrue(result.getResolvedException()
                                instanceof AccessDeniedException));

        verify(transactionsService, never())
                .processTransaction(any(TransactionPayload.class));
    }

    @Test
    @WithMockUser(authorities = "USER")
    void processTransaction_givenUserNotFound_shouldHandleResourceNotFoundException() throws Exception {
        when(transactionsService.processTransaction(any(TransactionPayload.class)))
                .thenThrow(ResourceNotFoundException.class);
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .post(PATH + "/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionPayload));

        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound())
                .andExpect(result ->
                        assertTrue(result.getResolvedException()
                                instanceof ResourceNotFoundException));

        verify(transactionsService, times(1))
                .processTransaction(any(TransactionPayload.class));
    }

    @Test
    @WithMockUser(authorities = "USER")
    void processTransaction_givenTransferIsNotAuthorized_shouldHandleTransferNotAuthorizedException() throws Exception {
        when(transactionsService.processTransaction(any(TransactionPayload.class)))
                .thenThrow(TransferNotAuthorizedException.class);
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .post(PATH + "/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionPayload));

        mockMvc.perform(mockRequest)
                .andExpect(status().isForbidden())
                .andExpect(result ->
                        assertTrue(result.getResolvedException()
                                instanceof TransferNotAuthorizedException));

        verify(transactionsService, times(1))
                .processTransaction(any(TransactionPayload.class));
    }

    @Test
    @WithMockUser(authorities = "USER")
    void processTransaction_givenAPIResponseIsNull_shouldHandleApiErrorException() throws Exception {
        when(transactionsService.processTransaction(any(TransactionPayload.class)))
                .thenThrow(ApiErrorException.class);
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .post(PATH + "/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionPayload));

        mockMvc.perform(mockRequest)
                .andExpect(status().isInternalServerError())
                .andExpect(result ->
                        assertTrue(result.getResolvedException()
                                instanceof ApiErrorException));

        verify(transactionsService, times(1))
                .processTransaction(any(TransactionPayload.class));
    }
}