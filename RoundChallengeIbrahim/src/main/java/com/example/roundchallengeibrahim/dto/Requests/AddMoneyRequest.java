package com.example.roundchallengeibrahim.dto.Requests;

import com.example.roundchallengeibrahim.dto.Common.Amount;

public class AddMoneyRequest {
    private Amount amount;


    public AddMoneyRequest(Amount amount) {
        this.amount = amount;
    }


    public AddMoneyRequest() { }

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }
}
