package com.example.roundchallengeibrahim.dto.Common;

public class Amount {
    private String currency;
    private long minorUnits;

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
