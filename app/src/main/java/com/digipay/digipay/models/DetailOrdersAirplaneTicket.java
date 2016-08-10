package com.digipay.digipay.models;

import java.io.Serializable;

public class DetailOrdersAirplaneTicket implements Serializable {
    private String psgrType, tittle, firstName, LastName, idPsgr, birthDate, paspor, expirePaspor, countryPaspor, parent;

    // customer
    private String cusName, cusPhone, cusEmail;

    // depart
    private String airlineId, airlineName, type, from, via, to, date, fno, etd, eta, classDepart, className;

    public String getPsgrType() {
        return psgrType;
    }

    public void setPsgrType(String psgrType) {
        this.psgrType = psgrType;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getIdPsgr() {
        return idPsgr;
    }

    public void setIdPsgr(String idPsgr) {
        this.idPsgr = idPsgr;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getPaspor() {
        return paspor;
    }

    public void setPaspor(String paspor) {
        this.paspor = paspor;
    }

    public String getExpirePaspor() {
        return expirePaspor;
    }

    public void setExpirePaspor(String expirePaspor) {
        this.expirePaspor = expirePaspor;
    }

    public String getCountryPaspor() {
        return countryPaspor;
    }

    public void setCountryPaspor(String countryPaspor) {
        this.countryPaspor = countryPaspor;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public String getCusPhone() {
        return cusPhone;
    }

    public void setCusPhone(String cusPhone) {
        this.cusPhone = cusPhone;
    }

    public String getCusEmail() {
        return cusEmail;
    }

    public void setCusEmail(String cusEmail) {
        this.cusEmail = cusEmail;
    }

    public String getAirlineId() {
        return airlineId;
    }

    public void setAirlineId(String airlineId) {
        this.airlineId = airlineId;
    }

    public String getAirlineName() {
        return airlineName;
    }

    public void setAirlineName(String airlineName) {
        this.airlineName = airlineName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getVia() {
        return via;
    }

    public void setVia(String via) {
        this.via = via;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFno() {
        return fno;
    }

    public void setFno(String fno) {
        this.fno = fno;
    }

    public String getEtd() {
        return etd;
    }

    public void setEtd(String etd) {
        this.etd = etd;
    }

    public String getEta() {
        return eta;
    }

    public void setEta(String eta) {
        this.eta = eta;
    }

    public String getClassDepart() {
        return classDepart;
    }

    public void setClassDepart(String classDepart) {
        this.classDepart = classDepart;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
