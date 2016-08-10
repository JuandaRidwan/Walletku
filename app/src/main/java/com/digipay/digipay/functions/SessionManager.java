package com.digipay.digipay.functions;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.digipay.digipay.activity.LoginActivity;

@SuppressLint("CommitPrefEdits")
public class SessionManager {
    SharedPreferences pref;
    Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "Walletku";
    private static final String IS_LOGIN = "IsLoggedIn";
    // name variable select user
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_UID = "uid";
    public static final String KEY_AUTH_TOKEN = "auth_token";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_NO_KTP = "no_ktp";
    public static final String KEY_NO_HP = "no_hp";
    public static final String KEY_ALAMAT = "alamat";
    public static final String KEY_PREMIUM = "premium";
    public static final String KEY_AVATAR = "avatar";
    public static final String KEY_REFERAL_ID = "referal_id";
    public static final String KEY_TOTAL_BALANCE = "total_balance";
    public static final String KEY_TOTAL_BONUS = "total_bonus";
    public static final String KEY_TOTAL_POINT = "total_point";
    public static final String KEY_PRICE = "price";
    public static final String KEY_AIRLINES_ID = "airlines_id";
    public static final String KEY_DEPARTURE = "departure";
    public static final String KEY_ARRIVAL = "arrival";
    public static final String KEY_SESSION_ID = "session_id";
    public static final String KEY_SESSION_ID_HOTEL = "session_id_hotel";
    public static final String KEY_SESSION_ID_HOTEL_DETAIL = "session_id_hotel_detail";
    public static final String KEY_SESSION_ID_HOTEL_BOOK = "session_id_hotel_book";
    public static final String KEY_HOTEL_ID = "hotel_id";
    public static final String KEY_HOTEL_NAME = "hotel_name";
    public static final String KEY_ROOM_CODE = "room_code";
    public static final String KEY_ADMIN_FEE_HOTEL = "admin_fee_hotel";
    public static final String KEY_ROOM_CODE_DETAIL = "room_code_detail";
    public static final String KEY_HOTEL_IMG_LARGE = "hotel_img_large";
    public static final String KEY_SESSION_ID_SEARCH_DETAIL = "session_id_search_detail";
    public static final String KEY_JML_PENUMPANG = "jml_penumpang";
    public static final String KEY_FNO_1 = "fno_1";
    public static final String KEY_FNO_2 = "fno_2";
    public static final String KEY_CLASS_1 = "class_1";
    public static final String KEY_CLASS_2 = "class_2";

    // hotel
    public static final String KEY_BASIC_PRICE = "basic_price";
    public static final String KEY_FEE_N_TAX = "fee_and_tax";
    public static final String KEY_TOTAL_PRICE = "total_price";
    public static final String KEY_COMISSION = "comission";

    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setUserId(String user_id) {
        editor.putString(KEY_USER_ID, user_id);
        editor.commit();
    }

    public String getUserId() {
        return pref.getString(KEY_USER_ID, "");
    }

    public void setUId(String uid) {
        editor.putString(KEY_UID, uid);
        editor.commit();
    }

    public String getUId() {
        return pref.getString(KEY_UID, "");
    }

    public void setToken(String token) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_AUTH_TOKEN, token);
        editor.commit();
    }

    public String getToken() {
        return pref.getString(KEY_AUTH_TOKEN, "");
    }

    public String getName() {
        return pref.getString(KEY_NAME, "");
    }

    public void setName(String name) {
        editor.putString(KEY_NAME, name);
        editor.commit();
    }

    public String getEmail() {
        return pref.getString(KEY_EMAIL, "");
    }

    public void setEmail(String email) {
        editor.putString(KEY_EMAIL, email);
        editor.commit();
    }

    public String getNoKtp() {
        return pref.getString(KEY_NO_KTP, "");
    }

    public void setNoKtp(String noKtp) {
        editor.putString(KEY_NO_KTP, noKtp);
        editor.commit();
    }

    public String getNoHp() {
        return pref.getString(KEY_NO_HP, "");
    }

    public void setNoHp(String noHp) {
        editor.putString(KEY_NO_HP, noHp);
        editor.commit();
    }

    public String getAlamat() {
        return pref.getString(KEY_ALAMAT, "");
    }

    public void setAlamat(String alamat) {
        editor.putString(KEY_ALAMAT, alamat);
        editor.commit();
    }

    public String getPremium() {
        return pref.getString(KEY_PREMIUM, "");
    }

    public void setPremium(String premium) {
        editor.putString(KEY_PREMIUM, premium);
        editor.commit();
    }

    public String getAvatar() {
        return pref.getString(KEY_AVATAR, "");
    }

    public void setAvatar(String avatar) {
        editor.putString(KEY_AVATAR, avatar);
        editor.commit();
    }

    public String getReferalId() {
        return pref.getString(KEY_REFERAL_ID, "");
    }

    public void setReferalId(String referalId) {
        editor.putString(KEY_REFERAL_ID, referalId);
        editor.commit();
    }

    public String getTotalPoint() {
        return pref.getString(KEY_TOTAL_POINT, "");
    }

    public void setTotalPoint(String totalPoint) {
        editor.putString(KEY_TOTAL_POINT, totalPoint);
        editor.commit();
    }

    public String getPrice() {
        return pref.getString(KEY_PRICE, "");
    }

    public void setPrice(String price) {
        editor.putString(KEY_PRICE, price);
        editor.commit();
    }

    public String getAirlinesId() {
        return pref.getString(KEY_AIRLINES_ID, "");
    }

    public void setAirlinesId(String airlinesId) {
        editor.putString(KEY_AIRLINES_ID, airlinesId);
        editor.commit();
    }

    public String getDeparture() {
        return pref.getString(KEY_DEPARTURE, "");
    }

    public void setDeparture(String departure) {
        editor.putString(KEY_DEPARTURE, departure);
        editor.commit();
    }

    public String getArrival() {
        return pref.getString(KEY_ARRIVAL, "");
    }

    public void setArrival(String arrival) {
        editor.putString(KEY_ARRIVAL, arrival);
        editor.commit();
    }

    public String getSessionId() {
        return pref.getString(KEY_SESSION_ID, "");
    }

    public void setSessionId(String sessionId) {
        editor.putString(KEY_SESSION_ID, sessionId);
        editor.commit();
    }

    public String getSessionIdHotel() {
        return pref.getString(KEY_SESSION_ID_HOTEL, "");
    }

    public void setSessionIdHotel(String sessionIdHotel) {
        editor.putString(KEY_SESSION_ID_HOTEL, sessionIdHotel);
        editor.commit();
    }

    public String getSessionIdHotelDetail() {
        return pref.getString(KEY_SESSION_ID_HOTEL_DETAIL, "");
    }

    public void setSessionIdHotelDetail(String sessionIdHotelDetail) {
        editor.putString(KEY_SESSION_ID_HOTEL_DETAIL, sessionIdHotelDetail);
        editor.commit();
    }

    public String getSessionIdHotelBook() {
        return pref.getString(KEY_SESSION_ID_HOTEL_BOOK, "");
    }

    public void setSessionIdHotelBook(String sessionIdHotelBook) {
        editor.putString(KEY_SESSION_ID_HOTEL_BOOK, sessionIdHotelBook);
        editor.commit();
    }

    public String getHotelId() {
        return pref.getString(KEY_HOTEL_ID, "");
    }

    public void setHotelId(String hotelId) {
        editor.putString(KEY_HOTEL_ID, hotelId);
        editor.commit();
    }

    public String getHotelName() {
        return pref.getString(KEY_HOTEL_NAME, "");
    }

    public void setHotelName(String hotelName) {
        editor.putString(KEY_HOTEL_NAME, hotelName);
        editor.commit();
    }

    public String getRoomCode() {
        return pref.getString(KEY_ROOM_CODE, "");
    }

    public void setRoomCode(String roomCode) {
        editor.putString(KEY_ROOM_CODE, roomCode);
        editor.commit();
    }

    public String getAdminFeeHotel() {
        return pref.getString(KEY_ADMIN_FEE_HOTEL, "");
    }

    public void setAdminFeeHotel(String adminFeeHotel) {
        editor.putString(KEY_ADMIN_FEE_HOTEL, adminFeeHotel);
        editor.commit();
    }

    public String getRoomCodeDetail() {
        return pref.getString(KEY_ROOM_CODE_DETAIL, "");
    }

    public void setRoomCodeDetail(String roomCodeDetail) {
        editor.putString(KEY_ROOM_CODE_DETAIL, roomCodeDetail);
        editor.commit();
    }

    public String getHotelImgLarge() {
        return pref.getString(KEY_HOTEL_IMG_LARGE, "");
    }

    public void setHotelImgLarge(String hotelImgLarge) {
        editor.putString(KEY_HOTEL_IMG_LARGE, hotelImgLarge);
        editor.commit();
    }

    public String getSessionIdSearchDetail() {
        return pref.getString(KEY_SESSION_ID_SEARCH_DETAIL, "");
    }

    public void setSessionIdSearchDetail(String sessionId) {
        editor.putString(KEY_SESSION_ID_SEARCH_DETAIL, sessionId);
        editor.commit();
    }

    public String getJmlPenumpang() {
        return pref.getString(KEY_JML_PENUMPANG, "");
    }

    public void setJmlPenumpang(String jmlPenumpang) {
        editor.putString(KEY_JML_PENUMPANG, jmlPenumpang);
        editor.commit();
    }

    public String getFno1() {
        return pref.getString(KEY_FNO_1, "");
    }

    public void setFno1(String fno1) {
        editor.putString(KEY_FNO_1, fno1);
        editor.commit();
    }

    public String getFno2() {
        return pref.getString(KEY_FNO_2, "");
    }

    public void setFno2(String fno2) {
        editor.putString(KEY_FNO_2, fno2);
        editor.commit();
    }

    public String getClass1() {
        return pref.getString(KEY_CLASS_1, "");
    }

    public void setClass1(String class1) {
        editor.putString(KEY_CLASS_1, class1);
        editor.commit();
    }

    public String getClass2() {
        return pref.getString(KEY_CLASS_2, "");
    }

    public void setClass2(String class2) {
        editor.putString(KEY_CLASS_2, class2);
        editor.commit();
    }

    public String getBasicPrice() {
        return pref.getString(KEY_BASIC_PRICE, "");
    }

    public void setBasicPrice(String basicPrice) {
        editor.putString(KEY_BASIC_PRICE, basicPrice);
        editor.commit();
    }

    public String getFeeNTax() {
        return pref.getString(KEY_FEE_N_TAX, "");
    }

    public void setFeeNTax(String feeNTax) {
        editor.putString(KEY_FEE_N_TAX, feeNTax);
        editor.commit();
    }

    public String getTotalPrice() {
        return pref.getString(KEY_TOTAL_PRICE, "");
    }

    public void setTotalPrice(String totalPrice) {
        editor.putString(KEY_TOTAL_PRICE, totalPrice);
        editor.commit();
    }

    public String getComission() {
        return pref.getString(KEY_COMISSION, "");
    }

    public void setComission(String Comission) {
        editor.putString(KEY_COMISSION, Comission);
        editor.commit();
    }

    public String getTotalBalance() {
        return pref.getString(KEY_TOTAL_BALANCE, "");
    }

    public void setTotalBalance(String totalBalance) {
        editor.putString(KEY_TOTAL_BALANCE, totalBalance);
        editor.commit();
    }

    public String getTotalBonus() {
        return pref.getString(KEY_REFERAL_ID, "");
    }

    public void setTotalBonus(String totalBonus) {
        editor.putString(KEY_REFERAL_ID, totalBonus);
        editor.commit();
    }

    /**
     * Check login method will check user login status If false it will redirect
     * user to login page Else won't do anything
     */
    public void checkLogin() {
        // Check login status
        if (!this.isLogin()) {
            Intent i = new Intent(_context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            _context.startActivity(i);
        }

    }

    public void logout() {
        editor.clear();
        editor.commit();

        Intent i = new Intent(_context, LoginActivity.class);
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        _context.startActivity(i);
    }

    public void clearSharedPrevByKey(String key) {
        editor.remove(key);
        editor.apply();
    }

    public boolean isLogin() {
        return pref.getBoolean(IS_LOGIN, false);
    }
}
