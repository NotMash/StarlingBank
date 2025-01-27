package com.example.roundchallengeibrahim.controller;

import com.example.roundchallengeibrahim.dto.Account.AccountResponse;
import com.example.roundchallengeibrahim.dto.Account.UserIdentityResponse;
import com.example.roundchallengeibrahim.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@CrossOrigin(origins = "http://localhost:3000") // Allow frontend access
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse.Account>> getAllAccounts() {

        List<AccountResponse.Account> accounts = accountService.fetchAllAccountsWithBalances();
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/identity/individual")
    public ResponseEntity<UserIdentityResponse> getUserIdentity() {
        try {
            UserIdentityResponse response = accountService.fetchUserIdentity();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(null);
        }
    }



}
