package com.example.demo.services;

import com.example.demo.ApplicationConfigTest;
import com.example.demo.controllers.utils.TestDataBuilder;
import com.example.demo.dtos.LoginDTO;
import com.example.demo.dtos.RegisterDTO;
import com.example.demo.entities.User;
import com.example.demo.exceptions.UniqueConstraintViolationError;
import com.example.demo.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest extends ApplicationConfigTest {

    @Autowired
    private AuthenticationService authenticationService;
    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private TokenService tokenService;
    @MockBean
    private UserRepository userRepository;

    User user = TestDataBuilder.buildUser();
    RegisterDTO registerDTO = TestDataBuilder.buildRegisterDTO();
    LoginDTO loginDTO = TestDataBuilder.buildLoginDTO();

    @Test
    void loadUserByUsername_givenUser_shouldReturnUser() {
        String email = user.getEmail();
        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        UserDetails result = authenticationService.loadUserByUsername(email);

        assertEquals(user, result);

        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void loadUserByUsername_givenNoUser_shouldThrowUsernameNotFoundException() {
        String email = user.getEmail();

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () ->
                authenticationService.loadUserByUsername(email));

        assertEquals("Email not found: " + email, exception.getMessage());

        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void register_givenValidUser_shouldReturnToken() {
        String token = "token";
        when(tokenService.generateToken(any(User.class))).thenReturn(token);

        String result = authenticationService.register(registerDTO);

        assertEquals(token, result);

        verify(userRepository, times(1)).save(any(User.class));
        verify(tokenService, times(1)).generateToken(any(User.class));
    }

    @Test
    void register_givenUserAlreadyExists_shouldThrowUniqueConstraintViolationError() {
        when(userRepository.save(any(User.class)))
                .thenThrow(DataIntegrityViolationException.class);

        assertThrows(UniqueConstraintViolationError.class, () -> {
            authenticationService.register(registerDTO);
        });

        verify(userRepository, times(1)).save(any(User.class));
        verify(tokenService, never()).generateToken(any(User.class));
    }

    @Test
    void login_givenUser_shouldReturnToken() {
        String token = "token";
        Authentication authenticate = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authenticate);
        when(authenticate.getPrincipal()).thenReturn(user);
        when(tokenService.generateToken(any(User.class))).thenReturn(token);

        String result = authenticationService.login(loginDTO);

        assertEquals(token, result);

        verify(authenticationManager, times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(authenticate, times(1)).getPrincipal();
        verify(tokenService, times(1)).generateToken(any(User.class));
    }

}