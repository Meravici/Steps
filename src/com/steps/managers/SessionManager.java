package com.steps.managers;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.steps.activities.LoginActivity;
 
public class SessionManager {
    // Shared Preferences
    private SharedPreferences pref;
     
    // Context
    private Context context;
     
    // Shared pref mode
    int PRIVATE_MODE = 0;
     
    // Sharedpref file name
    private static final String PREF_NAME = "StepsPrefs";
     
    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
     
    // User name (make variable public to access from outside)
    public static final String KEY_ID = "id";
     
    // Constructor
    public SessionManager(Context context){
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
    }
     
    /**
     * Create login session
     * */
    public void loginUser(int id){
        Editor editor = pref.edit();
    	// Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
         
        // Storing name in pref
        editor.putInt(KEY_ID, id);
         
        // commit changes
        editor.commit();
    }     
     
    /**
     * Get stored session data
     * */
    public int getUserId(){
        // user id
        return pref.getInt(KEY_ID, -1);
    }
     
    /**
     * Clear session details
     * */
    public void logoutUser(){
        Editor editor = pref.edit();
    	// Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
         
        // After logout redirect user to Login Activity
        Intent i = new Intent(context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
         
        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
         
        // Staring Login Activity
        context.startActivity(i);
    }
     
    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}