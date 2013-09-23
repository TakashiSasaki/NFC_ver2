package com.gmail.matsushige.nfcv2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.AlarmManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TemporaryUsersDatabaseOperate {
	private static String TAG = "TemporaryUsersDatabaseOperate";
	public static String tempText = "";
	
	public static void write(Context context, String type, String id, String regCode){
		long regTime = Calendar.getInstance().getTimeInMillis();
		SQLiteDatabase db = (new TemporaryUsersDatabaseHelper(context)).getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("card_type", type);
		cv.put("card_id", id);
		cv.put("register_code", regCode);
		cv.put("register_time", regTime);
		cv.put("send_check", false);
		db.insert("tempusers", null, cv);
		db.close();
	}
	
	public static void read(Context context){
		tempText = "";
		SQLiteDatabase db = (new TemporaryUsersDatabaseHelper(context))
				.getReadableDatabase();
		Cursor c = db.query("tempusers", null, null, null, null, null, null);
		c.moveToFirst();
		for (int i = 0; i < c.getCount(); ++i) {
			long serial = c.getLong(c.getColumnIndex("serial"));
			String type = c.getString(c.getColumnIndex("card_type"));
			String id = c.getString(c.getColumnIndex("card_id"));
			String register_code = c.getString(c.getColumnIndex("register_code"));
			long register_time = c.getLong(c.getColumnIndex("register_time"));
			String regTime = (new Date(register_time)).toLocaleString();
			String send_check = c.getString(c.getColumnIndex("send_check"));
			
			tempText += serial + " " + type + " " + id + " " + register_code + " " + regTime + " " + send_check + "\n";
			c.moveToNext();
		}// for
		db.close();
	}
	
	public static void deleteOldData(Context context){
		long currentTime = Calendar.getInstance().getTimeInMillis();
		SQLiteDatabase db = (new TemporaryUsersDatabaseHelper(context)).getReadableDatabase();
		String[] key = {"1"}; // send_checkÇ™"1"Ç≈Ç†ÇÈÇ© 
		Cursor cursor = db.query("tempusers", null, "send_check = ?", key, null, null, null);
		cursor.moveToFirst();
		for(int i = 0; i < cursor.getCount(); i++){
			long serial = cursor.getLong(cursor.getColumnIndex("serial"));
			long register_time = cursor.getLong(cursor.getColumnIndex("register_time"));
			long difference = currentTime - register_time;
			if(difference > AlarmManager.INTERVAL_FIFTEEN_MINUTES){
				String[] target = {"" + serial}; // äYìñÇ∑ÇÈí Çµî‘çÜÇëŒè€Ç…éwíË
				db.delete("tempusers", "serial = ?", target);
				Log.d(TAG, "DELETE");
			}else{
				Log.d(TAG, "NOT_DELETE");
			}// else
			cursor.moveToNext();
		}// for
		cursor.close();
		db.close();
	}// deleteOldData
	
	public static void checkRegisteredData(Context context, String type, String id){
		Nfc_simple.cardOwner = "";
		SQLiteDatabase db = (new TemporaryUsersDatabaseHelper(context)).getReadableDatabase();
		String where = "card_type = ?";
		String[] where_arg = {type};
		Cursor cursor = db.query("tempusers", null, where, where_arg, null, null, null);
		while(cursor.moveToNext()){
			String registeredId = cursor.getString(cursor.getColumnIndex("card_id"));
			if(id.equals(registeredId)){
				String serial = cursor.getString(cursor.getColumnIndex("serial"));
				String regcode = cursor.getString(cursor.getColumnIndex("register_code"));
				Nfc_simple.cardOwner = "ÉQÉXÉg" + serial;
				Nfc_simple.regCode = regcode;
				break;
			}// if
		}// while
		cursor.close();
		db.close();
	}// checkRegisteredData
	
	public static String getRegisterCode(Context context){
		String registerCode = "";
		SQLiteDatabase db = (new TemporaryUsersDatabaseHelper(context)).getReadableDatabase();
		db.close();
		return registerCode;
	}// getRegisterCode
}
