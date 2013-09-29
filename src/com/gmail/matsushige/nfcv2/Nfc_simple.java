package com.gmail.matsushige.nfcv2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.gmail.matsushige.R;
import com.gmail.matsushige.nfcv2.activity.BaseActivity;
import com.gmail.matsushige.nfcv2.activity.FirstUserActivity;
import com.gmail.matsushige.nfcv2.activity.TempUserActivity;
import com.gmail.matsushige.nfcv2.db.ActLogDatabase;
import com.gmail.matsushige.nfcv2.db.Database;
import com.gmail.matsushige.nfcv2.db.TemporaryUser;
import com.gmail.matsushige.nfcv2.db.TemporaryUsersDatabaseOperate;
import com.gmail.matsushige.nfcv2.db.UsersDatabase;

import java.util.Calendar;

public class Nfc_simple extends BaseActivity {
	private TextView timeText;
	private byte[] id;
//	private String tech = "";
	private String type = "";

	//private static final int FIRST_USER = 1;
	private static final int TEMPORARY_USER = 2;
	private static final int REGULAR_USER = 3;

	public static String cardOwner = "";

	public testBroadcastReceiver testReceiver;
	public int screenState = 0;

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

		testReceiver = new testBroadcastReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("TEST_RECEIVE_ACTION");
		registerReceiver(testReceiver, filter);

		if (!(preference.getBoolean("timerSet", false))) {
			DayAlarmManager.regularShortTimerSet(getApplicationContext());
			preference.putBoolean("timerSet", true);
		}else{
			Log.d("Activity", "already_set");
		}//if

        // when invoked by startActivity, it gets no intent..
		String action = getIntent().getAction();

        if(action == null) return;
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
		unregisterReceiver(testReceiver);
	}// onPause

	@Override
	public void onDestroy() {
		super.onDestroy();
		Relay.unRegisterReceiver(getApplicationContext());
	}// onDestroy



	private void reguUsersPic(){
		linearLayout.removeAllViews();
		LayoutInflater li = getLayoutInflater();
		li.inflate(R.layout.nfc_main_regular, linearLayout);

		screenState = REGULAR_USER;

		TextView userNameText = (TextView) findViewById(R.id.textViewUserName);
		userNameText.setText(cardOwner + "さん、こんにちは。");

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

		CountRelayTime.maxCount = 120;
		CountTimeAllUser.startCountTimeAllUser(this);

		powerCancelButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Relay.getRelay(0).open();
				Relay.getRelay(1).open();
				if(CountTimeAllUser.isUsed){
					CountTimeAllUser.stop();
				}
				if(CountRelayTime.isUsed){
					CountRelayTime.stop();
				}
				changeMainXto0();
			}// onClick
		});

		relay1Toggle.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(CountTimeAllUser.isUsed){
					CountTimeAllUser.retainUserData = true;
					CountTimeAllUser.stop();
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
                ActLogDatabase.getTheInstance().write(getApplicationContext(), hex(id).toUpperCase(), cardOwner, "リレー0" + Relay.getRelayStateString(0), time);
			}// onClick
		});

		relay2Toggle.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(CountTimeAllUser.isUsed){
					CountTimeAllUser.retainUserData = true;
					CountTimeAllUser.stop();
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
                ActLogDatabase.getTheInstance().write(getApplicationContext(), hex(id).toUpperCase(), cardOwner, "リレー1" + Relay.getRelayStateString(1), time);
			}// onClick
		});
	}// reguUsersPic

	public void changeMainXto0() { // スタート画面へ
		timeText = (TextView) findViewById(R.id.textViewTime);
		timeText.setText("");
		linearLayout.removeAllViews();
		LayoutInflater li = getLayoutInflater();
		li.inflate(R.layout.nfc_main_start, linearLayout);
		screenState = 0;
	}// changeMainXto0

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
			hexText += String.format("%02x", new Object[] { v });
		}// for
		return hexText;
	}// hex

	private void recordId(String type, String id) {

		long timestamp = Calendar.getInstance().getTimeInMillis();
		Database.write(this, type, id, timestamp);

		//TODO プリファレンスチェック
		String userType = preference.getUserType();
		String userId = preference.getUserId();
		if(userType == null && userId == null){
            preference.setTypeAndId(type, id);
			//recordPreference(type, id);
		}else if(type.equals(userType) && id.equals(userId)){
			Toast.makeText(getApplicationContext(), "あなたは使用中です", Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(getApplicationContext(), "ほかのユーザが使用中です", Toast.LENGTH_SHORT).show();
			return;
		}

		UsersDatabase.checkResist(this, type, id);

		if (!("".equals(cardOwner))) {
			ActLogDatabase.getTheInstance(this).write(this, id, cardOwner, "タッチ", timestamp);
			reguUsersPic();
		} else {
			TemporaryUser temporary_user = TemporaryUsersDatabaseOperate.getTheInstance(getApplicationContext()).getRegisteredData(type, id);
			if(temporary_user != null){
                assert(temporary_user.getCardId().equals(id));
                assert(temporary_user.getCardType().equals(type));
                preference.setTypeAndId(type, id);
				ActLogDatabase.getTheInstance(this).write(this, id, cardOwner, "タッチ", timestamp);
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), TempUserActivity.class);
                startActivity(intent);
			}else{
				//cardOwner = "未登録者";
				ActLogDatabase.getTheInstance(this).write(this, id, cardOwner, "タッチ", timestamp);
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), FirstUserActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("type", type);
                intent.putExtra("cardOwner", "未登録");
                startActivity(intent);
				//firstUsersPic();
			}// else
		}// else
	}// recordId



	public class testBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Bundle bundle = intent.getExtras();
			int count = bundle.getInt("count");
			/** 本登録ユーザ画面であればテキスト表示 */
			if (screenState == REGULAR_USER || screenState == TEMPORARY_USER) {
				timeText = (TextView) findViewById(R.id.textViewTime);
//				timeText.setText("あと"+(CountRelayTime.maxCount - count)+"秒で終了します");
				timeText.setText("あと" + secToMin(CountRelayTime.maxCount - count) +"で終了します");
			}// if
			if(count == CountRelayTime.maxCount){
                preference.resetPreference();
				Toast.makeText(getApplicationContext(), "使用可能時間が過ぎました", Toast.LENGTH_SHORT).show();
				changeMainXto0();
			}// if
		}// onReceive

		private String secToMin(int sec){
			String minSecData = "";
			if(sec > 59){
				int syou = sec / 60; // "/":商
				int amari = sec % 60; //  "%":余
				minSecData = syou + "分" + amari + "秒";
			}else{
				minSecData = sec + "秒";
			}// else
			return minSecData;
		}// secToMin

	}// testBroadcastReceiver

}// Nfc_simple

