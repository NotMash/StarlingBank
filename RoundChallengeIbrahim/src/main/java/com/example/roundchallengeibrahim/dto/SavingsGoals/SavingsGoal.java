package com.example.roundchallengeibrahim.dto.SavingsGoals;

import com.example.roundchallengeibrahim.dto.Common.Amount;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SavingsGoal {

    @JsonProperty("savingsGoalUid")
    private String savingsGoalUid;

    @JsonProperty("name")
    private String name;

    @JsonProperty("target")
    private Amount target;

    @JsonProperty("totalSaved")
    private Amount totalSaved;

    @JsonProperty("savedPercentage")
    private int savedPercentage;

    @JsonProperty("state")
    private String state;

    // Getters and Setters
    public String getSavingsGoalUid() {
        return savingsGoalUid;
    }

    public void setSavingsGoalUid(String savingsGoalUid) {
        this.savingsGoalUid = savingsGoalUid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Amount getTarget() {
        return target;
    }

    public void setTarget(Amount target) {
        this.target = target;
    }

    public Amount getTotalSaved() {
        return totalSaved;
    }

    public void setTotalSaved(Amount totalSaved) {
        this.totalSaved = totalSaved;
    }

    public int getSavedPercentage() {
        return savedPercentage;
    }

    public void setSavedPercentage(int savedPercentage) {
        this.savedPercentage = savedPercentage;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    // Override toString() for better logging
    @Override
    public String toString() {
        return "SavingsGoal{" +
               "savingsGoalUid='" + savingsGoalUid + '\'' +
               ", name='" + name + '\'' +
               ", target=" + target +
               ", totalSaved=" + totalSaved +
               ", savedPercentage=" + savedPercentage +
               ", state='" + state + '\'' +
               '}';
    }
}
