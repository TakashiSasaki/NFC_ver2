package com.gmail.matsushige.nfcv2.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.matsushige.R;
import com.gmail.matsushige.nfcv2.Nfc_simple;
import com.gmail.matsushige.nfcv2.db.ActLogDatabaseActivity;
import com.gmail.matsushige.nfcv2.db.TemporaryUsersDatabaseActivity;
import com.gmail.matsushige.nfcv2.db.TouchLogDatabase;
import com.gmail.matsushige.nfcv2.db.UserDatabaseActivity;
import com.gmail.matsushige.nfcv2.util.Preference;

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
        //return super.onCreateOptionsMenu(menu);
        return true;
    }//onCreateOptionsMenu

    @Override
    public boolean onOptionsItemSelected(MenuItem mi) {
//        Relay.getRelay(0).open();
//        Relay.getRelay(1).open();
//        if (CountTimeAllUser.isUsed) {
//            CountTimeAllUser.stop();
//        }
//        if (CountRelayTime.isUsed) {
//            CountRelayTime.stop();
//        }
        Intent intent = new Intent();
        switch (mi.getItemId()) {
            case R.id.usersDatabase:
                intent.setClass(this, UserDatabaseActivity.class);
                startActivity(intent);
                break;

            case R.id.logDatabase:
                setContentView(R.layout.check_log);
                TextView checkLogText = (TextView) findViewById(R.id.textViewCheckLog);
                TouchLogDatabase.read(this);
                checkLogText.setText(TouchLogDatabase.logText);
                break;

            case R.id.actlogDatabase:
                intent.setClass(this, ActLogDatabaseActivity.class);
                startActivity(intent);
                break;

            case R.id.tempusersDatabase:
                intent.setClass(this, TemporaryUsersDatabaseActivity.class);
                startActivity(intent);
                break;

            case R.id.removeTempUser:
                //TemporaryUsersDatabaseOperate.getTheInstance(this).delete(preference.getUserType(), preference.getUserId());
                preference.resetPreference();
                intent.setClass(this, Nfc_simple.class);
                startActivity(intent);
                break;

            default:
                Toast.makeText(getApplicationContext(), "fault", Toast.LENGTH_SHORT)
                        .show();
                break;
        }//switch
        return true;
    }//onOptionsItemSelected
}//BaseActivity
