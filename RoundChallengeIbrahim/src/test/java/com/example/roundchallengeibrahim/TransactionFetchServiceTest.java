package com.example.roundchallengeibrahim;

import com.example.roundchallengeibrahim.dto.Transactions.Transaction;
import com.example.roundchallengeibrahim.service.TransactionFetchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TransactionFetchServiceTest {

    @Autowired
    private TransactionFetchService transactionFetchService;

    @Test
    void testFetchTransactions() {


        List<Transaction> transactions = transactionFetchService.fetchTransactions();

        assertNotNull(transactions, "Transactions list should not be null");
        assertFalse(transactions.isEmpty(), "Transactions list should not be empty");

        System.out.println("Transactions found: " + transactions.size());
        transactions.forEach(transaction -> {
            System.out.println("Transaction Details:");
            System.out.println("ID: " + transaction.getId());
            System.out.println("Amount: " + transaction.getAmount());
            System.out.println("Direction: " + transaction.getDirection());
            System.out.println("Created: " + transaction.getCreated());
            System.out.println("Narrative: " + transaction.getNarrative());
            System.out.println("---");
        });
    }
}
