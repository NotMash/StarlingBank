package com.example.roundchallengeibrahim;

import com.example.roundchallengeibrahim.dto.Account.AccountResponse;
import com.example.roundchallengeibrahim.dto.Account.UserIdentityResponse;
import com.example.roundchallengeibrahim.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class AccountServiceTest {

    @Autowired
    private AccountService accountService;

   @Test
    void testFetchAllAccountsWithBalances() {
        List<AccountResponse.Account> accounts = accountService.fetchAllAccountsWithBalances();

        accounts.forEach(account -> {
            AccountResponse.Balance balance = account.getBalance();

            double availableBalance = balance.getEffectiveBalance().getMinorUnits() / 100.0;
            double clearedBalance = balance.getClearedBalance().getMinorUnits() / 100.0;
            double pendingBalance = balance.getPendingTransactions().getMinorUnits() / 100.0;

            System.out.println("Available Balance: £" + String.format("%.2f", availableBalance));
            System.out.println("Cleared Balance: £" + String.format("%.2f", clearedBalance));
            System.out.println("Pending: £" + String.format("%.2f", pendingBalance));
        });
    }

    @Test
    void testFetchUserIdentity() {
        // Call the method under test
        UserIdentityResponse result = accountService.fetchUserIdentity();

        // Assertions
        assertNotNull(result, "The result should not be null.");
        assertNotNull(result.getTitle(), "The title should not be null.");
        assertNotNull(result.getFirstName(), "The first name should not be null.");
        assertNotNull(result.getLastName(), "The last name should not be null.");
        assertNotNull(result.getDateOfBirth(), "The date of birth should not be null.");
        assertNotNull(result.getEmail(), "The email should not be null.");
        assertNotNull(result.getPhone(), "The phone number should not be null.");

        System.out.println("Title: " + result.getTitle());
        System.out.println("First Name: " + result.getFirstName());
        System.out.println("Last Name: " + result.getLastName());
        System.out.println("Date of Birth: " + result.getDateOfBirth());
        System.out.println("Email: " + result.getEmail());
        System.out.println("Phone: " + result.getPhone());
    }
}