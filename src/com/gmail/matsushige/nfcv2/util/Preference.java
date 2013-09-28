package com.gmail.matsushige.nfcv2.util;

import android.content.Context;
import android.content.SharedPreferences;

public class Preference {
    static Preference thePreference;

    public static Preference getTheInstance(Context context) {
        if (thePreference == null) {
            thePreference = new Preference(context);
        }
        return thePreference;
    }

    SharedPreferences sharedPreferences;

    private Preference(Context context) {
        sharedPreferences = context.getSharedPreferences("SNS_OUTLET", Context.MODE_PRIVATE);
    }//Preference (private constructor)

    public boolean getBoolean(String key, boolean default_value) {
        return this.sharedPreferences.getBoolean(key, default_value);
    }//getBoolean

    public void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }//putBoolean
}//Preference
