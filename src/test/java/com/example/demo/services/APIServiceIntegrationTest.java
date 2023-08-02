package com.example.demo.services;

import com.example.demo.ApplicationConfigTest;
import com.example.demo.entities.APIResponse;
import com.example.demo.exceptions.ApiErrorException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class APIServiceIntegrationTest extends ApplicationConfigTest {

    @Autowired
    private APIService apiService;

    @Autowired
    private RestTemplate restTemplate;

    @Test
    void isTransferAuthorized_whenAPIResponseIsNotNullAndMessageIsAutorizado_shouldReturnTrue() {
        boolean result = apiService.isTransferAuthorized();
        assertTrue(result);
    }

    @Test
    void isEmailSentSuccessfully_whenAPIResponseIsNotNullAndMessageIsSuccess_shouldReturnTrue() {
        boolean result = apiService.isEmailSentSuccessfully();
        assertTrue(result);
    }

}
