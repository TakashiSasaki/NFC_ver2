package com.gmail.matsushige.nfcv2;

import java.util.Calendar;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;


public class CountTimeFirstUser extends IntentService{
	private static String TAG = "CountTimeFirstUser";
	private int count = 0;
	public long startTime = 0;
	public static boolean isUsed = false;
	public static boolean finish = false;
	public static int maxCount = 20; 
	public static boolean retainUserData = false;
	
	public CountTimeFirstUser() {
		super(TAG);
		// TODO Auto-generated constructor stub
	}// CountTime

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		Log.d(TAG, "serviceStart");
		isUsed = true;
		startTime = Calendar.getInstance().getTimeInMillis();
		
		for(count = 0; count < maxCount; count++){
			try {
				Thread.sleep(990);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}// catch
			if(finish){
				break;
			}else{
				Log.d(TAG, "test");

				Intent broadcastIntent = new Intent();
				broadcastIntent.putExtra("count", count + 1);
				broadcastIntent.setAction("TEST3_RECEIVE_ACTION");
				sendBroadcast(broadcastIntent);

				long thisTime = Calendar.getInstance().getTimeInMillis();
				Log.d(TAG, count +":"+(thisTime - startTime));
			}
		}// for
		Log.d(TAG, "serviceEnd");
		isUsed = false;
		finish = false;
		if(retainUserData){
			Log.d(TAG, "retainUserData");
			retainUserData = false;
		} else {
			resetUserPreference();
//			retainUserData = false;
		}
//		isUsed = false;
	}// onHandleIntent
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy");
	}// onDestroy
	
	private void resetUserPreference(){
		SharedPreferences sdf = getSharedPreferences("SNS_OUTLET", MODE_PRIVATE);
		Editor edit = sdf.edit();
		edit.putString("userType", "");
		edit.putString("userId", "");
		edit.commit();
	}// resetUserPreference

}
