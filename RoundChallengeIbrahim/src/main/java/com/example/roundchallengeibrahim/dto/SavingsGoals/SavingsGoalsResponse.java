package com.example.roundchallengeibrahim.dto.SavingsGoals;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class SavingsGoalsResponse {

    @JsonProperty("savingsGoalList")
    private List<SavingsGoal> savingsGoals;

    // Getters and Setters
    public List<SavingsGoal> getSavingsGoals() {
        return savingsGoals;
    }

    public void setSavingsGoals(List<SavingsGoal> savingsGoals) {
        this.savingsGoals = savingsGoals;
    }

    // Override toString() for better logging
    @Override
    public String toString() {
        return "SavingsGoalsResponse{" +
               "savingsGoals=" + savingsGoals +
               '}';
    }
}
