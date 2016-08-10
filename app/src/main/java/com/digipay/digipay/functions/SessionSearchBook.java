package com.digipay.digipay.functions;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

@SuppressLint("CommitPrefEdits")
public class SessionSearchBook {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "SearchBook";
    // search Info
    private static final String KEY_AIRLINE = "airline";
    private static final String KEY_ROUNDTRIP = "roundtrip";
    private static final String KEY_FROM = "from";
    private static final String KEY_TO = "to";
    private static final String KEY_DEPART = "depart";
    private static final String KEY_RETURN = "return";
    private static final String KEY_ADULT = "adult";
    private static final String KEY_CHILD = "child";
    private static final String KEY_INFANT = "infant";

    // selected by pos input cutomer date airplane adapter
    private static final String KEY_POS = "pos";
    private static final String KEY_TITTLE = "tittle";
    private static final String KEY_NAMA_PENUMPANG = "nama_penumpang";
    private static final String KEY_JENIS_IDENTITAS = "jenis_identitas";
    private static final String KEY_NO_IDENTITAS = "no_identitas";
    private static final String KEY_BIRTHDATE = "birthdate";
    private static final String KEY_CITY_DEPART = "city_depart";
    private static final String KEY_CITY_DESTINATION = "city_destination";

    // depart
    private static final String KEY_AIRLINE_NAME_DEPART = "airline_name_depart";
    private static final String KEY_FROM_DEPART = "from_depart";
    private static final String KEY_TO_DEPART = "to_depart";
    private static final String KEY_TYPE_DEPART = "type_depart";
    private static final String KEY_DATE_DEPART = "date_depart";
    private static final String KEY_ETD_DEPART = "etd_depart";
    private static final String KEY_ETA_DEPART = "eta_depart";

    // add optional
    private static final String KEY_BOOK_ID = "book_id";
    private static final String KEY_FIRSTNAME_OPT = "firstname";
    private static final String KEY_LASTNAME_OPT = "lastname";
    private static final String KEY_NO_IDENTITAS_OPT = "no_identitas";

    // price book
    private static final String KEY_TICKET_PRICE = "ticket_price";
    private static final String KEY_AERO_COMISI = "aero_comisi";
    private static final String KEY_CABANG_COMISI = "cabang_comisi";
    private static final String KEY_AGENT_COMISI = "agent_comisi";
    private static final String KEY_AGENT_PRICE = "agent_price";
    private static final String KEY_AGENT_MARGIN = "agent_margin";
    private static final String KEY_ASURANSI = "asuransi";
    private static final String KEY_COMISI_ASURANSI = "comisi_asuransi";
    private static final String KEY_TOTAL_PRICE = "total_price";
    private static final String KEY_ADMIN_FEE = "admin_fee";

    // Constructor
    public SessionSearchBook(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setAirline(String airline) {
        editor.putString(KEY_AIRLINE, airline);
        editor.commit();
    }

    public String getAirline() {
        return pref.getString(KEY_AIRLINE, "");
    }

    public void setRoundtrip(String roundtrip) {
        editor.putString(KEY_ROUNDTRIP, roundtrip);
        editor.commit();
    }

    public String getRundtrip() {
        return pref.getString(KEY_ROUNDTRIP, "");
    }

    public void setFromBook(String from) {
        editor.putString(KEY_FROM, from);
        editor.commit();
    }

    public String getFromBook() {
        return pref.getString(KEY_FROM, "");
    }

    public void setToBook(String to) {
        editor.putString(KEY_TO, to);
        editor.commit();
    }

    public String getToBook() {
        return pref.getString(KEY_TO, "");
    }

    public void setDepart(String depart) {
        editor.putString(KEY_DEPART, depart);
        editor.commit();
    }

    public String getDepart() {
        return pref.getString(KEY_DEPART, "");
    }

    public void setReturnBook(String returnBook) {
        editor.putString(KEY_RETURN, returnBook);
        editor.commit();
    }

    public String getReturnBook() {
        return pref.getString(KEY_RETURN, "");
    }

    public void setAdult(String adult) {
        editor.putString(KEY_ADULT, adult);
        editor.commit();
    }

    public String getAdult() {
        return pref.getString(KEY_ADULT, "");
    }

    public void setChild(String child) {
        editor.putString(KEY_CHILD, child);
        editor.commit();
    }

    public String getChild() {
        return pref.getString(KEY_CHILD, "");
    }

    public void setInfant(String infant) {
        editor.putString(KEY_INFANT, infant);
        editor.commit();
    }

    public String getInfant() {
        return pref.getString(KEY_INFANT, "");
    }

    // adapter click
    public void setPos(String posisi) {
        editor.putString(KEY_POS, posisi);
        editor.commit();
    }

    public String getPos() {
        return pref.getString(KEY_POS, "");
    }

    public void setTittle(String tittle) {
        editor.putString(KEY_TITTLE, tittle);
        editor.commit();
    }

    public String getTittle() {
        return pref.getString(KEY_TITTLE, "");
    }

    public void setNamaPenumpang(String namaPenumpang) {
        editor.putString(KEY_NAMA_PENUMPANG, namaPenumpang);
        editor.commit();
    }

    public String getNamaPenumpang() {
        return pref.getString(KEY_NAMA_PENUMPANG, "");
    }

    public void setJenisIdentitas(String jenisIdentitas) {
        editor.putString(KEY_JENIS_IDENTITAS, jenisIdentitas);
        editor.commit();
    }

    public String getJenisIdentitas() {
        return pref.getString(KEY_JENIS_IDENTITAS, "");
    }

    public void setNoIdentitas(String noIdentitas) {
        editor.putString(KEY_NAMA_PENUMPANG, noIdentitas);
        editor.commit();
    }

    public String getNoIdentitas() {
        return pref.getString(KEY_NO_IDENTITAS, "");
    }

    public void setBirtdate(String birtdate) {
        editor.putString(KEY_BIRTHDATE, birtdate);
        editor.commit();
    }

    public String getBirthdate() {
        return pref.getString(KEY_BIRTHDATE, "");
    }

    public void setCityDepart(String cityDepart) {
        editor.putString(KEY_CITY_DEPART, cityDepart);
        editor.commit();
    }

    public String getCityDepart() {
        return pref.getString(KEY_CITY_DEPART, "");
    }

    public void setCityDestination(String cityDestination) {
        editor.putString(KEY_CITY_DESTINATION, cityDestination);
        editor.commit();
    }

    public String getCityDestination() {
        return pref.getString(KEY_CITY_DESTINATION, "");
    }

    public void setAirlineNameDepart(String airlineName) {
        editor.putString(KEY_AIRLINE_NAME_DEPART, airlineName);
        editor.commit();
    }

    public String getAirlineNameDepart() {
        return pref.getString(KEY_AIRLINE_NAME_DEPART, "");
    }

    public void setFromDepart(String fromDepart) {
        editor.putString(KEY_FROM_DEPART, fromDepart);
        editor.commit();
    }

    public String getFromDepart() {
        return pref.getString(KEY_FROM_DEPART, "");
    }

    public void setToDepart(String toDepart) {
        editor.putString(KEY_TO_DEPART, toDepart);
        editor.commit();
    }

    public String getToDepart() {
        return pref.getString(KEY_TO_DEPART, "");
    }

    public void setTypeDepart(String typeDepart) {
        editor.putString(KEY_TYPE_DEPART, typeDepart);
        editor.commit();
    }

    public String getTypeDepart() {
        return pref.getString(KEY_TYPE_DEPART, "");
    }

    public void setDateDepart(String dateDepart) {
        editor.putString(KEY_DATE_DEPART, dateDepart);
        editor.commit();
    }

    public String getDateDepart() {
        return pref.getString(KEY_DATE_DEPART, "");
    }

    public void setEtdDepart(String etdDepart) {
        editor.putString(KEY_ETD_DEPART, etdDepart);
        editor.commit();
    }

    public String getEtdDepart() {
        return pref.getString(KEY_ETD_DEPART, "");
    }

    public void setEtaDepart(String etaDepart) {
        editor.putString(KEY_ETA_DEPART, etaDepart);
        editor.commit();
    }

    public String getEtaDepart() {
        return pref.getString(KEY_ETA_DEPART, "");
    }

    public void setFirstnameOpt(String firstnameOpt) {
        editor.putString(KEY_FIRSTNAME_OPT, firstnameOpt);
        editor.commit();
    }

    public String getFirstNameOpt() {
        return pref.getString(KEY_FIRSTNAME_OPT, "");
    }

    public void setLastnameOpt(String lastnameOpt) {
        editor.putString(KEY_LASTNAME_OPT, lastnameOpt);
        editor.commit();
    }

    public String getLastnameOpt() {
        return pref.getString(KEY_LASTNAME_OPT, "");
    }

    public void setNoIdentitasOpt(String noIdentitasOpt) {
        editor.putString(KEY_NO_IDENTITAS_OPT, noIdentitasOpt);
        editor.commit();
    }

    public String getNoIdentitasOpt() {
        return pref.getString(KEY_NO_IDENTITAS_OPT, "");
    }

    public void setBookId(String bookId) {
        editor.putString(KEY_BOOK_ID, bookId);
        editor.commit();
    }

    public String getBookId() {
        return pref.getString(KEY_BOOK_ID, "");
    }

    public void setTickerPrice(String tickerPrice) {
        editor.putString(KEY_TICKET_PRICE, tickerPrice);
        editor.commit();
    }

    public String getTicketPrice() {
        return pref.getString(KEY_TICKET_PRICE, "");
    }

    public void setAeroComisi(String aeroComisi) {
        editor.putString(KEY_AERO_COMISI, aeroComisi);
        editor.commit();
    }

    public String getAeroComisi() {
        return pref.getString(KEY_AERO_COMISI, "");
    }

    public void setCabangComisi(String cabangComisi) {
        editor.putString(KEY_CABANG_COMISI, cabangComisi);
        editor.commit();
    }

    public String getCabangComisi() {
        return pref.getString(KEY_CABANG_COMISI, "");
    }

    public void setAgentComisi(String agentComisi) {
        editor.putString(KEY_AGENT_COMISI, agentComisi);
        editor.commit();
    }

    public String getAgentComisi() {
        return pref.getString(KEY_AGENT_COMISI, "");
    }

    public void setAgentPrice(String agentPrice) {
        editor.putString(KEY_AGENT_PRICE, agentPrice);
        editor.commit();
    }

    public String getAgentPrice() {
        return pref.getString(KEY_AGENT_PRICE, "");
    }

    public void setAgentMargin(String agentMargin) {
        editor.putString(KEY_AGENT_MARGIN, agentMargin);
        editor.commit();
    }

    public String getAgentMargin() {
        return pref.getString(KEY_AGENT_MARGIN, "");
    }

    public void setAsuransi(String asuransi) {
        editor.putString(KEY_ASURANSI, asuransi);
        editor.commit();
    }

    public String getAsuransi() {
        return pref.getString(KEY_ASURANSI, "");
    }

    public void setComisiAsuransi(String comisiAsuransi) {
        editor.putString(KEY_COMISI_ASURANSI, comisiAsuransi);
        editor.commit();
    }

    public String getComisiAsuransi() {
        return pref.getString(KEY_COMISI_ASURANSI, "");
    }

    public void setTotalPrice(String totalPrice) {
        editor.putString(KEY_TOTAL_PRICE, totalPrice);
        editor.commit();
    }

    public String getTotalPrice() {
        return pref.getString(KEY_TOTAL_PRICE, "");
    }

    public void setAdminFee(String adminFee) {
        editor.putString(KEY_ADMIN_FEE, adminFee);
        editor.commit();
    }

    public String getAdminFee() {
        return pref.getString(KEY_ADMIN_FEE, "");
    }

    // clear all data
    public void sessionSearchBookCleared() {
        editor.clear();
        editor.commit();
    }
}
