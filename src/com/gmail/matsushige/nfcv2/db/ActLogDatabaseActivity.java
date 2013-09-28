package com.gmail.matsushige.nfcv2.db;

import android.os.Bundle;
import android.widget.TextView;

import com.gmail.matsushige.R;
import com.gmail.matsushige.nfcv2.BaseActivity;

/**
 * Created by sasaki on 13/09/26.
 */
public class ActLogDatabaseActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_act_log);
        TextView checkActLogText = (TextView) findViewById(R.id.textViewCheckActLog);
        checkActLogText.setText(ActLogDatabase.getTheInstance(this).read(this));
    }
}
