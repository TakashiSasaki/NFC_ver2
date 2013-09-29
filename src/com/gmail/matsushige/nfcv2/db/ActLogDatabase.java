package com.gmail.matsushige.nfcv2.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

public class ActLogDatabase {
    private static ActLogDatabase theInstance;

	private static String actLogText = "";

    public static ActLogDatabase getTheInstance(Context context){
        if (theInstance==null){
            theInstance = new ActLogDatabase(context);
            return theInstance;
        } else {
            return theInstance;
        }
    }

    public static ActLogDatabase getTheInstance(){
        return theInstance;
    }

    private SQLiteOpenHelper sqliteOpenHelper;

    private ActLogDatabase(Context context){
        sqliteOpenHelper = new ActLogDatabaseHelper(context);
    }

	public void write(Context context, String id, String user, String how, long time){
		ContentValues cv = new ContentValues();
		cv.put("ID", id);
		cv.put("user_name", user);
		cv.put("howAct", how);
		cv.put("timestamp", time);
		SQLiteDatabase db = sqliteOpenHelper.getReadableDatabase();
		db.insert("actlog", null, cv);
		db.close();
	}// write
	
	public String read(Context context){
		String actLogText = "";
		SQLiteDatabase db = sqliteOpenHelper.getReadableDatabase();
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
        return actLogText;
	}// read
}// ActLogDatabase
