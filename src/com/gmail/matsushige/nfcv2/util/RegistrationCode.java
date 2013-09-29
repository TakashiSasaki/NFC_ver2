package com.gmail.matsushige.nfcv2.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.gmail.matsushige.nfcv2.db.TemporaryUsersDatabaseHelper;

public class RegistrationCode {
    Context context;

    private RegistrationCode(Context context) {
        this.context = context;
    }//RegistrationCode

    static RegistrationCode theInstance;

    static public RegistrationCode getTheInstance(Context context) {
        if (theInstance == null) {
            theInstance = new RegistrationCode(context);
        }
        return theInstance;
    }//getTheInstance

    /**
     * ユーザ登録番号作成手順
     * 　1.プリファレンスに保存されたコンセントIDを取得し、英字部分とする
     * 　2.仮登録ユーザデータベースの"register_code"から最後に発行された数字を取得する
     * 　3.取得した数字に1足した値をユーザ登録番号の数字部分とする
     * 　4.英字部分と数字部分を組み合わせる
     */
    public String GenerateRegisterCode() {
        String outletCode = Preference.getTheInstance(this.context).getOutletId();
        String preNumber = getPreviousNumber(outletCode);
        String numberCode = generateNumberCode(preNumber);
        String registerCode = outletCode + numberCode;
        Log.d("AService", registerCode);
        return registerCode;
    }

    /**
     * 仮登録データベースから最後に発行された数字を取得
     */
    private String getPreviousNumber(String outletCode) {
        String preNumber = "";
        SQLiteDatabase db = (new TemporaryUsersDatabaseHelper(this.context)).getReadableDatabase();
        /** "temporary_users"の"register_time"列のデータを降順にし、Cursorを取得 */
        String[] columns = {"register_code", "register_time"};
        Cursor c = db.query("tempusers", columns, null, null, null, null, "register_time desc");
        if (c.getCount() > 0) {
            c.moveToFirst();
            String regCode = c.getString(c.getColumnIndex("register_code"));
            preNumber = regCode.replace(outletCode, "");
            Log.d("AService", "getRegCode:" + preNumber);
        }
        db.close();
        if (preNumber.equals("")) {
            preNumber = "9999";
            Log.d("AService", "reset");
        }
        return preNumber;
    }// getPreviousNumber

    /**
     * 数字部分の作成
     */
    private String generateNumberCode(String preNumber) {
        int intNumber = 0;
        String numberCode = "";
        /** String → int変換 */
        try {
            int intPreNumber = Integer.parseInt(preNumber);
            if (intPreNumber > 9998) {
                intNumber = 0;
            } else {
                intNumber = intPreNumber + 1;
            }// else
            numberCode = adjust4digit(intNumber);
        } catch (Exception e) {
            Log.e("AService", e.toString());
        }
        return numberCode;
    }// generateNuberCode

    /**
     * 数字を4桁にする
     */
    private String adjust4digit(int rawData) {
        String newData = "";
        if (rawData < 10) {
            newData = "000" + rawData;
        } else if (rawData < 100) {
            newData = "00" + rawData;
        } else if (rawData < 1000) {
            newData = "0" + rawData;
        } else {
            newData = "" + rawData;
        }// else
        return newData;
    }// adjust4digit

}//RegistrationCode
