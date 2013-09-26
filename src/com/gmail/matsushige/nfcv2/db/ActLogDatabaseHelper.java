package com.gmail.matsushige.nfcv2.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class ActLogDatabaseHelper extends SQLiteOpenHelper {

	public ActLogDatabaseHelper(Context context) {
		super(context, "NFC.act.sqlite", null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE actlog(" +
				"_id INTEGER PRIMARY KEY AUTOINCREMENT," +
				"ID TEXT, user_name TEXT, howAct TEXT, timestamp INTEGER)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
