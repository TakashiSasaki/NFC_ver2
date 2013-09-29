package com.gmail.matsushige.nfcv2.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.matsushige.R;
import com.gmail.matsushige.nfcv2.CountRelayTime;

public class TimerActivity extends BaseActivity {

    public BroadcastReceiver countRelayTimeReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            int count = bundle.getInt("count");
            /** 本登録ユーザ画面であればテキスト表示 */
            ((TextView) findViewById(R.id.textViewRelayCountdown)).setText("使用中です。\nあと" + secToMin(CountRelayTime.getMaxCount() - count) + "で通電を終了します");
            if (count >= CountRelayTime.getMaxCount()) {
                preference.resetPreference();
                ((TextView) findViewById(R.id.textViewRelayCountdown)).setText("使用可能です。");
                Toast.makeText(getApplicationContext(), "使用可能時間が過ぎました", Toast.LENGTH_SHORT).show();
                //changeMainXto0();
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

}//TimerActivity

