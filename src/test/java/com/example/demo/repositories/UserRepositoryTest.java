package com.example.demo.repositories;

import com.example.demo.controllers.utils.TestDataBuilder;
import com.example.demo.entities.User;
import com.example.demo.enums.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    User user = TestDataBuilder.buildUserNoId();

    @AfterEach
    void tearDown() throws Exception {
        userRepository.deleteAll();
    }

    @Test
    void findByEmail_givenUser_shouldReturnOptionalUser(){
        userRepository.save(user);
        Optional<User> result = userRepository.findByEmail(user.getEmail());
        assertEquals(Optional.of(user),result);
    }

    @Test
    void findByEmail_givenNoUser_shouldReturnOptionalEmpty(){
        Optional<User> result = userRepository.findByEmail("random");
        assertEquals(Optional.empty(),result);
    }

}