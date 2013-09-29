package com.gmail.matsushige.nfcv2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.gmail.matsushige.R;
import com.gmail.matsushige.nfcv2.CountRelayTime;
import com.gmail.matsushige.nfcv2.CountTimeAllUser;
import com.gmail.matsushige.nfcv2.Nfc_simple;
import com.gmail.matsushige.nfcv2.Relay;
import com.gmail.matsushige.nfcv2.db.ActLogDatabase;

import java.util.Calendar;

public class RegularUserActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nfc_main_regular);
    }//onCreate

    @Override
    protected void onResume() {
        super.onResume();
        reguUsersPic();
    }//onResume

    private void reguUsersPic() {
        linearLayout.removeAllViews();
        LayoutInflater li = getLayoutInflater();
        li.inflate(R.layout.nfc_main_regular, linearLayout);

        TextView userNameText = (TextView) findViewById(R.id.textViewUserName);
        userNameText.setText(preference.getCardOwner() + "さん、こんにちは。");

        Button powerCancelButton = (Button) findViewById(R.id.buttonPowerCancel);
        ToggleButton relay1Toggle = (ToggleButton) findViewById(R.id.toggleRelay1);
        ToggleButton relay2Toggle = (ToggleButton) findViewById(R.id.toggleRelay2);

        /** すでにリレー使用中であればトグルボタンを押した状態にする */
        if (Relay.getRelay(0).isClosed()) {
            relay1Toggle.setChecked(true);
        }//if
        if (Relay.getRelay(1).isClosed()) {
            relay2Toggle.setChecked(true);
        }//if

        //CountRelayTime.maxCount = 120;
        CountTimeAllUser.startCountTimeAllUser(this);

        powerCancelButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Relay.getRelay(0).open();
                Relay.getRelay(1).open();
                if (CountTimeAllUser.isUsed) {
                    CountTimeAllUser.stop();
                }
                if (CountRelayTime.isUsed) {
                    CountRelayTime.stop();
                }
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), Nfc_simple.class);
            }// onClick
        });

        relay1Toggle.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (CountTimeAllUser.isUsed) {
                    CountTimeAllUser.retainUserData = true;
                    CountTimeAllUser.stop();
                }
                if (!(CountRelayTime.isUsed)) {
                    CountRelayTime.startCountRelayTime(getApplicationContext(), 60*60);
                }
                if (Relay.getRelay(0).isOpened()) {
                    Relay.getRelay(0).close();
                } else if (Relay.getRelay(0).isClosed()) {
                    Relay.getRelay(0).open();
                }
                long time = Calendar.getInstance().getTimeInMillis();
                ActLogDatabase.getTheInstance().write(getApplicationContext(), preference.getUserId(), preference.getCardOwner(), "リレー0" + Relay.getRelayStateString(0), time);
            }// onClick
        });

        relay2Toggle.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (CountTimeAllUser.isUsed) {
                    CountTimeAllUser.retainUserData = true;
                    CountTimeAllUser.stop();
                }
                if (!(CountRelayTime.isUsed)) {
                    CountRelayTime.startCountRelayTime(getApplicationContext(), 60*60);
                }
                if (Relay.getRelay(1).isOpened()) {
                    Relay.getRelay(1).close();
                } else if (Relay.getRelay(1).isClosed()) {
                    Relay.getRelay(1).open();
                }
                long time = Calendar.getInstance().getTimeInMillis();
                ActLogDatabase.getTheInstance().write(getApplicationContext(), preference.getUserId(), preference.getCardOwner(), "リレー1" + Relay.getRelayStateString(1), time);
            }// onClick
        });
    }// reguUsersPic

}//RegularUserActivity
