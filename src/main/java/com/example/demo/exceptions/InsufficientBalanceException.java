package com.example.demo.exceptions;

import java.math.BigDecimal;

public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(BigDecimal balance) {
        super("Insufficient balance to perform this transaction: " + balance);
    }
}
