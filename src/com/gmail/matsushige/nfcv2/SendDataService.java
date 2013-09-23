package com.gmail.matsushige.nfcv2;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SendDataService extends IntentService{
	HttpsURLConnection httpsConnect = null;
	public static String readData = "";
	public static String serial = "";
	public static String type = "";
	public static String id = "";
	public static String regCode = "";
	private final static String SIGNATURE = "Hyb6XmbN";
	
	private static boolean isUsed = false;
	
	public SendDataService() {
		super("sendDataService");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		isUsed = true;
		while (true) {
			String connect;
			readDatabase(getApplicationContext());
			if (readData != "") {
				/** sendToMyServer */
				connect = httpConnect(id, type);
				if (connect == "OK") {
					writeDatabase(getApplicationContext(), serial);
				}// if
			} else {
				Log.d("Service", "ALL_1");
				break;
			}// else
			try {
				Thread.sleep(66000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e("Service", e.toString());
			}// catch
		}// while
		isUsed = false;
		Log.d("Service", "END_SERVICE");
	}// onHandleIntent
	
	public String httpConnect(String idt, String typet) {
		String returnValue = "";
		try {
			SharedPreferences pref = getSharedPreferences("SNS_OUTLET", MODE_PRIVATE);
			String outletId = pref.getString("outletId", "AA");
			
			String baseUrl = "https://odenkiapi.appspot.com/api/Card";
			/** 送信パラメータの取得 */
			String outletIdParameter = "outletId=" + outletId;
			String regCodeParameter = "regCode=" + regCode;
			String cardIdParameter = "cardId=" + id;
			String cardTypeParameter = "cardType=" + type;
			String signatureParameter = "signature=" + SIGNATURE;
			
			URL url = new URL(baseUrl + "?" + outletIdParameter + "&" + regCodeParameter + "&" + cardIdParameter + "&" + cardTypeParameter + "&" + signatureParameter);
			URLConnection connect = url.openConnection();
			httpsConnect = (HttpsURLConnection) connect;
			httpsConnect.connect();
			int response = httpsConnect.getResponseCode();
			/** OK(200)であるか判定 */
			if (response == HttpURLConnection.HTTP_OK) {
				returnValue = "OK";
				Log.d("Service", "HTTP : OK (" + response + ")");
			} else {
				returnValue = "ERROR";
				Log.d("Service", "HTTP : NOT_OK (" + response + ")");
			}// else
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("Service", e.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("Service", e.toString());
		} finally {
			httpsConnect.disconnect();
		}
		return returnValue;
	}// httpConnect
	
	/** 送信のためのデータベース読み出し(send_checkが0の行を1つ取り出す) */
	public void readDatabase(Context context){
		readData = "";
		SQLiteDatabase db = (new TemporaryUsersDatabaseHelper(context)).getReadableDatabase();
		/** 未送信のデータをCursorで取得 */
		String selection = "send_check = ?";
		String[] selectionArg = {"0"}; 
		Cursor c = db.query("tempusers", null, selection, selectionArg, null, null, null);
		if (c.getCount() > 0) {
			c.moveToFirst();
			serial = c.getString(c.getColumnIndex("serial"));
			type = c.getString(c.getColumnIndex("card_type"));
			id = c.getString(c.getColumnIndex("card_id"));
			regCode = c.getString(c.getColumnIndex("register_code"));
			readData = serial + "," + type + "," + id + "," + regCode;
			Log.d("Activity", "readData:" + readData);
		} else {
			Log.d("Activity", "NO_DATA");
		}// else
		db.close();
	}// readDatabase
	
	/** "send_check"カラムの書き換え */
	public void writeDatabase(Context context, String serial){
		SQLiteDatabase db = (new TemporaryUsersDatabaseHelper(context)).getWritableDatabase();
		String[] target = {serial};
		ContentValues cv = new ContentValues();
		cv.put("send_check", true);
		db.update("tempusers", cv, "serial = ?", target);
		db.close();
	}// writeDatabase
	
	public static boolean getIsUsed(){
		return isUsed;
	}
	

}
