package com.example.roundchallengeibrahim.dto.SavingsGoals;

public class CreateOrUpdateSavingsGoalResponse {
    private String savingsGoalUid;
    private boolean success;

    public String getSavingsGoalUid() {
        return savingsGoalUid;
    }

    public void setSavingsGoalUid(String savingsGoalUid) {
        this.savingsGoalUid = savingsGoalUid;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}