package com.example.demo.exceptions;

public class SelfTransferException extends RuntimeException{
    public SelfTransferException(){
        super("You cannot send money to yourself");
    }
}
