package com.example.demo.services;

import com.example.demo.dtos.LoginDTO;
import com.example.demo.dtos.RegisterDTO;
import com.example.demo.entities.User;
import com.example.demo.exceptions.UniqueConstraintViolationError;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Lazy
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email not found: " + email));
    }

    public String register(RegisterDTO register) {
        try {
            User user = User.builder()
                    .name(register.getName())
                    .email(register.getEmail())
                    .password(passwordEncoder.encode(register.getPassword()))
                    .CPF(register.getCPF())
                    .role(register.getRole())
                    .build();
            userRepository.save(user);
            return tokenService.generateToken(user);
        } catch (DataIntegrityViolationException e) {
            throw new UniqueConstraintViolationError("user", "email or cpf");
        }
    }

    public String login(LoginDTO login) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword());

        Authentication authentication = this.authenticationManager.authenticate
                (usernamePasswordAuthenticationToken);

        User user = (User) authentication.getPrincipal();

        return tokenService.generateToken(user);
    }

}
