package com.example.roundchallengeibrahim.service;

import com.example.roundchallengeibrahim.dto.Account.AccountDetails;
import com.example.roundchallengeibrahim.dto.Account.AccountResponse;
import com.example.roundchallengeibrahim.dto.Feed.FeedItem;
import com.example.roundchallengeibrahim.dto.Feed.FeedItemsResponse;
import com.example.roundchallengeibrahim.dto.SavingsGoals.CreateOrUpdateSavingsGoalResponse;
import com.example.roundchallengeibrahim.dto.SavingsGoals.SavingsGoal;
import com.example.roundchallengeibrahim.dto.SavingsGoals.SavingsGoalTransferResponse;
import com.example.roundchallengeibrahim.dto.SavingsGoals.SavingsGoalsResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.function.Supplier;

@Service
public class RoundUpService {

    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final String accessToken;
    private final Map<String, Boolean> processedWeeks = new HashMap<>();
    private Supplier<String> inputSupplier = () -> new Scanner(System.in).nextLine();

    public RoundUpService(RestTemplate restTemplate,
                          @Value("${starling.base-url}") String baseUrl,
                          @Value("${starling.access-token}") String accessToken) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
        this.accessToken = accessToken;
    }

    /**
     * Process weekly round-up: fetch transactions, calculate round-up, and transfer to savings goal.
     */
    public void processWeeklyRoundUp(String selectedGoalName) {
        String accountUid = fetchAccountDetails().getAccountUid();

        // Step 1: Fetch transactions and calculate round-up amount
        List<FeedItem> transactions = fetchWeekTransactions(accountUid);
        BigDecimal roundUpAmount = calculateRoundUp(transactions);

        if (roundUpAmount.compareTo(BigDecimal.ZERO) > 0) {
            System.out.printf("Round-up amount to transfer: £%.2f%n", roundUpAmount);

            // Step 2: Use the selected savings goal or create a default one
            String savingsGoalUid = getOrCreateSavingsGoal(accountUid, selectedGoalName);

            // Step 3: Transfer the round-up amount
            transferToSavingsGoal(accountUid, savingsGoalUid, roundUpAmount);
        } else {
            System.out.println("No round-up amount to transfer for this week.");
        }
    }

    private String getOrCreateSavingsGoal(String accountUid, String goalName) {
        String existingGoalUid = findExistingSavingsGoal(accountUid, goalName);

        if (existingGoalUid != null) {
            return existingGoalUid;
        }

        System.out.printf("Creating savings goal: '%s'%n", goalName);
        return createSavingsGoal(accountUid, goalName, BigDecimal.ZERO); // Optional target
    }


    /**
     * Fetch account details.
     */
    public AccountDetails fetchAccountDetails() {
        HttpEntity<Void> requestEntity = new HttpEntity<>(createHeaders());
        ResponseEntity<AccountResponse> response = restTemplate.exchange(
                baseUrl + "/api/v2/accounts",
                HttpMethod.GET,
                requestEntity,
                AccountResponse.class
        );

        if (response.getBody() != null && !response.getBody().getAccounts().isEmpty()) {
            AccountResponse.Account account = response.getBody().getAccounts().get(0);
            return new AccountDetails(account.getAccountUid(), account.getDefaultCategory(), account.getCurrency());
        }

        throw new RuntimeException("No Starling accounts found for this user!");
    }

    /**
     * Fetch transactions from the last 7 days.
     */
    public List<FeedItem> fetchWeekTransactions(String accountUid) {
        OffsetDateTime start = OffsetDateTime.now().minusDays(7);
        String changesSinceParam = start.toString();

        String categoryUid = fetchAccountDetails().getDefaultCategory();
        String url = String.format(
                "%s/api/v2/feed/account/%s/category/%s?changesSince=%s",
                baseUrl, accountUid, categoryUid, changesSinceParam
        );

        HttpEntity<Void> requestEntity = new HttpEntity<>(createHeaders());
        ResponseEntity<FeedItemsResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                FeedItemsResponse.class
        );

        if (response.getBody() != null) {
            return response.getBody().getFeedItems();
        }

        return Collections.emptyList();
    }

    /**
     * Calculate the round-up amount for all transactions.
     */
    public BigDecimal calculateRoundUp(List<FeedItem> feedItems) {
        BigDecimal total = BigDecimal.ZERO;

        for (FeedItem item : feedItems) {
            if ("OUT".equals(item.getDirection())) {
                BigDecimal spend = convertMinorUnitsToPounds(Math.abs(item.getAmount().getMinorUnits()));
                BigDecimal nextWhole = spend.setScale(0, RoundingMode.CEILING);
                total = total.add(nextWhole.subtract(spend));
            }
        }

        return total.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Fetch all savings goals.
     */
    public List<SavingsGoal> fetchSavingsGoals(String accountUid) {
        String url = String.format("%s/api/v2/account/%s/savings-goals", baseUrl, accountUid);
        HttpEntity<Void> requestEntity = new HttpEntity<>(createHeaders());
        ResponseEntity<SavingsGoalsResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                SavingsGoalsResponse.class
        );

        if (response.getBody() != null) {
            return response.getBody().getSavingsGoals();
        }

        throw new RuntimeException("Failed to fetch savings goals for account: " + accountUid);
    }

    private String findExistingSavingsGoal(String accountUid, String goalName) {
        List<SavingsGoal> existingGoals = fetchSavingsGoals(accountUid);

        // Iterate through the existing goals to find a matching one
        for (SavingsGoal goal : existingGoals) {
            if (goalName.equalsIgnoreCase(goal.getName()) && "ACTIVE".equalsIgnoreCase(goal.getState())) {
                return goal.getSavingsGoalUid(); // Return the UID of the matching savings goal
            }
        }
        return null; // No matching goal found
    }


    /**
     * Create a new savings goal.
     */
    public String createSavingsGoal(String accountUid, String goalName, BigDecimal targetAmount) {
        if (goalName == null || goalName.trim().isEmpty()) {
            throw new IllegalArgumentException("Savings goal name cannot be empty.");
        }
        if (targetAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Target amount must be greater than zero.");
        }

        // Check for an existing goal with the same name
        String existingGoalUid = findExistingSavingsGoal(accountUid, goalName);
        if (existingGoalUid != null) {
            System.out.printf("Savings goal '%s' already exists with UID: %s%n", goalName, existingGoalUid);
            return existingGoalUid;
        }

        // Create new goal
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", goalName);
        requestBody.put("currency", fetchAccountDetails().getCurrency());
        requestBody.put("target", Map.of(
                "currency", fetchAccountDetails().getCurrency(),
                "minorUnits", targetAmount.movePointRight(2).longValue()
        ));

        HttpHeaders headers = createHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        String url = String.format("%s/api/v2/account/%s/savings-goals", baseUrl, accountUid);
        ResponseEntity<CreateOrUpdateSavingsGoalResponse> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                requestEntity,
                CreateOrUpdateSavingsGoalResponse.class
        );

        if (response.getBody() != null && response.getBody().isSuccess()) {
            return response.getBody().getSavingsGoalUid();
        }

        throw new RuntimeException("Failed to create savings goal");
    }



    /**
     * Transfer a round-up amount to the savings goal.
     */
    public void transferToSavingsGoal(String accountUid, String savingsGoalUid, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        long minorUnits = amount.movePointRight(2).longValue();
        Map<String, Object> amountMap = new HashMap<>();
        amountMap.put("currency", fetchAccountDetails().getCurrency());
        amountMap.put("minorUnits", minorUnits);

        HttpHeaders headers = createHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(Collections.singletonMap("amount", amountMap), headers);

        String transferUid = UUID.randomUUID().toString();
        String url = String.format("%s/api/v2/account/%s/savings-goals/%s/add-money/%s", baseUrl, accountUid, savingsGoalUid, transferUid);

        ResponseEntity<SavingsGoalTransferResponse> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                requestEntity,
                SavingsGoalTransferResponse.class
        );

        if (response.getBody() == null || !response.getBody().isSuccess()) {
            throw new RuntimeException("Failed to transfer money to savings goal");
        }

        System.out.printf("Successfully transferred £%.2f to savings goal with ID %s%n", amount, savingsGoalUid);
    }
    //extra
    /**
     * Withdraw from a savings goal.
     */

    public void withdrawFromSavings(String accountUid, String savingsGoalUid, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        // Convert pounds to minor units (pence)
        long minorUnits = amount.movePointRight(2).longValue();
        Map<String, Object> amountMap = new HashMap<>();
        amountMap.put("currency", fetchAccountDetails().getCurrency());
        amountMap.put("minorUnits", minorUnits);

        HttpHeaders headers = createHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(Collections.singletonMap("amount", amountMap), headers);

        // Generate a unique transfer ID
        String transferUid = UUID.randomUUID().toString();

        // Prepare the URL
        String url = String.format(
            "%s/api/v2/account/%s/savings-goals/%s/withdraw-money/%s",
            baseUrl, accountUid, savingsGoalUid, transferUid
        );

        // Execute the PUT request
        ResponseEntity<SavingsGoalTransferResponse> response = restTemplate.exchange(
            url,
            HttpMethod.PUT,
            requestEntity,
            SavingsGoalTransferResponse.class
        );

        if (response.getBody() == null || !response.getBody().isSuccess()) {
            throw new RuntimeException("Failed to withdraw money from savings goal");
        }

        System.out.printf("Successfully withdrew £%.2f from savings goal with ID %s%n", amount, savingsGoalUid);
    }


    /**
     * Helper methods.
     */
    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }

    private BigDecimal convertMinorUnitsToPounds(long minorUnits) {
        return BigDecimal.valueOf(minorUnits).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }



}
