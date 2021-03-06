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

    public String getUserType() {
        String user_type = this.sharedPreferences.getString("userType", null);
        if (user_type != null) assert (!user_type.equals(""));
        return user_type;
    }//getUserType

    public String getUserId() {
        String user_id = this.sharedPreferences.getString("userId", null);
        if (user_id != null) assert (!user_id.equals(""));
        return user_id;
    }//getUserId

    public String getOutletId() {
        return this.sharedPreferences.getString("outletId", "AA");
    }

    public void setOutletId(String outlet_id) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putString("outletId", outlet_id);
        editor.commit();
    }

    public void setTypeAndId(String type, String id) {
        if ("".equals(type))
            throw new IllegalArgumentException("card type should not be empty string");
        if ("".equals(id)) throw new IllegalArgumentException("card id should not be empty string");
        SharedPreferences.Editor edit = this.sharedPreferences.edit();
        edit.putString("userType", type);
        edit.putString("userId", id);
        edit.commit();
    }//setTypeAndId

    public void resetPreference() {
        SharedPreferences.Editor edit = this.sharedPreferences.edit();
        edit.putString("userType", null);
        edit.putString("userId", null);
        edit.commit();
    }// resetPreference

}//Preference
