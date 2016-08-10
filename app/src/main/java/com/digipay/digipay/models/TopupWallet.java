package com.digipay.digipay.models;

/**
 * Created by Ridone on 3/9/2016.
 */
public class TopupWallet {

    private int id;
    private String userId, sourceType, fromSourceName, fromAccountName, fromAccountNumber,
            toSourceName, toAccountName, toAccountNumber, amount, status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getFromSourceName() {
        return fromSourceName;
    }

    public void setFromSourceName(String fromSourceName) {
        this.fromSourceName = fromSourceName;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAmount() {
        return amount;
    }

    public String getFromAccountName() {
        return fromAccountName;
    }

    public String getFromAccountNumber() {
        return fromAccountNumber;
    }

    public String getToAccountName() {
        return toAccountName;
    }

    public String getToAccountNumber() {
        return toAccountNumber;
    }

    public String getToSourceName() {
        return toSourceName;
    }

    public void setFromAccountName(String fromAccountName) {
        this.fromAccountName = fromAccountName;
    }

    public void setFromAccountNumber(String fromAccountNumber) {
        this.fromAccountNumber = fromAccountNumber;
    }

    public void setToAccountName(String toAccountName) {
        this.toAccountName = toAccountName;
    }

    public void setToAccountNumber(String toAccountNumber) {
        this.toAccountNumber = toAccountNumber;
    }

    public void setToSourceName(String toSourceName) {
        this.toSourceName = toSourceName;
    }
}

