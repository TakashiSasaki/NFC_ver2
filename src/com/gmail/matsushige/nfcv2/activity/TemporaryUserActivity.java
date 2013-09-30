package com.gmail.matsushige.nfcv2.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.gmail.matsushige.R;
import com.gmail.matsushige.nfcv2.CountTimeAllUser;
import com.gmail.matsushige.nfcv2.Nfc_simple;
import com.gmail.matsushige.nfcv2.Relay;
import com.gmail.matsushige.nfcv2.RelayOpenTimerIntentService;
import com.gmail.matsushige.nfcv2.db.ActLogDatabase;
import com.gmail.matsushige.nfcv2.db.TemporaryUsersDatabaseOperate;
import com.gmail.matsushige.nfcv2.util.MakeQRCode;

import java.util.Calendar;

public class TemporaryUserActivity extends TimerActivity {
    private static final int MAX_RELAY_CLOSE_SECONDS = 10;

    private EditText editTextRegistrationCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nfc_main_temporary);
        setTitle("みんなでおでんき ソーシャル・コンセント 一時使用者");
        editTextRegistrationCode = (EditText)findViewById(R.id.editTextRegistrationCode);
    }//onCreate

    @Override
    protected void onResume() {
        super.onResume();
        Relay.setRelayOnResume();
        CountTimeAllUser.startCountTimeAllUser(this);
        tempUsersPic();
    }//onResume

    private void tempUsersPic() {
        editTextRegistrationCode.setText(preference.getRegistrationCode());
        ImageView qrCode = (ImageView) findViewById(R.id.imageViewQR);
        qrCode.setImageBitmap(MakeQRCode.getQRCode("http://odenki.org/outletdemo?registrationCode=" + preference.getRegistrationCode()));

        Button powerCancelButton = (Button) findViewById(R.id.buttonPowerCancel);
        ToggleButton relay1Toggle = (ToggleButton) findViewById(R.id.toggleRelay1);
        ToggleButton relay2Toggle = (ToggleButton) findViewById(R.id.toggleRelay2);

        /** すでにリレー使用中であればトグルボタンを押した状態にする */
        if (Relay.getRelay(0).isClosed()) {
            relay1Toggle.setChecked(true);
        }//if
        if (Relay.getRelay(1).isClosed()) {
            relay2Toggle.setChecked(true);
        }//f


        powerCancelButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Relay.openAll();
                if (CountTimeAllUser.isUsed) {
                    CountTimeAllUser.stop();
                }
                if (RelayOpenTimerIntentService.isUsed) {
                    RelayOpenTimerIntentService.stop();
                }
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), Nfc_simple.class);
                startActivity(intent);
            }// onClick
        });

        relay1Toggle.setOnClickListener(new View.OnClickListener() {
            @Override
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
                //ActLogDatabase.getTheInstance().write(getApplicationContext(), hex(id).toUpperCase(), cardOwner, "リレー1" + Relay.getRelayStateString(1), time);
                ActLogDatabase.getTheInstance().write(getApplicationContext(), "unknown", "unknown", "リレー1" + Relay.getRelayStateString(1), time);
            }// onClick
        });//setOnClickListener

        relay2Toggle.setOnClickListener(new View.OnClickListener() {
            @Override
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
                ActLogDatabase.getTheInstance().write(getApplicationContext(), "unknown", "unknown", "リレー1" + Relay.getRelayStateString(1), time);
                //ActLogDatabase.getTheInstance().write(getApplicationContext(), hex(id).toUpperCase(), cardOwner, "リレー1" + Relay.getRelayStateString(1), time);
            }// onClick
        });//setOnClickListener

        ((Button) findViewById(R.id.buttonDeleteUser)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int n_deleted_rows = TemporaryUsersDatabaseOperate.getTheInstance(getApplicationContext()).delete(preference.getUserType(), preference.getUserId());
                        assert (n_deleted_rows > 0);
                        if (CountTimeAllUser.isUsed) {
                            CountTimeAllUser.stop();
                        }//if
                        if (RelayOpenTimerIntentService.isUsed) {
                            RelayOpenTimerIntentService.stop();
                        }//if
                        Intent intent = new Intent();
                        intent.setClass(getApplicationContext(), Nfc_simple.class);
                        startActivity(intent);
                    }//onClick
                }//View.OnClickListener
        );//setOnClickListener

    }// tempUsersPic

}//TemporaryUserActivity
