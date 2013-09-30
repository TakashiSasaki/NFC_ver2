package com.gmail.matsushige.nfcv2.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

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
        /** outletId（プリファレンス保存）を"AB"に変更 */
        String outletId = this.getOutletId();
        if (outletId == "AA") {
            this.setOutletId("AB");
        } else {
            Log.d("Activity", "second~_pref");
        }// else
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
    }//getOutletId

    public void setOutletId(String outlet_id) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putString("outletId", outlet_id);
        editor.commit();
    }//setOutletId

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
        edit.putString("cardOwner", null);
        edit.commit();
    }// resetPreference

    public String getCardOwner() {
        return this.sharedPreferences.getString("cardOwner", null);
    }//getCardOwner

    public void setCardOwner(String card_owner) {
        if ("".equals(card_owner))
            throw new IllegalArgumentException("card owner should not be empty string");
        SharedPreferences.Editor edit = this.sharedPreferences.edit();
        edit.putString("cardOwner", card_owner);
        edit.commit();
    }//setCardOwner

    public void setRegistrationCode(String registration_code) {
        if ("".equals(registration_code))
            throw new IllegalArgumentException("registration code shold not be empty string");
        SharedPreferences.Editor edit = this.sharedPreferences.edit();
        edit.putString("registrationCode", registration_code);
        edit.commit();
    }//setRegistrationCode

    public String getRegistrationCode() {
        return this.sharedPreferences.getString("registrationCode", null);
    }//getRegistrationCode
}//Preference
