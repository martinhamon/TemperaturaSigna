package com.mcode.tempsigna;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Systema on 13/10/2016.
 */
public class configuracion {

    private final String SHARED_PREFS_FILE = "user";
    private final String KEY_EMAIL = "email";
    private final String KEY_PASS = "pass";

    private Context mContext;

    public configuracion(Context context) {
        mContext = context;
    }

    private SharedPreferences getSettings() {
        return mContext.getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
    }

    public String getUserPass() {
        return getSettings().getString(KEY_PASS, null);
    }

    public String getUserEmail() {
        return getSettings().getString(KEY_EMAIL, null);
    }

    public String getUserUser() {
        return getSettings().getString(SHARED_PREFS_FILE, null);
    }

    public void setUserEmail(String email) {
        SharedPreferences pref = mContext.getApplicationContext().getSharedPreferences("PREFERENCE", 1);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("email", email);
    }

    public void setUser(String user) {
        SharedPreferences pref = mContext.getApplicationContext().getSharedPreferences("PREFERENCE", 1);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("user", user);
    }

    public void setUserpass(String pass) {
        SharedPreferences pref = mContext.getApplicationContext().getSharedPreferences("PREFERENCE", 1);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("pass", pass);
    }
}
