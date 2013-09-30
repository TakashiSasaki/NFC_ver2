package com.gmail.matsushige.nfcv2;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
//import android.content.SharedPreferences;
//import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.widget.Toast;

import com.gmail.matsushige.nfcv2.util.Preference;

import java.util.Calendar;


public class CountTimeAllUser extends IntentService{
	private static String TAG = "CountTimeAllUser";
	private int count = 0;
	public long startTime = 0;
	public static boolean isUsed = false;
	private static boolean finish = false;
	public static int maxCount = 20; 
	public static boolean retainUserData = false;

    static public void startCountTimeAllUser(Context context){
        finish = false;
        if (!(RelayOpenTimerIntentService.isUsed)) {
            if (!(CountTimeAllUser.isUsed)) {
                Intent intent = new Intent(context,
                        CountTimeAllUser.class);
                context.startService(intent);
            } else {
                Toast.makeText(context, "CountTime2isUsed",
                        Toast.LENGTH_SHORT).show();
            }// else
        }else{
            Toast.makeText(context, "countTime > countTime2", Toast.LENGTH_SHORT).show();
        } // else
    }// startCountTimeAllUser

   static public void stop(){
       finish = true;
   }//stop

    /** 利用画面からスタート画面に戻るまでの時間 */
	public CountTimeAllUser() {
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
				broadcastIntent.setAction("TEST2_RECEIVE_ACTION");
				sendBroadcast(broadcastIntent);

				long thisTime = Calendar.getInstance().getTimeInMillis();
				Log.d(TAG, count +":"+(thisTime - startTime));
			}
		}// for
		Log.d(TAG, "serviceEnd");
		isUsed = false;
		finish = false;
	}// onHandleIntent
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy");
	}// onDestroy
	
}//CountTimeAllUser
