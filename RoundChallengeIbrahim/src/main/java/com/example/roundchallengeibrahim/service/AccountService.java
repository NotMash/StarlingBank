package com.example.roundchallengeibrahim.service;

import com.example.roundchallengeibrahim.dto.Account.AccountResponse;
import com.example.roundchallengeibrahim.dto.Account.UserIdentityResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class AccountService {

    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final String accessToken;

    public AccountService(RestTemplate restTemplate,
                          @Value("${starling.base-url}") String baseUrl,
                          @Value("${starling.access-token}") String accessToken) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
        this.accessToken = accessToken;
    }

    public List<AccountResponse.Account> fetchAllAccountsWithBalances() {
        String url = baseUrl + "/api/v2/accounts";

        HttpEntity<Void> requestEntity = new HttpEntity<>(createHeaders());
        ResponseEntity<AccountResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                AccountResponse.class
        );

        if (response.getBody() != null) {
            List<AccountResponse.Account> accounts = response.getBody().getAccounts();

            // Fetch and attach balance for each account
            accounts.forEach(account -> {
                AccountResponse.Balance balance = fetchAccountBalance(account.getAccountUid());
                account.setBalance(balance);
            });

            return accounts;
        }

        throw new RuntimeException("No accounts found for this user!");
    }

    public AccountResponse.Balance fetchAccountBalance(String accountUid) {
        String url = String.format("%s/api/v2/accounts/%s/balance", baseUrl, accountUid);

        HttpEntity<Void> requestEntity = new HttpEntity<>(createHeaders());
        ResponseEntity<AccountResponse.Balance> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                AccountResponse.Balance.class
        );

        if (response.getBody() != null) {
            return response.getBody();
        }

        throw new RuntimeException("Failed to fetch balance for account UID: " + accountUid);
    }

    public UserIdentityResponse fetchUserIdentity() {
        String url = baseUrl + "/api/v2/identity/individual";

        HttpEntity<Void> requestEntity = new HttpEntity<>(createHeaders());
        ResponseEntity<UserIdentityResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                UserIdentityResponse.class
        );

        if (response.getBody() != null) {
            return response.getBody();
        }

        throw new RuntimeException("Failed to fetch user identity!");
    }



    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.set("Accept", "application/json");
        return headers;
    }
}
