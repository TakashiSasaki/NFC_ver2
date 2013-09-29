package com.gmail.matsushige.nfcv2.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class TouchLogDatabaseHelper extends SQLiteOpenHelper {

    public TouchLogDatabaseHelper(Context context) {
        super(context, "NFC.log.sqlite", null, 1);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL("CREATE TABLE touch("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "type TEXT, ID TEXT, timestamp INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub

    }
}
