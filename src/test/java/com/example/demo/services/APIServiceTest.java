package com.example.demo.services;

import com.example.demo.ApplicationConfigTest;
import com.example.demo.controllers.utils.TestDataBuilder;
import com.example.demo.entities.APIResponse;
import com.example.demo.entities.User;
import com.example.demo.exceptions.ApiErrorException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class APIServiceTest extends ApplicationConfigTest {

    @Autowired
    private APIService apiService;

    @MockBean
    private RestTemplate restTemplate;

    User user = TestDataBuilder.buildUser();

    @Test
    void isTransferAuthorized_whenAPIResponseIsNotNullAndMessageIsAutorizado_shouldReturnTrue() {
        APIResponse apiResponse = new APIResponse();
        apiResponse.setMessage("Autorizado");
        when(restTemplate.getForObject(anyString(), eq(APIResponse.class)))
                .thenReturn(apiResponse);
        boolean result = apiService.isTransferAuthorized();
        assertTrue(result);
    }

    @Test
    void isTransferAuthorized_whenAPIResponseIsNull_shouldThrowAPIErrorException() {
        when(restTemplate.getForObject(anyString(), eq(APIResponse.class)))
                .thenReturn(null);

        assertThrows(ApiErrorException.class, () -> apiService.isTransferAuthorized());
    }

    @Test
    void isTransferAuthorized_whenAPIResponseMessageIsNotAutorizado_shouldReturnFalse() {
        APIResponse apiResponse = new APIResponse();
        apiResponse.setMessage("random");

        when(restTemplate.getForObject(anyString(), eq(APIResponse.class)))
                .thenReturn(apiResponse);

        boolean result = apiService.isTransferAuthorized();
        assertFalse(result);
    }

    @Test
    void isEmailSentSuccessfully_whenAPIResponseIsNotNullAndMessageIsSuccess_shouldReturnTrue() {
        APIResponse apiResponse = new APIResponse();
        apiResponse.setMessage("Success");
        when(restTemplate.getForObject(anyString(), eq(APIResponse.class)))
                .thenReturn(apiResponse);
        boolean result = apiService.isEmailSentSuccessfully();
        assertTrue(result);
    }

    @Test
    void isEmailSentSuccessfully_whenAPIResponseIsNull_shouldThrowAPIErrorException() {
        when(restTemplate.getForObject(anyString(), eq(APIResponse.class)))
                .thenReturn(null);

        assertThrows(ApiErrorException.class, () -> apiService.isEmailSentSuccessfully());
    }

    @Test
    void isEmailSentSuccessfully_whenAPIResponseMessageIsNotSuccess_shouldReturnFalse() {
        APIResponse apiResponse = new APIResponse();
        apiResponse.setMessage("random");

        when(restTemplate.getForObject(anyString(), eq(APIResponse.class)))
                .thenReturn(apiResponse);

        boolean result = apiService.isEmailSentSuccessfully();
        assertFalse(result);
    }
}