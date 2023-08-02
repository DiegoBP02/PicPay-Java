package com.example.demo.services;

import com.example.demo.entities.APIResponse;
import com.example.demo.exceptions.ApiErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class APIService {

    @Autowired
    private RestTemplate restTemplate;

    public boolean isTransferAuthorized() {
        try {
            final String url = "https://run.mocky.io/v3/8fafdd68-a090-496f-8c9a-3442cf30dae6";
            APIResponse response = restTemplate.getForObject(url, APIResponse.class);
            return Objects.equals(response.getMessage(), "Autorizado");
        } catch (NullPointerException e) {
            throw new ApiErrorException("API response is null");
        }
    }

    public boolean isEmailSentSuccessfully() {
        final String url = "http://o4d9z.mocklab.io/notify";

        try {
            APIResponse response = restTemplate.getForObject(url, APIResponse.class);
            return Objects.equals(response.getMessage(), "Success");
        } catch (NullPointerException e) {
            throw new ApiErrorException("API response is null");
        } catch (Exception e) {
            return false;
        }
    }

}
