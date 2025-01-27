package com.example.roundchallengeibrahim.dto.Feed;

import com.example.roundchallengeibrahim.dto.Common.Amount;

public class FeedItem {
    private String direction; // "OUT" or "IN"
    private Amount amount;

    // Getters and setters
    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }
}

