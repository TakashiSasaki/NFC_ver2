package com.gmail.matsushige.nfcv2;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.gmail.matsushige.nfcv2.util.Preference;

import java.util.Calendar;


public class CountTimeFirstUser extends IntentService {
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

        for (count = 0; count < maxCount; count++) {
            try {
                Thread.sleep(990);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }// catch
            if (finish) {
                break;
            } else {
                Log.d(TAG, "test");

                Intent broadcastIntent = new Intent();
                broadcastIntent.putExtra("count", count + 1);
                broadcastIntent.setAction("TEST3_RECEIVE_ACTION");
                sendBroadcast(broadcastIntent);

                long thisTime = Calendar.getInstance().getTimeInMillis();
                Log.d(TAG, count + ":" + (thisTime - startTime));
            }
        }// for
        Log.d(TAG, "serviceEnd");
        isUsed = false;
        finish = false;
        if (retainUserData) {
            Log.d(TAG, "retainUserData");
            retainUserData = false;
        } else {
            Preference.getTheInstance(getApplicationContext()).resetPreference();
//			retainUserData = false;
        }//if
//		isUsed = false;
    }// onHandleIntent

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }// onDestroy
}//CountTimeFirstUser
