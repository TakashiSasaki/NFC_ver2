package com.gmail.matsushige.nfcv2.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.gmail.matsushige.R;

public class UnknownUserActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unknown_user_activity);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.unknown_user, menu);
        return true;
    }
    
}
