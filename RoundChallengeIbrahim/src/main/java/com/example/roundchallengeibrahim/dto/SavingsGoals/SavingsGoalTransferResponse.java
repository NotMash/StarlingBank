package com.example.roundchallengeibrahim.dto.SavingsGoals;

public class SavingsGoalTransferResponse {
    private String transferUid;
    private boolean success;

    public String getTransferUid() {
        return transferUid;
    }

    public void setTransferUid(String transferUid) {
        this.transferUid = transferUid;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}