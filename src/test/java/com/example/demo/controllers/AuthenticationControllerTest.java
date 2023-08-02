package com.example.demo.controllers;

import com.example.demo.ApplicationConfigTest;
import com.example.demo.controllers.utils.TestDataBuilder;
import com.example.demo.dtos.LoginDTO;
import com.example.demo.dtos.RegisterDTO;
import com.example.demo.entities.User;
import com.example.demo.enums.Role;
import com.example.demo.exceptions.UniqueConstraintViolationError;
import com.example.demo.services.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthenticationControllerTest extends ApplicationConfigTest {
    private static final String PATH = "/auth";

    @MockBean
    private AuthenticationService authenticationService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    RegisterDTO registerDto = TestDataBuilder.buildRegisterDTO();
    LoginDTO loginDto = TestDataBuilder.buildLoginDTO();

    @Test
    void register_givenValidUser_shouldReturnToken() throws Exception {
        String token = "token";
        when(authenticationService.register(any(RegisterDTO.class))).thenReturn(token);
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .post(PATH + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(registerDto));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(content().string(token));

        verify(authenticationService, times(1)).register(any(RegisterDTO.class));
    }

    @Test
    void register_givenInvalidBody_shouldHandleMethodArgumentNotValidException() throws Exception {
        RegisterDTO registerDTO = new RegisterDTO();

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .post(PATH + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(registerDTO));

        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertTrue(result.getResolvedException()
                                instanceof MethodArgumentNotValidException));

        verify(authenticationService, never()).register(any(RegisterDTO.class));
    }

    @Test
    void register_givenUserAlreadyExists_shouldHandleUniqueConstraintViolationError()
            throws Exception {
        when(authenticationService.register(any(RegisterDTO.class)))
                .thenThrow(UniqueConstraintViolationError.class);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .post(PATH + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(registerDto));

        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertTrue(result.getResolvedException()
                                instanceof UniqueConstraintViolationError));

        verify(authenticationService, times(1)).register(any(RegisterDTO.class));
    }

    @Test
    void login_givenUser_shouldReturnToken() throws Exception {
        when(authenticationService.login(any(LoginDTO.class))).thenReturn("token");

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .post(PATH + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(loginDto));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(content().string("token"));

        verify(authenticationService, times(1)).login(any(LoginDTO.class));
    }

    @Test
    void login_givenInvalidBody_shouldHandleMethodArgumentNotValidException() throws Exception {
        LoginDTO loginDTO = new LoginDTO();

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .post(PATH + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(loginDTO));

        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertTrue(result.getResolvedException()
                                instanceof MethodArgumentNotValidException));

        verify(authenticationService, never()).login(any(LoginDTO.class));
    }

}