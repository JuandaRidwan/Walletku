package com.digipay.digipay.models;

import java.io.Serializable;

/**
 * Created by Ridone-PC on 2/3/2016.
 */
public class DataUsersRegister implements Serializable {
    String email;
    String name;
    String address;
    String phone;
    String password;
    String password_confirmation;
    String referral_id;
    String terms;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword_confirmation(String password_confirmation) {
        this.password_confirmation = password_confirmation;
    }

    public String getPassword_confirmation() {
        return password_confirmation;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    public String getTerms() {
        return terms;
    }

    public void setReferral_id(String referral_id) {
        this.referral_id = referral_id;
    }

    public String getReferral_id() {
        return referral_id;
    }
}
