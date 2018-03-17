package com.example.awesome.opticsdisplay.Util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by awesomeness on 3/6/18.
 */

public class Session {
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    public static final String LOGGED_IN_MODE = "loggedInMode";
    public static final String LOGIN_PREF = "loginPref";
    
    
    public Session(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(LOGIN_PREF, Context
                .MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
    
    public void setUserLoggedIn(boolean loggedIn) {
        editor.putBoolean(LOGGED_IN_MODE, loggedIn);
        editor.commit();
    }
    
    public boolean checkIfUserIsLoggedIn() {
        return sharedPreferences.getBoolean(LOGGED_IN_MODE, false);
    }
    
}
