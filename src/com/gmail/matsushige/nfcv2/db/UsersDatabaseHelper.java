package com.gmail.matsushige.nfcv2.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 本登録ユーザーデータベース 要変更>Nfc_simple.java,UsersDatabase.java
 */
public class UsersDatabaseHelper extends SQLiteOpenHelper {

    public UsersDatabaseHelper(Context context) {
        super(context, "NFC.user.sqlite", null, 2);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL("CREATE TABLE users(serial INTEGER PRIMARY KEY AUTOINCREMENT, ic_type TEXT,"
                + " ic_id TEXT, odenki_id TEXT, user_name TEXT, regist_time TEXT, expire_time TEXT);");

        ContentValues cv = new ContentValues();
        /** serialは1から順に割り当てた */

        //cv.put("serial", 1);
        cv.put("ic_type", "f");
        cv.put("ic_id", "0115E5005B0BB104");
        cv.put("user_name", "学生証");
        db.insert("users", null, cv);

        //cv.put("serial", 2);
        cv.put("ic_type", "b");
        cv.put("ic_id", "91D3B724");
        cv.put("user_name", "免許証");
        db.insert("users", null, cv);

        //cv.put("serial", 3);
        cv.put("ic_type", "a");
        cv.put("ic_id", "8532D18C");
        cv.put("user_name", "taspo");
        db.insert("users", null, cv);

        //cv.put("serial", 4);
        cv.put("ic_type", "f");
        cv.put("ic_id", "01130200F10C3B01");
        cv.put("user_name", "都築 伸二");
        db.insert("users", null, cv);

    }//onCreate

    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
        // TODO Auto-generated method stub
        db.execSQL("drop users if exists users");
    }//onUpgrade

}//UsersDatabaseHelper
