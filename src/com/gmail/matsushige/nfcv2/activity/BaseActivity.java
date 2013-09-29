package com.gmail.matsushige.nfcv2.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.matsushige.R;
import com.gmail.matsushige.nfcv2.CountRelayTime;
import com.gmail.matsushige.nfcv2.CountTimeAllUser;
import com.gmail.matsushige.nfcv2.Relay;
import com.gmail.matsushige.nfcv2.db.ActLogDatabaseActivity;
import com.gmail.matsushige.nfcv2.db.Database;
import com.gmail.matsushige.nfcv2.db.TemporaryUsersDatabaseOperate;
import com.gmail.matsushige.nfcv2.db.UsersDatabase;
import com.gmail.matsushige.nfcv2.util.Preference;

/**
 * Created by sasaki on 13/09/26.
 */
public class BaseActivity extends Activity {
    protected LinearLayout linearLayout;
    protected Preference preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.preference = Preference.getTheInstance(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }//onCreate

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menu_inflater = getMenuInflater();
        menu_inflater.inflate(R.menu.base_activity, menu);
//        menu.add(Menu.NONE, 0, 0, "usersDatabase");
//        menu.add(Menu.NONE, 1, 0, "logDatabase");
//        menu.add(Menu.NONE, 2, 0, "actlogDatabase");
//        menu.add(Menu.NONE, 3, 0, "スタート画面");
//        menu.add(Menu.NONE, 4, 0, "tempusersDatabase");
        //return super.onCreateOptionsMenu(menu);
        return true;
    }//onCreateOptionsMenu

    @Override
    public boolean onOptionsItemSelected(MenuItem mi) {
        Relay.getRelay(0).open();
        Relay.getRelay(1).open();
        if (CountTimeAllUser.isUsed) {
            CountTimeAllUser.stop();
        }
        if (CountRelayTime.isUsed) {
            CountRelayTime.stop();
        }
        switch (mi.getItemId()) {
            case R.id.usersDatabase:
                setContentView(R.layout.check_users);
                TextView checkUsersText = (TextView) findViewById(R.id.textViewCheckUsers);
                UsersDatabase.read(this);
                checkUsersText.setText(UsersDatabase.usersText);
                break;

            case R.id.logDatabase:
                setContentView(R.layout.check_log);
                TextView checkLogText = (TextView) findViewById(R.id.textViewCheckLog);
                Database.read(this);
                checkLogText.setText(Database.logText);
                break;

            case R.id.actlogDatabase:
                Intent intent = new Intent();
                intent.setClass(this, ActLogDatabaseActivity.class);
                startActivity(intent);
                break;

            case R.id.start:
                setContentView(R.layout.nfc_main);
                linearLayout = (LinearLayout) findViewById(R.id.linearLayoutMain);
                LayoutInflater li = getLayoutInflater();
                li.inflate(R.layout.nfc_main_start, linearLayout);
                break;

            case R.id.tempusersDatabase:
                setContentView(R.layout.check_temp_users);
                TextView tempusersText = (TextView) findViewById(R.id.textViewCheckTempUsers);
                TemporaryUsersDatabaseOperate.getTheInstance(this).read();
                tempusersText.setText(TemporaryUsersDatabaseOperate.tempText);
                break;

            case R.id.reset:
                Preference.getTheInstance(this).resetPreference();
                break;

            case R.id.removeTempUser:
                TemporaryUsersDatabaseOperate.getTheInstance(this).delete(preference.getUserType(), preference.getUserId());
                preference.resetPreference();

            default:
                Toast.makeText(getApplicationContext(), "fault", Toast.LENGTH_SHORT)
                        .show();
                break;
        }//switch
        return true;
    }//onOptionsItemSelected
}//BaseActivity
