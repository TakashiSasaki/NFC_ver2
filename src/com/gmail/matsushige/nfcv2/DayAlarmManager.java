package com.gmail.matsushige.nfcv2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class DayAlarmManager {
	private static String TAG = "DayAlarmManager";
	
    /** <shot version ( 15 minutes )> */
	public static void regularShortTimerSet(Context context){
    	AlarmManager alManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    	
    	Intent intent = new Intent(context, ReceiveDataService.class);
    	PendingIntent pIntent = PendingIntent.getService(context, 0, intent, 0);
    	
//		long setTime = getCurrentHour();
    	long setTime = getToday();
    	if(setTime > 0){
    		Log.d(TAG, ""+setTime);
//        	alManager.setRepeating(AlarmManager.RTC_WAKEUP, setTime, AlarmManager.INTERVAL_FIFTEEN_MINUTES, pIntent);
    		alManager.setRepeating(AlarmManager.RTC_WAKEUP, setTime, AlarmManager.INTERVAL_DAY, pIntent);
    	}else{
    		Log.e(TAG, "failed_to_set_time");
    	}
    }// regularTimerSet
  

	/** This returns current hour.
     *   (201211301430) -> (201211301400)
     *  If it occurs error, return 0. */
    public static long getCurrentHour(){
    	long currentHour = 0;
    	Calendar calendar = Calendar.getInstance();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
    	String formatTimeString = sdf.format(calendar.getTime());
    	Log.d(TAG, formatTimeString);
    	Date date = null;
    	try {
			date = sdf.parse(formatTimeString);
			calendar.setTime(date);
			currentHour = calendar.getTimeInMillis();
			Log.d(TAG, (new Date(currentHour)).toLocaleString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(TAG, e.toString());
		}// catch
    	return currentHour;
    }// getCurrentHour

    public static long getToday(){
    	long today = 0;
    	Calendar calendar = Calendar.getInstance();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    	String formatTimeString = sdf.format(calendar.getTime());
    	Log.d(TAG, formatTimeString);
    	Date date = null;
    	try {
			date = sdf.parse(formatTimeString);
			calendar.setTime(date);
			today = calendar.getTimeInMillis();
			Log.d(TAG, (new Date(today)).toLocaleString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(TAG, e.toString());
		}// catch
    	return today;
    }// getToday
}
