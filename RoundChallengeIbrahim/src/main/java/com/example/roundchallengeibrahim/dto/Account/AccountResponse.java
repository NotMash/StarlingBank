package com.example.roundchallengeibrahim.dto.Account;

import java.util.List;

public class AccountResponse {
    private List<Account> accounts;

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public static class Account {
        private String accountUid;
        private String accountType;
        private String defaultCategory;
        private String currency;
        private String createdAt;
        private String name;
        private Balance balance;

        // Getters and Setters
        public String getAccountUid() {
            return accountUid;
        }

        public void setAccountUid(String accountUid) {
            this.accountUid = accountUid;
        }

        public String getAccountType() {
            return accountType;
        }

        public void setAccountType(String accountType) {
            this.accountType = accountType;
        }

        public String getDefaultCategory() {
            return defaultCategory;
        }

        public void setDefaultCategory(String defaultCategory) {
            this.defaultCategory = defaultCategory;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Balance getBalance() {
            return balance;
        }

        public void setBalance(Balance balance) {
            this.balance = balance;
        }
    }

    public static class Balance {
        private ClearedBalance clearedBalance;
        private EffectiveBalance effectiveBalance;
        private PendingTransactions pendingTransactions;
        private AcceptedOverdraft acceptedOverdraft;
        private Amount amount;
        private TotalClearedBalance totalClearedBalance;
        private TotalEffectiveBalance totalEffectiveBalance;

        // Getters and setters
        public ClearedBalance getClearedBalance() {
            return clearedBalance;
        }

        public void setClearedBalance(ClearedBalance clearedBalance) {
            this.clearedBalance = clearedBalance;
        }

        public EffectiveBalance getEffectiveBalance() {
            return effectiveBalance;
        }

        public void setEffectiveBalance(EffectiveBalance effectiveBalance) {
            this.effectiveBalance = effectiveBalance;
        }

        public PendingTransactions getPendingTransactions() {
            return pendingTransactions;
        }

        public void setPendingTransactions(PendingTransactions pendingTransactions) {
            this.pendingTransactions = pendingTransactions;
        }

        public AcceptedOverdraft getAcceptedOverdraft() {
            return acceptedOverdraft;
        }

        public void setAcceptedOverdraft(AcceptedOverdraft acceptedOverdraft) {
            this.acceptedOverdraft = acceptedOverdraft;
        }

        public Amount getAmount() {
            return amount;
        }

        public void setAmount(Amount amount) {
            this.amount = amount;
        }

        public TotalClearedBalance getTotalClearedBalance() {
            return totalClearedBalance;
        }

        public void setTotalClearedBalance(TotalClearedBalance totalClearedBalance) {
            this.totalClearedBalance = totalClearedBalance;
        }

        public TotalEffectiveBalance getTotalEffectiveBalance() {
            return totalEffectiveBalance;
        }

        public void setTotalEffectiveBalance(TotalEffectiveBalance totalEffectiveBalance) {
            this.totalEffectiveBalance = totalEffectiveBalance;
        }

        public static class ClearedBalance {
            private String currency;
            private long minorUnits;

            // Getters and setters
            public String getCurrency() {
                return currency;
            }

            public void setCurrency(String currency) {
                this.currency = currency;
            }

            public long getMinorUnits() {
                return minorUnits;
            }

            public void setMinorUnits(long minorUnits) {
                this.minorUnits = minorUnits;
            }
        }

        public static class EffectiveBalance {
            private String currency;
            private long minorUnits;

            // Getters and setters
            public String getCurrency() {
                return currency;
            }

            public void setCurrency(String currency) {
                this.currency = currency;
            }

            public long getMinorUnits() {
                return minorUnits;
            }

            public void setMinorUnits(long minorUnits) {
                this.minorUnits = minorUnits;
            }
        }

        public static class PendingTransactions {
            private String currency;
            private long minorUnits;

            // Getters and setters
            public String getCurrency() {
                return currency;
            }

            public void setCurrency(String currency) {
                this.currency = currency;
            }

            public long getMinorUnits() {
                return minorUnits;
            }

            public void setMinorUnits(long minorUnits) {
                this.minorUnits = minorUnits;
            }
        }

        public static class AcceptedOverdraft {
            private String currency;
            private long minorUnits;

            // Getters and setters
            public String getCurrency() {
                return currency;
            }

            public void setCurrency(String currency) {
                this.currency = currency;
            }

            public long getMinorUnits() {
                return minorUnits;
            }

            public void setMinorUnits(long minorUnits) {
                this.minorUnits = minorUnits;
            }
        }

        public static class Amount {
            private String currency;
            private long minorUnits;

            // Getters and setters
            public String getCurrency() {
                return currency;
            }

            public void setCurrency(String currency) {
                this.currency = currency;
            }

            public long getMinorUnits() {
                return minorUnits;
            }

            public void setMinorUnits(long minorUnits) {
                this.minorUnits = minorUnits;
            }
        }

        public static class TotalClearedBalance {
            private String currency;
            private long minorUnits;

            // Getters and setters
            public String getCurrency() {
                return currency;
            }

            public void setCurrency(String currency) {
                this.currency = currency;
            }

            public long getMinorUnits() {
                return minorUnits;
            }

            public void setMinorUnits(long minorUnits) {
                this.minorUnits = minorUnits;
            }
        }

        public static class TotalEffectiveBalance {
            private String currency;
            private long minorUnits;

            // Getters and setters
            public String getCurrency() {
                return currency;
            }

            public void setCurrency(String currency) {
                this.currency = currency;
            }

            public long getMinorUnits() {
                return minorUnits;
            }

            public void setMinorUnits(long minorUnits) {
                this.minorUnits = minorUnits;
            }
        }
    }
}
