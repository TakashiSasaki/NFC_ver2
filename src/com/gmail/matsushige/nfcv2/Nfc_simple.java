package com.gmail.matsushige.nfcv2;

import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.matsushige.R;
import com.gmail.matsushige.nfcv2.activity.FirstUserActivity;
import com.gmail.matsushige.nfcv2.activity.RegularUserActivity;
import com.gmail.matsushige.nfcv2.activity.TempUserActivity;
import com.gmail.matsushige.nfcv2.activity.TimerActivity;
import com.gmail.matsushige.nfcv2.db.ActLogDatabase;
import com.gmail.matsushige.nfcv2.db.CardUser;
import com.gmail.matsushige.nfcv2.db.TemporaryUsersDatabaseOperate;
import com.gmail.matsushige.nfcv2.db.TouchLogDatabase;
import com.gmail.matsushige.nfcv2.db.UsersDatabase;

import java.util.Calendar;

public class Nfc_simple extends TimerActivity {
    private TextView timeText;
    private byte[] id;
    private String type = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nfc_main_start);
        setTitle("ソーシャルコンセント");

        Relay.test(getApplicationContext());
    }// onCreate

    @Override
    public void onResume() {
        super.onResume();

        Relay.setRelayOnResume();

        //testReceiver = new testBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("TEST_RECEIVE_ACTION");
        registerReceiver(countRelayTimerReceiver, filter);

        if (!(preference.getBoolean("timerSet", false))) {
            DayAlarmManager.regularShortTimerSet(getApplicationContext());
            preference.putBoolean("timerSet", true);
        } else {
            Log.d("Activity", "already_set");
        }//if

        // when invoked by startActivity, it gets no intent..
        String action = getIntent().getAction();

        if (action == null) return;
        if (action.equals(NfcAdapter.ACTION_TECH_DISCOVERED)) {
            readNfc();
        } else if (action.equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
            readNfc();
        }// if
    }// onResume

    @Override
    public void onPause() {
        super.onPause();
        if (!(CountRelayTime.isUsed)) {
            Relay.closeAccessory();
        }
        unregisterReceiver(countRelayTimerReceiver);
    }// onPause

    @Override
    public void onDestroy() {
        super.onDestroy();
        Relay.unRegisterReceiver(getApplicationContext());
    }// onDestroy

    private void readNfc() {
        Intent intent = getIntent();
        id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
        Tag tag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

//		String idD = "ID : " + hex(id);
        String[] techList = tag.getTechList();
        String tech = "TechList : ";
        for (String w : techList) {
            tech += w;
        }// for

        if (tech.contains("NfcF")) {
            type = "f";
            recordId(type, hex(id).toUpperCase());
        }// if <NfcF>
        else if (tech.contains("NfcB")) {
            type = "b";
            recordId(type, hex(id).toUpperCase());
        }// else if <NfcB>
        else if (tech.contains("NfcA")) {
            type = "a";
            recordId(type, hex(id).toUpperCase());
        }// else if <NfcA>
        else {
            Toast.makeText(getApplicationContext(), "対応していないカードです",
                    Toast.LENGTH_SHORT).show();
        }// else
    }// readNfc

    private String hex(byte[] data) {
        String hexText = "";
        for (byte v : data) {
            hexText += String.format("%02x", new Object[]{v});
        }// for
        return hexText;
    }// hex

    private void recordId(String type, String id) {

        long timestamp = Calendar.getInstance().getTimeInMillis();
        TouchLogDatabase.write(this, type, id, timestamp);

        String userType = preference.getUserType();
        String userId = preference.getUserId();
        if (userType == null && userId == null) {
            preference.setTypeAndId(type, id);
            //recordPreference(type, id);
        } else if (type.equals(userType) && id.equals(userId)) {
            Toast.makeText(getApplicationContext(), "あなたは使用中です", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "ほかのユーザが使用中です", Toast.LENGTH_SHORT).show();
            return;
        }//if

        CardUser registered_user = UsersDatabase.getRegisteredUser(this, type, id);

        if (registered_user != null) {
            preference.setCardOwner(registered_user.getOwnerName());
            ActLogDatabase.getTheInstance(this).write(this, id, registered_user.getOwnerName(), "タッチ", timestamp);
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), RegularUserActivity.class);
            startActivity(intent);
        } else {
            CardUser temporary_user = TemporaryUsersDatabaseOperate.getTheInstance(getApplicationContext()).getRegisteredData(type, id);
            if (temporary_user != null) {
                assert (temporary_user.getCardId().equals(id));
                assert (temporary_user.getCardType().equals(type));
                preference.setTypeAndId(type, id);
                ActLogDatabase.getTheInstance(this).write(this, id, temporary_user.getOwnerName(), "タッチ", timestamp);
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), TempUserActivity.class);
                startActivity(intent);
            } else {
                //cardOwner = "未登録者";
                ActLogDatabase.getTheInstance(this).write(this, id, "未知のカード", "タッチ", timestamp);
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), FirstUserActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("type", type);
                intent.putExtra("cardOwner", "未登録");
                startActivity(intent);
                //firstUsersPic();
            }// if
        }// if
    }// recordId

}// Nfc_simple

