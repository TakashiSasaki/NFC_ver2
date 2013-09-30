package com.gmail.matsushige.nfcv2.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.gmail.matsushige.R;
import com.gmail.matsushige.nfcv2.CountRelayTime;
import com.gmail.matsushige.nfcv2.CountTimeAllUser;
import com.gmail.matsushige.nfcv2.Nfc_simple;
import com.gmail.matsushige.nfcv2.Relay;
import com.gmail.matsushige.nfcv2.db.ActLogDatabase;
import com.gmail.matsushige.nfcv2.db.TemporaryUsersDatabaseOperate;
import com.gmail.matsushige.nfcv2.util.MakeQRCode;

import java.util.Calendar;

/**
 * Created by sasaki on 13/09/29.
 */
public class TempUserActivity extends TimerActivity {
    public static String regCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nfc_main_temporary);
        setTitle("仮登録ユーザー");
        CountTimeAllUser.startCountTimeAllUser(this);
        tempUsersPic();
    }//onCreate

    private void tempUsersPic() {

        //screenState = TEMPORARY_USER;

        //CountRelayTime.maxCount = 10;

        //TODO 以下操作部分
        TextView userNameText = (TextView) findViewById(R.id.textViewUserName);
        TextView explainText = (TextView) findViewById(R.id.textViewExplain);
        userNameText.setText("ユーザー登録番号は、\n「" + regCode + "」です。");
        userNameText.setTextColor(Color.RED);
        explainText.setText("みんなでおでんき\n(http://odenki.org)\nにアクセスしてユーザ登録番号を入力してください");
        ImageView qrCode = (ImageView) findViewById(R.id.imageViewQR);
        qrCode.setImageBitmap(MakeQRCode.getQRCode("http://odenki.org/api/outlet/" + regCode));

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
                startActivity(intent);
            }// onClick
        });

        relay1Toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (CountTimeAllUser.isUsed) {
                    CountTimeAllUser.retainUserData = true;
                    CountTimeAllUser.stop();
                }
                if (!(CountRelayTime.isUsed)) {
                    CountRelayTime.startCountRelayTime(getApplicationContext(), 60 * 10);
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
                // TODO Auto-generated method stub
                if (CountTimeAllUser.isUsed) {
                    CountTimeAllUser.retainUserData = true;
                    CountTimeAllUser.stop();
                }
                if (!(CountRelayTime.isUsed)) {
                    CountRelayTime.startCountRelayTime(getApplicationContext(), 60 * 10);
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
                        preference.resetPreference();
                        Relay.openAll();
                        if (CountTimeAllUser.isUsed) {
                            CountTimeAllUser.stop();
                        }//if
                        if (CountRelayTime.isUsed) {
                            CountRelayTime.stop();
                        }//if
                        Intent intent = new Intent();
                        intent.setClass(getApplicationContext(), Nfc_simple.class);
                        startActivity(intent);
                    }//onClick
                }//View.OnClickListener
        );//setOnClickListener

    }// tempUsersPic

    @Override
    protected void onResume() {
        super.onResume();
    }//onResume

    @Override
    protected void onPause() {
        super.onPause();
    }//onResume
}//TempUserActivity
