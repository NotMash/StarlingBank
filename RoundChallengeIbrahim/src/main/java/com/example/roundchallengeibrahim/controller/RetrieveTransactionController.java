package com.example.roundchallengeibrahim.controller;

import com.example.roundchallengeibrahim.dto.Transactions.Transaction;
import com.example.roundchallengeibrahim.service.TransactionFetchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class RetrieveTransactionController {

    private final TransactionFetchService transactionFetchService;

    public RetrieveTransactionController(TransactionFetchService transactionFetchService) {
        this.transactionFetchService = transactionFetchService;
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getTransactions(

    ) {
        try {
            // Fetch transactions
            List<Transaction> transactions = transactionFetchService.fetchTransactions();

            // Return the response
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            // Log the error and return a 500 error
            System.err.println("Error fetching transactions: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
