package com.gmail.matsushige.nfcv2.ndef;

import android.nfc.NdefRecord;

public class TextNdefRecord {

    private boolean isUtf8;
    private boolean isUtf16;

    public TextNdefRecord(NdefRecord ndef_record) {
        byte[] recordType = ndef_record.getType();
        if (!(new String(recordType)).equals("T")) {
            throw new IllegalArgumentException("record type it not text");
        }
        byte[] payload = ndef_record.getPayload();
        byte languageCodeLength = (byte) (payload[0] & 0x3F);
        byte[] language_code = new byte[languageCodeLength];
        System.arraycopy(payload, 0, language_code, 0, languageCodeLength);
        byte[] actual_text = new byte[payload.length - 1 - languageCodeLength];
        System.arraycopy(payload, 1 + languageCodeLength, actual_text, 0, actual_text.length);
        int lcLength = (int) languageCodeLength;

        if (((byte) payload[0] & 0x80) == 0) {
            this.isUtf8 = true;
            this.isUtf16 = false;
        } else {
            this.isUtf8 = false;
            this.isUtf16 = true;
        }//if
    }//TextNdefRecord

}//TextNdefRecord
