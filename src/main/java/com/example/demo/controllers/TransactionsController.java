package com.example.demo.controllers;

import com.example.demo.dtos.RegisterDTO;
import com.example.demo.entities.TransactionPayload;
import com.example.demo.entities.TransactionResponse;
import com.example.demo.services.TransactionsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping(value = "/transactions")
public class TransactionsController {

    @Autowired
    private TransactionsService transactionsService;

    @PostMapping(value = "/payment")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<TransactionResponse> processTransaction
            (@Valid @RequestBody TransactionPayload transactionPayload) {
        TransactionResponse result = transactionsService.processTransaction(transactionPayload);
        return ResponseEntity.ok().body(result);
    }
}
