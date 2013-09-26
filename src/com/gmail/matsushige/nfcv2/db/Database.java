package com.gmail.matsushige.nfcv2.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Database {
	public static String logText = "";

	public static void write(Context context,String type, String id, long timestamp){
		ContentValues cv = new ContentValues();
		cv.put("type", type );
		cv.put("ID", id);
		cv.put("timestamp", timestamp);
		SQLiteDatabase db = (new DatabaseHelper(context)).getReadableDatabase();
		db.insert("touch", null, cv);
		db.close();
	}// write
	
	public static void read(Context context){
		logText = "";
		SQLiteDatabase db = (new DatabaseHelper(context)).getReadableDatabase();
		Cursor c = db.query("touch", null, null, null, null, null, null);
		c.moveToFirst();
		for (int i = 0; i < c.getCount(); ++i) {
			long _id = c.getLong(c.getColumnIndex("_id"));
			String type = c.getString(c.getColumnIndex("type"));
			String id = c.getString(c.getColumnIndex("ID"));
			long timestamp = c.getLong(c.getColumnIndex("timestamp"));
			logText += _id + " " + type + " " + id + " " + timestamp + "\n";
			c.moveToNext();
		}// for
		db.close();
	}// read
}// UsersDatabase
