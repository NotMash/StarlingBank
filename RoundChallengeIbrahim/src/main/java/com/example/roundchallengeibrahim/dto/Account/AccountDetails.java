package com.example.roundchallengeibrahim.dto.Account;

public class AccountDetails {
    private final String accountUid;
    private final String defaultCategory;
    private final String currency;

    public AccountDetails(String accountUid, String defaultCategory, String currency) {
        this.accountUid = accountUid;
        this.defaultCategory = defaultCategory;
        this.currency = currency;
    }

    public String getAccountUid() {
        return accountUid;
    }

    public String getDefaultCategory() {
        return defaultCategory;
    }

    public String getCurrency() {
        return currency;
    }
}