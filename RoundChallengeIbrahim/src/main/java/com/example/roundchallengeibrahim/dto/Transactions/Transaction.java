package com.example.roundchallengeibrahim.dto.Transactions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction {

    @JsonProperty("feedItemUid")
    private String id;

    @JsonProperty("amount")
    private AmountDetails amount;

    @JsonProperty("direction")
    private String direction;

    @JsonProperty("transactionTime")
    private OffsetDateTime created;

    @JsonProperty("counterPartyName")
    private String narrative;

    // Inner class to handle currency and minor units
    public static class AmountDetails {
        @JsonProperty("currency")
        private String currency;

        @JsonProperty("minorUnits")
        private BigDecimal minorUnits;

        // Getters and Setters
        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public BigDecimal getMinorUnits() {
            return minorUnits;
        }

        public void setMinorUnits(BigDecimal minorUnits) {
            this.minorUnits = minorUnits;
        }
    }

    // Getters
    public String getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount != null ? amount.getMinorUnits() : null;
    }

    public String getCurrency() {
        return amount != null ? amount.getCurrency() : null;
    }

    public String getDirection() {
        return direction;
    }

    public OffsetDateTime getCreated() {
        return created;
    }

    public String getNarrative() {
        return narrative;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setAmount(AmountDetails amount) {
        this.amount = amount;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public void setCreated(OffsetDateTime created) {
        this.created = created;
    }

    public void setNarrative(String narrative) {
        this.narrative = narrative;
    }
}
