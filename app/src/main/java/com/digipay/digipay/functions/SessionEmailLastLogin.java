package com.digipay.digipay.functions;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Ridone on 5/11/2016.
 */
@SuppressLint("CommitPrefEdits")
public class SessionEmailLastLogin {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "Email";
    private static final String EMAIL_LAST_LOGIN = "email_last_login";

    // Constructor
    public SessionEmailLastLogin(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public String getEmailLastLogin() {
        return pref.getString(EMAIL_LAST_LOGIN, "");
    }

    public void setEmailLastLogin(String email) {
        editor.putString(EMAIL_LAST_LOGIN, email);
        editor.commit();
    }
}
