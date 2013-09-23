package com.gmail.matsushige.nfcv2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/** 仮登録ユーザーデータベース  */
public class TemporaryUsersDatabaseHelper extends SQLiteOpenHelper {

	public TemporaryUsersDatabaseHelper(Context context) {
		super(context, "NFC.temp.sqlite", null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE tempusers(serial INTEGER PRIMARY KEY AUTOINCREMENT, card_type TEXT,"
		+" card_id TEXT, register_code TEXT, register_time TEXT, send_check TEXT);");
		
//		ContentValues cv = new ContentValues();
//		for(int i = 0; i < 999; i++){
//			String number = Integer.toString(i);
//			if(i < 10){
//				number = "000" + number;
//			}else if(i < 100){
//				number = "00" + number;
//			}else if(i < 1000){
//				number = "0" + number;
//			}
//			cv.put("card_id", "ABC345");
//			cv.put("register_code", "AA" + number);
//			db.insert("tempusers", null, cv);
//		}// for

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
