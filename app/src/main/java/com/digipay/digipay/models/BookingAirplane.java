package com.digipay.digipay.models;

public class BookingAirplane {
    // set Session Id
    private String sessionId;

    // search info
    private String searchInfo, roundTrip, from, to, depart, departReturn, adult, child, infant;

    // airlines Details
    private String airlineId, airlineCode, airlineName;

    // schedule
    private String airlineIdDepart, airlineNameDepart, typeSchedule, fromDepart, toDepart, dateDepart, fnoDepart, etdDepart, etaDepart, viaDepart;

    // connecting flight
    private String fromConnecting, toConnecting, dateConnecting, fnoConnecting, etdConnecting, etaConnecting, viaConnecting;

    // class
    private String classId, className, classTitle, classPrice, classSeat, classValue;

    // class next
    private String classIdNext, classNameNext, classNext, classPriceNext, classSeatNext, classValueNext;

    // airline return
    private String airlineIdReturn, airlineNameReturn, typeReturn, fromReturn, toReturn, dateReturn, fnoReturn, etdReturn, etaReturn, viaReturn;

    // connecting flight return
    private String fromConnectingReturn, toConnectingReturn, dateConnectingReturn, fnoConnectingReturn, etdConnectingReturn, etaConnectingReturn, viaConnectingReturn;

    // class return
    private String classReturn, classNameReturn, classPriceReturn, classSeatReturn, classValueReturn;

    // class return next
    private String classIdReturnNext, classReturnNext, classNameReturnNext, classPriceReturnNext, classSeatReturnNext, classValueReturnNext;

    public BookingAirplane() {
    }

    public String getSearchInfo() {
        return searchInfo;
    }

    public void setSearchInfo(String searchInfo) {
        this.searchInfo = searchInfo;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getRoundTrip() {
        return roundTrip;
    }

    public void setRoundTrip(String roundTrip) {
        this.roundTrip = roundTrip;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getDepart() {
        return depart;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }

    public String getDepartReturn() {
        return departReturn;
    }

    public void setDepartReturn(String departReturn) {
        this.departReturn = departReturn;
    }

    public String getAdult() {
        return adult;
    }

    public void setAdult(String adult) {
        this.adult = adult;
    }

    public String getChild() {
        return child;
    }

    public void setChild(String child) {
        this.child = child;
    }

    public String getInfant() {
        return infant;
    }

    public void setInfant(String infant) {
        this.infant = infant;
    }

    public String getAirlineId() {
        return airlineId;
    }

    public void setAirlineId(String airlineId) {
        this.airlineId = airlineId;
    }

    public String getAirlineCode() {
        return airlineCode;
    }

    public void setAirlineCode(String airlineCode) {
        this.airlineCode = airlineCode;
    }

    public String getAirlineName() {
        return airlineName;
    }

    public void setAirlineName(String airlineName) {
        this.airlineName = airlineName;
    }

    public String getAirlineIdDepart() {
        return airlineIdDepart;
    }

    public void setAirlineIdDepart(String airlineIdDepart) {
        this.airlineIdDepart = airlineIdDepart;
    }


    public String getFromDepart() {
        return fromDepart;
    }

    public void setFromDepart(String fromDepart) {
        this.fromDepart = fromDepart;
    }

    public String getToDepart() {
        return toDepart;
    }

    public void setToDepart(String toDepart) {
        this.toDepart = toDepart;
    }

    public String getAirlineNameDepart() {
        return airlineNameDepart;
    }

    public void setAirlineNameDepart(String airlineNameDepart) {
        this.airlineNameDepart = airlineNameDepart;
    }

    public String getDateDepart() {
        return dateDepart;
    }

    public void setDateDepart(String dateDepart) {
        this.dateDepart = dateDepart;
    }

    public String getFnoDepart() {
        return fnoDepart;
    }

    public void setFnoDepart(String fnoDepart) {
        this.fnoDepart = fnoDepart;
    }

    public String getEtdDepart() {
        return etdDepart;
    }

    public void setEtdDepart(String etdDepart) {
        this.etdDepart = etdDepart;
    }

    public String getEtaDepart() {
        return etaDepart;
    }

    public void setEtaDepart(String etaDepart) {
        this.etaDepart = etaDepart;
    }

    public String getViaDepart() {
        return viaDepart;
    }

    public void setViaDepart(String viaDepart) {
        this.viaDepart = viaDepart;
    }

    public String getFromConnecting() {
        return fromConnecting;
    }

    public void setFromConnecting(String fromConnecting) {
        this.fromConnecting = fromConnecting;
    }

    public String getToConnecting() {
        return toConnecting;
    }

    public void setToConnecting(String toConnecting) {
        this.toConnecting = toConnecting;
    }

    public String getDateConnecting() {
        return dateConnecting;
    }

    public void setDateConnecting(String dateConnecting) {
        this.dateConnecting = dateConnecting;
    }

    public String getFnoConnecting() {
        return fnoConnecting;
    }

    public void setFnoConnecting(String fnoConnecting) {
        this.fnoConnecting = fnoConnecting;
    }

    public String getEtdConnecting() {
        return etdConnecting;
    }

    public void setEtdConnecting(String etdConnecting) {
        this.etdConnecting = etdConnecting;
    }

    public String getEtaConnecting() {
        return etaConnecting;
    }

    public void setEtaConnecting(String etaConnecting) {
        this.etaConnecting = etaConnecting;
    }

    public String getViaConnecting() {
        return viaConnecting;
    }

    public void setViaConnecting(String viaConnecting) {
        this.viaConnecting = viaConnecting;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassTitle() {
        return classTitle;
    }

    public void setClassTitle(String classTitle) {
        this.classTitle = classTitle;
    }

    public String getClassPrice() {
        return classPrice;
    }

    public void setClassPrice(String classPrice) {
        this.classPrice = classPrice;
    }

    public String getClassSeat() {
        return classSeat;
    }

    public void setClassSeat(String classSeat) {
        this.classSeat = classSeat;
    }

    public String getClassValue() {
        return classValue;
    }

    public void setClassValue(String classValue) {
        this.classValue = classValue;
    }

    public String getClassIdNext() {
        return classIdNext;
    }

    public void setClassIdNext(String classIdNext) {
        this.classIdNext = classIdNext;
    }

    public String getClassNameNext() {
        return classNameNext;
    }

    public void setClassNameNext(String classNameNext) {
        this.classNameNext = classNameNext;
    }

    public String getClassNext() {
        return classNext;
    }

    public void setClassNext(String classNext) {
        this.classNext = classNext;
    }

    public String getClassSeatNext() {
        return classSeatNext;
    }

    public void setClassSeatNext(String classSeatNext) {
        this.classSeatNext = classSeatNext;
    }

    public String getClassValueNext() {
        return classValueNext;
    }

    public void setClassValueNext(String classValueNext) {
        this.classValueNext = classValueNext;
    }

    public String getClassPriceNext() {
        return classPriceNext;
    }

    public void setClassPriceNext(String classPriceNext) {
        this.classPriceNext = classPriceNext;
    }

    public String getAirlineIdReturn() {
        return airlineIdReturn;
    }

    public void setAirlineIdReturn(String airlineIdReturn) {
        this.airlineIdReturn = airlineIdReturn;
    }

    public String getAirlineNameReturn() {
        return airlineNameReturn;
    }

    public void setAirlineNameReturn(String airlineNameReturn) {
        this.airlineNameReturn = airlineNameReturn;
    }

    public String getTypeReturn() {
        return typeReturn;
    }

    public void setTypeReturn(String typeReturn) {
        this.typeReturn = typeReturn;
    }

    public String getFromReturn() {
        return fromReturn;
    }

    public void setFromReturn(String fromReturn) {
        this.fromReturn = fromReturn;
    }

    public String getToReturn() {
        return toReturn;
    }

    public void setToReturn(String toReturn) {
        this.toReturn = toReturn;
    }

    public String getDateReturn() {
        return dateReturn;
    }

    public void setDateReturn(String dateReturn) {
        this.dateReturn = dateReturn;
    }

    public String getFnoReturn() {
        return fnoReturn;
    }

    public void setFnoReturn(String fnoReturn) {
        this.fnoReturn = fnoReturn;
    }

    public String getEtdReturn() {
        return etdReturn;
    }

    public void setEtdReturn(String etdReturn) {
        this.etdReturn = etdReturn;
    }

    public String getEtaReturn() {
        return etaReturn;
    }

    public void setEtaReturn(String etaReturn) {
        this.etaReturn = etaReturn;
    }

    public String getViaReturn() {
        return viaReturn;
    }

    public void setViaReturn(String viaReturn) {
        this.viaReturn = viaReturn;
    }

    public String getFromConnectingReturn() {
        return fromConnectingReturn;
    }

    public void setFromConnectingReturn(String fromConnectingReturn) {
        this.fromConnectingReturn = fromConnectingReturn;
    }

    public String getToConnectingReturn() {
        return toConnectingReturn;
    }

    public void setToConnectingReturn(String toConnectingReturn) {
        this.toConnectingReturn = toConnectingReturn;
    }

    public String getDateConnectingReturn() {
        return dateConnectingReturn;
    }

    public void setDateConnectingReturn(String dateConnectingReturn) {
        this.dateConnectingReturn = dateConnectingReturn;
    }

    public String getFnoConnectingReturn() {
        return fnoConnectingReturn;
    }

    public void setFnoConnectingReturn(String fnoConnectingReturn) {
        this.fnoConnectingReturn = fnoConnectingReturn;
    }

    public String getEtdConnectingReturn() {
        return etdConnectingReturn;
    }

    public void setEtdConnectingReturn(String etdConnectingReturn) {
        this.etdConnectingReturn = etdConnectingReturn;
    }

    public String getEtaConnectingReturn() {
        return etaConnectingReturn;
    }

    public void setEtaConnectingReturn(String etaConnectingReturn) {
        this.etaConnectingReturn = etaConnectingReturn;
    }

    public String getViaConnectingReturn() {
        return viaConnectingReturn;
    }

    public void setViaConnectingReturn(String viaConnectingReturn) {
        this.viaConnectingReturn = viaConnectingReturn;
    }

    public String getClassNameReturn() {
        return classNameReturn;
    }

    public void setClassNameReturn(String classNameReturn) {
        this.classNameReturn = classNameReturn;
    }

    public String getClassPriceReturn() {
        return classPriceReturn;
    }

    public void setClassPriceReturn(String classPriceReturn) {
        this.classPriceReturn = classPriceReturn;
    }

    public String getClassSeatReturn() {
        return classSeatReturn;
    }

    public void setClassSeatReturn(String classSeatReturn) {
        this.classSeatReturn = classSeatReturn;
    }

    public String getClassValueReturn() {
        return classValueReturn;
    }

    public void setClassValueReturn(String classValueReturn) {
        this.classValueReturn = classValueReturn;
    }

    public String getClassIdReturnNext() {
        return classIdReturnNext;
    }

    public void setClassIdReturnNext(String classIdReturnNext) {
        this.classIdReturnNext = classIdReturnNext;
    }

    public String getClassNameReturnNext() {
        return classNameReturnNext;
    }

    public void setClassNameReturnNext(String classNameReturnNext) {
        this.classNameReturnNext = classNameReturnNext;
    }

    public String getClassPriceReturnNext() {
        return classPriceReturnNext;
    }

    public void setClassPriceReturnNext(String classPriceReturnNext) {
        this.classPriceReturnNext = classPriceReturnNext;
    }

    public String getClassSeatReturnNext() {
        return classSeatReturnNext;
    }

    public void setClassSeatReturnNext(String classSeatReturnNext) {
        this.classSeatReturnNext = classSeatReturnNext;
    }

    public String getClassValueReturnNext() {
        return classValueReturnNext;
    }

    public void setClassValueReturnNext(String classValueReturnNext) {
        this.classValueReturnNext = classValueReturnNext;
    }

    public String getTypeSchedule() {
        return typeSchedule;
    }

    public void setTypeSchedule(String typeSchedule) {
        this.typeSchedule = typeSchedule;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getClassReturn() {
        return classReturn;
    }

    public void setClassReturn(String classReturn) {
        this.classReturn = classReturn;
    }

    public String getClassReturnNext() {
        return classReturnNext;
    }

    public void setClassReturnNext(String classReturnNext) {
        this.classReturnNext = classReturnNext;
    }
}
