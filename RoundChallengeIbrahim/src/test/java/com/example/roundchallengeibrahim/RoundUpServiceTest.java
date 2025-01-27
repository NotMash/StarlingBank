package com.example.roundchallengeibrahim;

import com.example.roundchallengeibrahim.service.RoundUpService;
import com.example.roundchallengeibrahim.dto.Feed.FeedItem;
import com.example.roundchallengeibrahim.dto.SavingsGoals.SavingsGoal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RoundUpServiceTest {

    @Autowired
    private RoundUpService roundUpService;

    @Test
    void testFetchAccountUid() {
        String accountUid = roundUpService.fetchAccountDetails().getAccountUid();

        System.out.println("Fetched Account UID: " + accountUid);
        assertNotNull(accountUid, "Account UID should not be null");
    }

    @Test
    void testFetchWeekTransactions() {
        String accountUid = roundUpService.fetchAccountDetails().getAccountUid();
        List<FeedItem> transactions = roundUpService.fetchWeekTransactions(accountUid);

        assertNotNull(transactions, "Transactions list should not be null");
        System.out.println("Fetched " + transactions.size() + " transactions");
    }

    @Test
    void testRoundUpCalculation() {
        String accountUid = roundUpService.fetchAccountDetails().getAccountUid();
        List<FeedItem> transactions = roundUpService.fetchWeekTransactions(accountUid);

        BigDecimal roundUpAmount = roundUpService.calculateRoundUp(transactions);

        assertNotNull(roundUpAmount, "Round-up amount should not be null");
        assertTrue(roundUpAmount.compareTo(BigDecimal.ZERO) >= 0, "Round-up amount should be greater than or equal to zero");
        System.out.println("Calculated round-up amount: £" + roundUpAmount);
    }

    @Test
    void testCreateSavingsGoal() {
        String accountUid = roundUpService.fetchAccountDetails().getAccountUid();
        String goalName = "Weekly Round-Up Savings " + System.currentTimeMillis();

        String savingsGoalUid = roundUpService.createSavingsGoal(accountUid, goalName, BigDecimal.valueOf(1000));

        assertNotNull(savingsGoalUid, "Savings goal UID should not be null");
        System.out.println("Created savings goal: " + savingsGoalUid);
    }

    @Test
    void testTransferToSavingsGoal() {
        String accountUid = roundUpService.fetchAccountDetails().getAccountUid();
        String goalName = "Weekly Round-Up Savings " + System.currentTimeMillis();
        String savingsGoalUid = roundUpService.createSavingsGoal(accountUid, goalName, BigDecimal.valueOf(1000));

        List<FeedItem> transactions = roundUpService.fetchWeekTransactions(accountUid);
        BigDecimal roundUpAmount = roundUpService.calculateRoundUp(transactions);

        if (roundUpAmount.compareTo(BigDecimal.ZERO) > 0) {
            assertDoesNotThrow(() ->
                roundUpService.transferToSavingsGoal(accountUid, savingsGoalUid, roundUpAmount)
            );
            System.out.println("Successfully transferred £" + roundUpAmount + " to savings goal");
        } else {
            System.out.println("No round-up amount to transfer (£0.00)");
        }
    }

    @Test
    void testFetchSavingsGoals() {
        String accountUid = roundUpService.fetchAccountDetails().getAccountUid();
        List<SavingsGoal> savingsGoals = roundUpService.fetchSavingsGoals(accountUid);

        assertNotNull(savingsGoals, "Savings goals list should not be null");
        assertTrue(savingsGoals.size() >= 0, "Savings goals size should be greater than or equal to zero");

        System.out.println("Fetched " + savingsGoals.size() + " savings goals.");
        savingsGoals.forEach(goal -> System.out.println("Savings Goal UID: " + goal.getSavingsGoalUid()));
    }

    @Test
    void testWithdrawFromSavingsGoal() {
        String accountUid = roundUpService.fetchAccountDetails().getAccountUid();
        String goalName = "Test Withdrawal Goal " + System.currentTimeMillis();
        String savingsGoalUid = roundUpService.createSavingsGoal(accountUid, goalName, BigDecimal.valueOf(1000));

        BigDecimal deposit = new BigDecimal("210.00");
        roundUpService.transferToSavingsGoal(accountUid, savingsGoalUid, deposit);
        System.out.println("Deposited £" + deposit);

        BigDecimal withdrawal = new BigDecimal("69.00");
        roundUpService.withdrawFromSavings(accountUid, savingsGoalUid, withdrawal);
        System.out.printf("Withdrew £%.2f.%n", withdrawal);
    }

    @Test
    void testInvalidAmountTransfer() {
        String accountUid = roundUpService.fetchAccountDetails().getAccountUid();
        String goalName = "Invalid Transfer Test";
        String savingsGoalUid = roundUpService.createSavingsGoal(accountUid, goalName, BigDecimal.valueOf(1000));

        BigDecimal invalidAmount = new BigDecimal("-5.00");

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
            roundUpService.transferToSavingsGoal(accountUid, savingsGoalUid, invalidAmount)
        );
        assertTrue(e.getMessage().contains("Amount must be greater than zero"));
    }



}
