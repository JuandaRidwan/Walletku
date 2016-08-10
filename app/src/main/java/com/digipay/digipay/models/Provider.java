package com.digipay.digipay.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Ridone on 3/13/2016.
 */
public class Provider implements Serializable {
    private int idProvider;
    private String namaProvider;
    private ArrayList<Nominal> nominalList;

    public ArrayList<Nominal> getNominalList() {
        return nominalList;
    }

    public void setNominalList(ArrayList<Nominal> nominalList) {
        this.nominalList = nominalList;
    }


    public void setIdProvider(int idProvider) {
        this.idProvider = idProvider;
    }

    public int getIdProvider() {
        return idProvider;
    }

    public void setNamaProvider(String namaProvider) {
        this.namaProvider = namaProvider;
    }

    public String getNamaProvider() {
        return namaProvider;
    }
}
