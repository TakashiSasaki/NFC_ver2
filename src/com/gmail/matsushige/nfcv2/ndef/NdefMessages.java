package com.gmail.matsushige.nfcv2.ndef;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Parcelable;

import java.util.ArrayList;

public class NdefMessages extends ArrayList<NdefMessage> {
    public NdefMessages(Intent intent) {
        Parcelable[] rawMsg = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        for (Parcelable parcelable : rawMsg) {
            this.add((NdefMessage) parcelable);
        }//for

    }//NdefMessages constructor

    public NdefMessages(Activity activity) {
        Intent intent = activity.getIntent();
        Parcelable[] rawMsg = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        for (Parcelable parcelable : rawMsg) {
            this.add((NdefMessage) parcelable);
        }//for
    }//NdefMessages constructor

}//NdefMessages
