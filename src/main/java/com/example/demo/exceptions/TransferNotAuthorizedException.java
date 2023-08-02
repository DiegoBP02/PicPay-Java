package com.example.demo.exceptions;

public class TransferNotAuthorizedException extends RuntimeException {
    public TransferNotAuthorizedException() {
        super("Transfer is not authorized.");
    }
}
