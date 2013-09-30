package com.gmail.matsushige.nfcv2.db;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gmail.matsushige.R;
import com.gmail.matsushige.nfcv2.activity.BaseActivity;

public class TemporaryUsersDatabaseActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_temp_users);
        setTitle("一時使用者データベース");

        ((Button) findViewById(R.id.buttonRemoveAllTemporaryUsers)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TemporaryUsersDatabaseOperate.getTheInstance(getApplicationContext()).deleteAll();
                updateView();
            }//OnClickListener
        });

        ((Button) findViewById(R.id.buttonRemoveThisTemporaryUser)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TemporaryUsersDatabaseOperate.getTheInstance(getApplicationContext()).delete(preference.getUserType(), preference.getUserId());
                updateView();
            }
        });
    }//onCreate

    @Override
    protected void onResume() {
        super.onResume();
        updateView();
    }//onResume

    private void updateView() {
        TextView tempusersText = (TextView) findViewById(R.id.textViewCheckTempUsers);
        TemporaryUsersDatabaseOperate.getTheInstance(this).read();
        tempusersText.setText(TemporaryUsersDatabaseOperate.tempText);
    }//updateView
}//TemporaryUsersDatabaseActivity
