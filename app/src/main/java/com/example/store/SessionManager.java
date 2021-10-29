package com.example.store;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;


public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    public SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "userLogin";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_PASSWORD = "password";

    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";

    public static final String KEY_LAT = "user_lat";

    public static final String KEY_LON = "user_lon";

    public static final String KEY_COMPANYID = "company_id";

    public static final String KEY_EMPLOYID = "employee_id";

    public static final String KEY_USERID = "user_id";

    public static final String KEY_BRANCHID = "branch_id";

    public static final String KEY_NS_COUNTER = "ns_counter";


    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public void createLoginSession(String user_id){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        // Storing user id
        editor.putString(KEY_USERID, user_id);

        // commit changes
        editor.commit();
    }

    /*** Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();

        // password
        user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));

        // user email
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        // company id
        user.put(KEY_COMPANYID, pref.getString(KEY_COMPANYID, null));

        // user location
        user.put(KEY_LAT, pref.getString(KEY_LAT, null));
        user.put(KEY_LON, pref.getString(KEY_LON, null));

        // employee id
        user.put(KEY_EMPLOYID, pref.getString(KEY_EMPLOYID, null));

        // user id
        user.put(KEY_USERID, pref.getString(KEY_USERID, null));

        // branch id
        user.put(KEY_BRANCHID, pref.getString(KEY_BRANCHID, null));

        user.put(KEY_NS_COUNTER,pref.getString(KEY_NS_COUNTER,null));

        // return user
        return user;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loging Activity
        Intent i = new Intent(_context, Login.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}
