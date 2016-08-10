package com.digipay.digipay.models;

/**
 * Created by Ridone on 1/27/2016.
 */
public class PayHistory {
    private String id, nominal,amount, total_balance, total_bonus, total_point, status, msisdnHistory, trans_id, paymentTipeHistory, message, provider, serialNumber, date;
    private int image;

    public PayHistory() {

    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAmount() {
        return amount;
    }

    public String getPaymentTipeHistory() {
        return paymentTipeHistory;
    }

    public String getMessage() {
        return message;
    }

    public String getTrans_id() {
        return trans_id;
    }

    public String getMsisdnHistory() {
        return msisdnHistory;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setPaymentTipeHistory(String paymentTipeHistory) {
        this.paymentTipeHistory = paymentTipeHistory;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTrans_id(String trans_id) {
        this.trans_id = trans_id;
    }

    public void setMsisdnHistory(String msisdnHistory) {
        this.msisdnHistory = msisdnHistory;
    }

    public String getNominal() {
        return nominal;
    }

    public void setNominal(String nominal) {
        this.nominal = nominal;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTotal_balance() {
        return total_balance;
    }

    public void setTotal_balance(String total_balance) {
        this.total_balance = total_balance;
    }

    public String getTotal_bonus() {
        return total_bonus;
    }

    public void setTotal_bonus(String total_bonus) {
        this.total_bonus = total_bonus;
    }

    public String getTotal_point() {
        return total_point;
    }

    public void setTotal_point(String total_point) {
        this.total_point = total_point;
    }
}
