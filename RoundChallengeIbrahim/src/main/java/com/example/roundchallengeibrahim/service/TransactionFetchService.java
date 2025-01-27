package com.example.roundchallengeibrahim.service;

import com.example.roundchallengeibrahim.dto.Account.AccountDetails;
import com.example.roundchallengeibrahim.dto.Account.AccountResponse;
import com.example.roundchallengeibrahim.dto.Transactions.Transaction;
import com.example.roundchallengeibrahim.dto.Transactions.TransactionResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class TransactionFetchService {

    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final String accessToken;

    private static final String TRANSACTION_ENDPOINT_TEMPLATE = "/api/v2/feed/account/%s/category/%s?changesSince=%s";

    public TransactionFetchService(RestTemplate restTemplate, @Value("${starling.base-url}") String baseUrl, @Value("${starling.access-token}") String accessToken) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
        this.accessToken = accessToken;
    }

    public String fetchAccountUid2() {
        return fetchAccountDetails().getAccountUid();
    }

    public String fetchCategoryUid() {
        return fetchAccountDetails().getDefaultCategory();
    }

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

    public List<Transaction> fetchTransactions() {
    // Fetch account UID and category UID dynamically
    String accountUid = fetchAccountUid2();
    String categoryUid = fetchCategoryUid();

    // Calculate the start time 7 days ago
    OffsetDateTime start = OffsetDateTime.now().minusDays(7);
    String changesSinceParam = start.toString();

    // Log the API call details
    System.out.println("Fetching transactions for account UID: " + accountUid + " and category UID: " + categoryUid + " since: " + changesSinceParam);

    String url = String.format(
        "%s/api/v2/feed/account/%s/category/%s?changesSince=%s",
        baseUrl,
        accountUid,
        categoryUid,
        changesSinceParam
    );

    // Log the URL being used
    System.out.println("Request URL: " + url);

    HttpEntity<Void> requestEntity = new HttpEntity<>(createHeaders());
    ResponseEntity<TransactionResponse> response = restTemplate.exchange(
        url,
        HttpMethod.GET,
        requestEntity,
        TransactionResponse.class
    );

    // Log the response status and headers
    System.out.println("Response Status: " + response.getStatusCode());
    System.out.println("Response Headers: " + response.getHeaders());

    // Check if the response body is null or empty
    if (response.getBody() == null || response.getBody().getTransactions() == null) {
        System.out.println("No transactions found for the given account and category.");
        return Collections.emptyList();
    }

    // Log details of each transaction
    List<Transaction> transactions = response.getBody().getTransactions();
    System.out.println("Fetched " + transactions.size() + " transactions.");
    transactions.forEach(transaction -> {
        System.out.println("Transaction ID: " + transaction.getId());
        System.out.println("Amount: " + transaction.getAmount());
        System.out.println("Direction: " + transaction.getDirection());
        System.out.println("Created: " + transaction.getCreated());
        System.out.println("Narrative: " + transaction.getNarrative());
    });

    return transactions;
}




        private HttpHeaders createHeaders() {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);
            headers.set("Accept", "application/json");
            return headers;
        }
    }
