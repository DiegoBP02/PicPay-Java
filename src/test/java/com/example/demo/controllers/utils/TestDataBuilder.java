package com.example.demo.controllers.utils;

import com.example.demo.dtos.LoginDTO;
import com.example.demo.dtos.RegisterDTO;
import com.example.demo.entities.TransactionPayload;
import com.example.demo.entities.TransactionResponse;
import com.example.demo.entities.User;
import com.example.demo.enums.Role;

import java.math.BigDecimal;
import java.util.UUID;

public class TestDataBuilder {
    public static User buildUser() {
        return User.builder()
                .id(UUID.randomUUID())
                .name("name")
                .email("email")
                .password("password")
                .role(Role.USER)
                .CPF("cpf")
                .balance(BigDecimal.valueOf(100))
                .build();
    }

    public static User buildUserNoId() {
        return User.builder()
                .name("name")
                .email("email")
                .password("password")
                .role(Role.USER)
                .CPF("cpf")
                .balance(BigDecimal.valueOf(100))
                .build();
    }

    public static RegisterDTO buildRegisterDTO() {
        return RegisterDTO.builder()
                .name("name")
                .email("email@email.com")
                .password("password")
                .role(Role.USER)
                .CPF("01234567890")
                .balance(BigDecimal.valueOf(100))
                .build();
    }

    public static LoginDTO buildLoginDTO() {
        return LoginDTO.builder()
                .email("email")
                .password("password")
                .build();
    }

    public static TransactionPayload buildTransactionPayload() {
        return TransactionPayload.builder()
                .payeeId(UUID.randomUUID())
                .amount(BigDecimal.valueOf(10))
                .build();
    }

    public static TransactionPayload buildTransactionPayload(UUID userId) {
        return TransactionPayload.builder()
                .payeeId(userId)
                .amount(BigDecimal.valueOf(10))
                .build();
    }

    public static TransactionResponse buildTransactionResponse() {
        return TransactionResponse.builder()
                .userBalance(BigDecimal.valueOf(100))
                .payeeBalance(BigDecimal.valueOf(50))
                .amountTransferred(BigDecimal.valueOf(10))
                .build();
    }

}
