package com.gmail.matsushige.nfcv2.db;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gmail.matsushige.R;
import com.gmail.matsushige.nfcv2.activity.BaseActivity;

public class UserDatabaseActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_users);
        setTitle("登録使用者データベース");

        ((Button) findViewById(R.id.buttonRegisterUser)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisteredUsersDatabase.write(getApplicationContext(), null, preference.getUserType(), preference.getUserId(), null,
                        ((EditText) findViewById(R.id.editTextRegisteredUserName)).getEditableText().toString(), null, null);
                updateView();
            }//onClick
        });//setOnClickListener

        ((Button) findViewById(R.id.buttonRemoveAllRegisteredUsers)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisteredUsersDatabase.deleteAllRecord(getApplicationContext());
                updateView();
            }//onClick
        });//setOnClickListener

        ((Button) findViewById(R.id.buttonRemoveThisRegisteredUser)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisteredUsersDatabase.delete(getApplicationContext(), preference.getUserType(), preference.getUserId());
                updateView();
            }
        });//setOnClickListener

    }//onCreate

    @Override
    protected void onResume() {
        super.onResume();
        updateView();
    }//onResume

    private void updateView() {
        TextView checkUsersText = (TextView) findViewById(R.id.textViewCheckUsers);
        RegisteredUsersDatabase.read(this);
        checkUsersText.setText(RegisteredUsersDatabase.usersText);
    }
}//UserDatabaseActivity

