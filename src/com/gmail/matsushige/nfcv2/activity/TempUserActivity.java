package com.gmail.matsushige.nfcv2.activity;

import android.app.Activity;
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
import com.gmail.matsushige.nfcv2.util.MakeQRCode;

import java.util.Calendar;

/**
 * Created by sasaki on 13/09/29.
 */
public class TempUserActivity extends Activity {
    public static String regCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nfc_main_temporary);
        setTitle(this.getLocalClassName());
        tempUsersPic();
    }//onCreate

    private void tempUsersPic(){

        //screenState = TEMPORARY_USER;

        CountRelayTime.maxCount = 10;

        //TODO 以下操作部分
        TextView userNameText = (TextView) findViewById(R.id.textViewUserName);
        TextView explainText = (TextView) findViewById(R.id.textViewExplain);
        userNameText.setText("ユーザー登録番号は、\n「"+ regCode +"」です。");
        userNameText.setTextColor(Color.RED);
        explainText.setText("みんなでおでんき\n(http://odenki.org)\nにアクセスしてユーザ登録番号を入力してください");
        ImageView qrCode = (ImageView) findViewById(R.id.imageViewQR);
        qrCode.setImageBitmap(MakeQRCode.getQRCode("http://odenki.org/api/outlet/" + regCode));

        Button powerCancelButton = (Button) findViewById(R.id.buttonPowerCancel);
        ToggleButton relay1Toggle = (ToggleButton)findViewById(R.id.toggleRelay1);
        ToggleButton relay2Toggle = (ToggleButton)findViewById(R.id.toggleRelay2);

        /** すでにリレー使用中であればトグルボタンを押した状態にする */
        if(Relay.getRelay(0).isClosed()){
            relay1Toggle.setChecked(true);
        }
        if(Relay.getRelay(1).isClosed()){
            relay2Toggle.setChecked(true);
        }

        CountTimeAllUser.startCountTimeAllUser(this);

        powerCancelButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Relay.getRelay(0).open();
                Relay.getRelay(1).open();
                if(CountTimeAllUser.isUsed){
                    CountTimeAllUser.finish = true;
                }
                if(CountRelayTime.isUsed){
                    CountRelayTime.finish = true;
                }
                //changeMainXto0();
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), Nfc_simple.class);
                startActivity(intent);
            }// onClick
        });

        relay1Toggle.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(CountTimeAllUser.isUsed){
                    CountTimeAllUser.retainUserData = true;
                    CountTimeAllUser.finish = true;
                }
                if(!(CountRelayTime.isUsed)){
                    CountRelayTime.startCountRelayTime(getApplicationContext());
                }
                if(Relay.getRelay(0).isOpened()){
                    Relay.getRelay(0).close();
                }else if(Relay.getRelay(0).isClosed()){
                    Relay.getRelay(0).open();
                }
                long time = Calendar.getInstance().getTimeInMillis();
                //ActLogDatabase.getTheInstance().write(getApplicationContext(), hex(id).toUpperCase(), cardOwner, "リレー1" + Relay.getRelayStateString(1), time);
                ActLogDatabase.getTheInstance().write(getApplicationContext(), "unknown", "unknown", "リレー1" + Relay.getRelayStateString(1), time);
            }// onClick
        });

        relay2Toggle.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(CountTimeAllUser.isUsed){
                    CountTimeAllUser.retainUserData = true;
                    CountTimeAllUser.finish = true;
                }
                if(!(CountRelayTime.isUsed)){
                    CountRelayTime.startCountRelayTime(getApplicationContext());
                }
                if(Relay.getRelay(1).isOpened()){
                    Relay.getRelay(1).close();
                }else if(Relay.getRelay(1).isClosed()){
                    Relay.getRelay(1).open();
                }
                long time = Calendar.getInstance().getTimeInMillis();
                ActLogDatabase.getTheInstance().write(getApplicationContext(), "unknown", "unknown", "リレー1" + Relay.getRelayStateString(1), time);
                //ActLogDatabase.getTheInstance().write(getApplicationContext(), hex(id).toUpperCase(), cardOwner, "リレー1" + Relay.getRelayStateString(1), time);
            }// onClick
        });
    }// tempUsersPic

}//TempUserActivity
