package com.gmail.matsushige.nfcv2.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.matsushige.R;
import com.gmail.matsushige.nfcv2.CountTimeAllUser;
import com.gmail.matsushige.nfcv2.Nfc_simple;
import com.gmail.matsushige.nfcv2.SendDataService;
import com.gmail.matsushige.nfcv2.db.ActLogDatabase;
import com.gmail.matsushige.nfcv2.db.TemporaryUser;
import com.gmail.matsushige.nfcv2.db.TemporaryUsersDatabaseOperate;
import com.gmail.matsushige.nfcv2.util.RegistrationCode;

import java.util.Calendar;

public class FirstUserActivity extends BaseActivity {

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;

    private String cardOwner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nfc_main_first);
        setTitle(this.getLocalClassName());
    }//onCreate

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        this.type = intent.getStringExtra("type");
        this.id = intent.getStringExtra("id");
        this.cardOwner = intent.getStringExtra("cardOwner");
        firstUsersPic();

        IntentFilter intent_filter = new IntentFilter();
        intent_filter.addAction("TEST2_RECEIVE_ACTION");
        registerReceiver(closeTimerBroadcastReceiver, intent_filter);
    }//onResume

    BroadcastReceiver closeTimerBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Bundle bundle = intent.getExtras();
            if (action.equals("TEST2_RECEIVE_ACTION")) {
                int count = bundle.getInt("count");
                TextView timeText = (TextView) findViewById(R.id.textViewTime);
                timeText.setText("あと" + (CountTimeAllUser.maxCount - count)
                        + "秒でスタート画面に戻ります");
                if (count == CountTimeAllUser.maxCount) {
                    Intent intent_to_send = new Intent();
                    intent_to_send.setClass(getApplicationContext(), Nfc_simple.class);
                    startActivity(intent_to_send);
                }// if
            }
        }// onReceive
    };// closeCountdownBroadcastReceiver

    @Override
    protected void onPause() {
        super.onPause();
        CountTimeAllUser.stop();
        unregisterReceiver(closeTimerBroadcastReceiver);
    }//onPause

    private void firstUsersPic() {

        //screenState = FIRST_USER;

        //startCountTimeFirstUser();
        CountTimeAllUser.startCountTimeAllUser(this);
        Button registYesButton = (Button) findViewById(R.id.buttonResistYes);
        Button registNoButton = (Button) findViewById(R.id.buttonResistNo);

        registYesButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (CountTimeAllUser.isUsed) {
                    CountTimeAllUser.retainUserData = true;
                    CountTimeAllUser.stop();
                }//if

                usersInput(getType(), getId());
//				if (type.equals("f")) {
//					usersInput(type, hex(id).toUpperCase());
//				} else if (type.equals("b")) {
//					usersInput(type, hex(id).toUpperCase());
//				} else if (type.equals("a")) {
//					usersInput(type, hex(id).toUpperCase());
//				}
                if (!(SendDataService.getIsUsed())) {
                    Intent intent = new Intent(getApplicationContext(),
                            SendDataService.class);
                    startService(intent);
                }
                preference.setTypeAndId(getType(), getId());
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), TempUserActivity.class);
                startActivity(intent);
            }// onClick
        });

        registNoButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (CountTimeAllUser.isUsed) {
                    CountTimeAllUser.stop();
                }
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), Nfc_simple.class);
                startActivity(intent);
                //changeMainXto0();
            }// onClick
        });
    }// firstUsersPic


    /**
     * TemporaryUsersDatabaseに記録
     */
    public void usersInput(String type, String id) {
        String registration_code = RegistrationCode.getTheInstance(getApplicationContext()).GenerateRegisterCode();
        Log.d(this.getLocalClassName() + "#userInput", registration_code);

        TemporaryUsersDatabaseOperate.getTheInstance(getApplicationContext()).write(type, id, registration_code);
        Toast.makeText(getApplicationContext(), "登録しました" + "(" + registration_code + ")", Toast.LENGTH_SHORT).show();

        TemporaryUser temporary_user = TemporaryUsersDatabaseOperate.getTheInstance(getApplicationContext()).getRegisteredData(type, id);
        long time = Calendar.getInstance().getTimeInMillis();
        ActLogDatabase.getTheInstance(this).write(this, id, cardOwner, "登録", time);
    }// usersInput

}//FirstUserActivity
