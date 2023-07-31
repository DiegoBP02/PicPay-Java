package com.example.demo.repositories;

import com.example.demo.entities.User;
import com.example.demo.enums.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    User USER_RECORD = User.builder()
            .name("name")
            .email("email")
            .password("password")
            .role(Role.PAYEE)
            .CPF("cpf")
            .build();

    @AfterEach
    void tearDown() throws Exception {
        userRepository.deleteAll();
    }

    @Test
    void findByEmail_givenUser_shouldReturnOptionalUser(){
        userRepository.save(USER_RECORD);
        Optional<User> result = userRepository.findByEmail(USER_RECORD.getEmail());
        assertEquals(Optional.of(USER_RECORD),result);
    }

    @Test
    void findByEmail_givenNoUser_shouldReturnOptionalEmpty(){
        Optional<User> result = userRepository.findByEmail("random");
        assertEquals(Optional.empty(),result);
    }

}