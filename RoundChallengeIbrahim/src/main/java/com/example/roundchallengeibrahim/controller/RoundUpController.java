package com.example.roundchallengeibrahim.controller;

import com.example.roundchallengeibrahim.dto.Account.AccountDetails;
import com.example.roundchallengeibrahim.dto.Feed.FeedItem;
import com.example.roundchallengeibrahim.dto.SavingsGoals.SavingsGoal;
import com.example.roundchallengeibrahim.service.RoundUpService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000") // Allow your frontend origin
@RequestMapping("/round-up")
public class RoundUpController {

    private final RoundUpService roundUpService;

    public RoundUpController(RoundUpService roundUpService) {
        this.roundUpService = roundUpService;
    }

    /**
     * Get the total round-up amount for the last 7 days.
     */
    @GetMapping("/amount")
    public ResponseEntity<BigDecimal> getRoundUpAmount() {
        try {
            String accountUid = roundUpService.fetchAccountDetails().getAccountUid();
            List<FeedItem> feedItems = roundUpService.fetchWeekTransactions(accountUid);
            BigDecimal totalRoundUp = roundUpService.calculateRoundUp(feedItems);
            return ResponseEntity.ok(totalRoundUp);
        } catch (Exception e) {
            System.err.println("Error calculating round-up amount: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Perform a weekly round-up transfer to a savings goal.
     */
    @PostMapping("/transfer")
    public ResponseEntity<String> performRoundUpTransfer(@RequestParam(required = false) String goalName) {
        try {
            // Use the provided goal name, or default to "Round-Up Savings"
            roundUpService.processWeeklyRoundUp(goalName != null ? goalName : "Round-Up Savings");
            return ResponseEntity.ok("Round-up transfer completed successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid input: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error performing round-up transfer: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body("Failed to complete round-up transfer: " + e.getMessage());
        }
    }

    @PutMapping("/transfer")
    public ResponseEntity<String> transferToSavingsGoal(
            @RequestParam String savingsGoalUid,
            @RequestParam BigDecimal amount,
            @RequestBody AccountDetails accountDetails) {

        try {
            System.out.println("Account UID: " + accountDetails.getAccountUid());
            System.out.println("Transferring £" + amount + " to savings goal with ID: " + savingsGoalUid);

            // Perform the transfer using the service
            roundUpService.transferToSavingsGoal(accountDetails.getAccountUid(), savingsGoalUid, amount);

            return ResponseEntity.ok(String.format("Successfully transferred £%.2f to savings goal with ID: %s", amount, savingsGoalUid));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            System.err.println("Error performing round-up transfer: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Failed to complete transfer: " + e.getMessage());
        }
    }



    /**
     * Withdraw an amount from a specific savings goal.
     */
    @PostMapping("/withdraw")
    public ResponseEntity<String> withdrawFromSavings(
            @RequestParam String savingsGoalUid,
            @RequestParam BigDecimal amount) {
        try {
            String accountUid = roundUpService.fetchAccountDetails().getAccountUid();
            roundUpService.withdrawFromSavings(accountUid, savingsGoalUid, amount);
            return ResponseEntity.ok(
                    String.format("Successfully withdrew £%.2f from savings goal with ID: %s", amount, savingsGoalUid)
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            System.err.println("Error performing withdrawal: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body("Failed to complete withdrawal: " + e.getMessage());
        }
    }

    /**
     * Fetch all savings goals with their details.
     */
    @GetMapping("/savings-goals")
    public ResponseEntity<List<SavingsGoal>> getSavingsGoals() {
        try {
            // Fetch account UID
            String accountUid = roundUpService.fetchAccountDetails().getAccountUid();

            // Fetch all savings goals for the account
            List<SavingsGoal> savingsGoals = roundUpService.fetchSavingsGoals(accountUid);

            // Return the goals with their details
            return ResponseEntity.ok(savingsGoals);
        } catch (Exception e) {
            System.err.println("Error fetching savings goals: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(null);
        }
    }

    /**
     * Create a new savings goal.
     */
    @PostMapping("/savings-goal")
    public ResponseEntity<String> createSavingsGoal(
            @RequestParam String name,
            @RequestParam BigDecimal targetAmount) {
        try {
            String accountUid = roundUpService.fetchAccountDetails().getAccountUid();
            String savingsGoalUid = roundUpService.createSavingsGoal(accountUid, name, targetAmount);
            return ResponseEntity.ok("Savings goal created with UID: " + savingsGoalUid);
        } catch (Exception e) {
            System.err.println("Error creating savings goal: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body("Failed to create savings goal: " + e.getMessage());
        }
    }
}
