package com.gmail.matsushige.nfcv2.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gmail.matsushige.nfcv2.Nfc_simple;
import com.gmail.matsushige.nfcv2.UsersDatabaseHelper;

public class UsersDatabase {
	public static String usersText = "";

//	public static void write(Context context, String type, String id,
//			String user_name) {
//		ContentValues cv = new ContentValues();
//		cv.put("ic_type", type);
//		cv.put("ic_id", id);
//		cv.put("user_name", user_name);
//		SQLiteDatabase db = (new UsersDatabaseHelper(context))
//				.getReadableDatabase();
//		db.insert("users", null, cv);
//		db.close();
//	}// write
	
	public static void write(Context context, String serial, String icType, String icId, String odenkiId, String userName, String registTime, String expireTime ){
		ContentValues cv = new ContentValues();
		cv.put("serial", serial);
		cv.put("ic_type",icType);
		cv.put("ic_id", icId);
		cv.put("odenki_id", odenkiId);
		cv.put("user_name", userName);
		cv.put("regist_time", registTime);
		cv.put("expire_time",expireTime);
		SQLiteDatabase db = (new UsersDatabaseHelper(context)).getWritableDatabase();
		db.insert("users", null, cv);
		db.close();
	}

	public static void read(Context context) {
		usersText = "";
		SQLiteDatabase db = (new UsersDatabaseHelper(context))
				.getReadableDatabase();
		Cursor c = db.query("users", null, null, null, null, null, null);
		c.moveToFirst();
		for (int i = 0; i < c.getCount(); ++i) {
			String _id = c.getString(c.getColumnIndex("serial"));
			String type = c.getString(c.getColumnIndex("ic_type"));
			String id = c.getString(c.getColumnIndex("ic_id"));
			String odenkiId = c.getString(c.getColumnIndex("odenki_id"));
			String user_name = c.getString(c.getColumnIndex("user_name"));
			String regist_time = c.getString(c.getColumnIndex("regist_time"));
			String expire_time = c.getString(c.getColumnIndex("expire_time"));
			usersText += _id + " " + type + " " + id + " " + odenkiId + " " +user_name + " " + regist_time + " " + expire_time + "\n";
			c.moveToNext();
		}// for
		db.close();
	}// read

	public static void checkResist(Context context, String type, String ID) {
		SQLiteDatabase users = (new UsersDatabaseHelper(context))
				.getReadableDatabase();
		String where = "ic_type = ?";
		String[] where_arg = { type };
		/** 一致するtypeを確認 */
		Cursor cursor = users.query("users", null, where, where_arg, null,
				null, null);
		Nfc_simple.cardOwner = "";
		while (cursor.moveToNext()) {
			String idre = cursor.getString(cursor.getColumnIndex("ic_id"));
			/** 一致するidを確認 */
			if (ID.equals(idre)) {
				Nfc_simple.cardOwner = cursor.getString(cursor.getColumnIndex("user_name"));
				break;
			}// if
		}// while
		users.close();
	}// checkResist
	
//	public static void deleteRecord(Context context, String type, String idD){
//		SQLiteDatabase db = (new UsersDatabaseHelper(context))
//				.getWritableDatabase();
//		String where = "ic_type = ?";
//		String[] where_arg = { type };
//		Cursor cursor = db.query("users", null, where, where_arg, null, null,
//				null);
//		String serialN = "";
//		while (cursor.moveToNext()) {
//			String idre = cursor.getString(cursor.getColumnIndex("ic_id"));
//			if (idD.equals(idre)) {
//				/** 一致するidを確認 */
//				serialN = cursor.getString(cursor.getColumnIndex("serial"));
//			}// if
//		}// while
//		String[] target = { serialN };
//		db.delete("users", "serial = ?", target);
//		db.close();
//	}// deleteRecord
	
	public static void deleteAllRecord(Context context){
		SQLiteDatabase db = (new UsersDatabaseHelper(context)).getWritableDatabase();
		db.delete("users", null, null);
		db.close();
	}// deleteAllRecord

}// UsersDatabase
