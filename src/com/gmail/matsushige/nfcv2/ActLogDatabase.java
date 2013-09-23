package com.gmail.matsushige.nfcv2;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ActLogDatabase {
	public static String actLogText = "";

	public static void write(Context context, String id, String user, String how, long time){
		ContentValues cv = new ContentValues();
		cv.put("ID", id);
		cv.put("user_name", user);
		cv.put("howAct", how);
		cv.put("timestamp", time);
		SQLiteDatabase db = (new ActLogDatabaseHelper(context)).getReadableDatabase();
		db.insert("actlog", null, cv);
		db.close();
	}// write
	
	public static void read(Context context){
		actLogText = "";
		SQLiteDatabase db = (new ActLogDatabaseHelper(context)).getReadableDatabase();
		Cursor c = db.query("actlog", null, null, null, null, null, null);
		c.moveToFirst();
		for (int i = 0; i < c.getCount(); ++i) {
			long _id = c.getLong(c.getColumnIndex("_id"));
			String id = c.getString(c.getColumnIndex("ID"));
			String user_name = c.getString(c.getColumnIndex("user_name"));
			String howAct = c.getString(c.getColumnIndex("howAct"));
			long timestamp = c.getLong(c.getColumnIndex("timestamp"));
			String timestamp_string = (new Date(timestamp)).toLocaleString();
			actLogText += _id + " " + id + " " + user_name + " " + howAct + " " + timestamp_string + "\n";
			c.moveToNext();
		}// for
		db.close();
	}// read
}// ActLogDatabase
