package com.digipay.digipay.models;

import java.io.Serializable;

/**
 * Created by RidoneJuanda on 3/13/2016.
 */
public class Nominal implements Serializable {

    private int idNominal;
    private String amount;
    private String alias;
    private String price;

    @Override
    public String toString() {
        return this.amount;
    }

    public Nominal() {

    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public int getIdNominal() {
        return idNominal;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setIdNominal(int idNominal) {
        this.idNominal = idNominal;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
