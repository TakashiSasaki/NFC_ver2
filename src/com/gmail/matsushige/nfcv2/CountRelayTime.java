package com.gmail.matsushige.nfcv2;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.gmail.matsushige.nfcv2.util.Preference;

import java.util.Calendar;


public class CountRelayTime extends IntentService {
    private static String TAG = "CountRelayTime";
    private int count = 0;
    public long startTime = 0;
    public static boolean isUsed = false;
    private static boolean finish = false;
    public static int maxCount = 15;

    public static void startCountRelayTime(Context context) {
        if (!(CountRelayTime.isUsed)) {
            Intent intent = new Intent(context, CountRelayTime.class);
//			intent.putExtra("cardType", type);
            context.startService(intent);
        } else {
            Toast.makeText(context, "CountRelayTimeisUsed", Toast.LENGTH_SHORT).show();
        }// else
    }// startCountTime

    static public void stop() {
        finish = true;
    }//stop

//	private UsbManager mUsbManager;
//	private ParcelFileDescriptor mFileDescriptor;
//	private UsbAccessory mUsbAccessory;
//	private FileInputStream mfiFileInputStream;
//	private FileOutputStream mFileOutputStream;

    public CountRelayTime() {
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
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }// catch
            if (finish) {
                break;
            } else {
//				Log.d(TAG, "test");

                Intent broadcastIntent = new Intent();
                broadcastIntent.putExtra("count", count + 1);
                broadcastIntent.setAction("TEST_RECEIVE_ACTION");
                sendBroadcast(broadcastIntent);

//				long thisTime = Calendar.getInstance().getTimeInMillis();
//				Log.d(TAG, count +":"+(thisTime - startTime));
            }
        }// for
        long endTime = Calendar.getInstance().getTimeInMillis();
        Log.d(TAG, "" + (endTime - startTime));
        Log.d(TAG, "serviceEnd");

        Relay.getRelay(0).open();
        Relay.getRelay(1).open();
        Relay.closeAccessory();

        isUsed = false;
        finish = false;
        Preference.getTheInstance(getApplicationContext()).resetPreference();
//		isUsed = false;
    }// onHandleIntent

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }// onDestroy
}//CountRelayTime
