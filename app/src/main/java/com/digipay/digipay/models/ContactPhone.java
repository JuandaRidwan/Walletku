package com.digipay.digipay.models;

import java.io.Serializable;

/**
 * Created by Ridone-PC on 2/2/2016.
 */
public class ContactPhone implements Serializable {
    String contactName;
    String phoneNumber;


    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
