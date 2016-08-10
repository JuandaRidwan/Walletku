package com.digipay.digipay.models;

import java.io.Serializable;

public class Destination implements Serializable {
    private String airPortCode, airPortCity;

    public String getAirPortCode() {
        return airPortCode;
    }

    public void setAirPortCode(String airPortCode) {
        this.airPortCode = airPortCode;
    }

    public String getAirPortCity() {
        return airPortCity;
    }

    public void setAirPortCity(String airPortCity) {
        this.airPortCity = airPortCity;
    }
}
