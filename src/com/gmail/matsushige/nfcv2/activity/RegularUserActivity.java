package com.gmail.matsushige.nfcv2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.gmail.matsushige.R;
import com.gmail.matsushige.nfcv2.CountTimeAllUser;
import com.gmail.matsushige.nfcv2.Nfc_simple;
import com.gmail.matsushige.nfcv2.Relay;
import com.gmail.matsushige.nfcv2.RelayOpenTimerIntentService;
import com.gmail.matsushige.nfcv2.db.ActLogDatabase;

import java.util.Calendar;

public class RegularUserActivity extends TimerActivity {
    static final int MAX_RELAY_CLOSE_SECONDS = 10;
    private EditText editTextUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nfc_main_regular);
        setTitle("みんなでおでんき ソーシャル・コンセント 登録使用者");
        editTextUserName = (EditText) findViewById(R.id.editTextUserName);
    }//onCreate

    @Override
    protected void onResume() {
        super.onResume();
        Relay.setRelayOnResume();
        reguUsersPic();
        CountTimeAllUser.startCountTimeAllUser(this);
    }//onResume

    private void reguUsersPic() {
        editTextUserName.setText(preference.getCardOwner());
        TextView userNameText = (TextView) findViewById(R.id.textViewUserName);

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

        //RelayOpenTimerIntentService.maxCount = 120;
        CountTimeAllUser.startCountTimeAllUser(this);

        powerCancelButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Relay.openAll();
                if (CountTimeAllUser.isUsed) {
                    CountTimeAllUser.stop();
                }//if
                if (RelayOpenTimerIntentService.isUsed) {
                    RelayOpenTimerIntentService.stop();
                }//if
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), Nfc_simple.class);
                startActivity(intent);
            }// onClick
        });//setOnClickListener

        relay1Toggle.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                stopCloseTimer();
                if (!(RelayOpenTimerIntentService.isUsed)) {
                    RelayOpenTimerIntentService.startCountRelayTime(getApplicationContext(), MAX_RELAY_CLOSE_SECONDS);
                }
                if (Relay.getRelay(0).isOpened()) {
                    Relay.getRelay(0).close();
                } else if (Relay.getRelay(0).isClosed()) {
                    Relay.getRelay(0).open();
                }
                long time = Calendar.getInstance().getTimeInMillis();
                ActLogDatabase.getTheInstance().write(getApplicationContext(), preference.getUserId(), preference.getCardOwner(), "リレー0" + Relay.getRelayStateString(0), time);
            }// onClick
        });//setOnClickListener

        relay2Toggle.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                stopCloseTimer();
                if (!(RelayOpenTimerIntentService.isUsed)) {
                    RelayOpenTimerIntentService.startCountRelayTime(getApplicationContext(), MAX_RELAY_CLOSE_SECONDS);
                }
                if (Relay.getRelay(1).isOpened()) {
                    Relay.getRelay(1).close();
                } else if (Relay.getRelay(1).isClosed()) {
                    Relay.getRelay(1).open();
                }
                long time = Calendar.getInstance().getTimeInMillis();
                ActLogDatabase.getTheInstance().write(getApplicationContext(), preference.getUserId(), preference.getCardOwner(), "リレー1" + Relay.getRelayStateString(1), time);
            }// onClick
        });//setOnClickListener
    }// reguUsersPic

}//RegularUserActivity
