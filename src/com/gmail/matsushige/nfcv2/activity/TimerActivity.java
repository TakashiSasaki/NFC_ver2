package com.gmail.matsushige.nfcv2.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.matsushige.R;
import com.gmail.matsushige.nfcv2.RelayOpenTimerIntentService;
import com.gmail.matsushige.nfcv2.CountTimeAllUser;
import com.gmail.matsushige.nfcv2.Nfc_simple;

public class TimerActivity extends BaseActivity {

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intent_filter = new IntentFilter();
        intent_filter.addAction("TEST_RECEIVE_ACTION");
        registerReceiver(countRelayTimerReceiver, intent_filter);

        intent_filter = new IntentFilter();
        intent_filter.addAction("TEST2_RECEIVE_ACTION");
        registerReceiver(closeTimerBroadcastReceiver, intent_filter);
    }//onResume

    @Override
    protected void onPause() {
        super.onPause();
        CountTimeAllUser.stop();
        unregisterReceiver(closeTimerBroadcastReceiver);
        unregisterReceiver(countRelayTimerReceiver);
    }//onPause

    final protected BroadcastReceiver countRelayTimerReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            int count = bundle.getInt("count");
            /** 本登録ユーザ画面であればテキスト表示 */
            TextView textViewRelayCountdown = (TextView) findViewById(R.id.textViewRelayCountdown);
            if (textViewRelayCountdown != null)
                textViewRelayCountdown.setText("あと" + secToMin(RelayOpenTimerIntentService.getMaxCount() - count) + "で通電を終了します");
            if (count >= RelayOpenTimerIntentService.getMaxCount()) {
                preference.resetPreference();
                if (textViewRelayCountdown != null) textViewRelayCountdown.setText("使用可能です。");
                Toast.makeText(getApplicationContext(), "使用可能時間が過ぎました", Toast.LENGTH_SHORT).show();
            }// if
        }// onReceive

        private String secToMin(int sec) {
            String minSecData = "";
            if (sec > 59) {
                int syou = sec / 60; // "/":商
                int amari = sec % 60; //  "%":余
                minSecData = syou + "分" + amari + "秒";
            } else {
                minSecData = sec + "秒";
            }// else
            return minSecData;
        }// secToMin

    };// testBroadcastReceiver

    final protected BroadcastReceiver closeTimerBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Bundle bundle = intent.getExtras();
            if (action.equals("TEST2_RECEIVE_ACTION")) {
                int count = bundle.getInt("count");
                TextView timeText = (TextView) findViewById(R.id.textViewTime);
                if (timeText != null)
                    timeText.setText("あと" + (CountTimeAllUser.maxCount - count)
                            + "秒でスタート画面に戻ります");
                if (count == CountTimeAllUser.maxCount) {
                    Intent intent_to_send = new Intent();
                    intent_to_send.setClass(getApplicationContext(), Nfc_simple.class);
                    startActivity(intent_to_send);
                }// if
            }//if
        }// onReceive
    };// closeCountdownBroadcastReceiver

}//TimerActivity
