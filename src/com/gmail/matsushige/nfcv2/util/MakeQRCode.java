package com.gmail.matsushige.nfcv2.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.Hashtable;

public class MakeQRCode {
    private static String TAG = "MakeQRCode";
    private static int size = 160;

    public static Bitmap getQRCode(String rawData) {
        Log.d(TAG, "start");
        Bitmap bitmap = null;
        QRCodeWriter writer = new QRCodeWriter();
        Hashtable encodeHint = new Hashtable();
        encodeHint.put(EncodeHintType.CHARACTER_SET, "Shift_JIS");
        encodeHint.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        try {
            BitMatrix qrCodeData = writer.encode(rawData, BarcodeFormat.QR_CODE, size, size, encodeHint);
            bitmap = Bitmap.createBitmap(size, size, Config.ARGB_8888);
            int height = bitmap.getHeight();
            int width = bitmap.getWidth();
            int[] pixels = new int[height * width];
            for (int y = 0; y < height; y++) {
                int offset = y * width;
                for (int x = 0; x < width; x++) {
                    pixels[offset + x] = (qrCodeData.get(x, y) ? Color.BLACK : Color.WHITE);
                }// for
            }// for
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            Log.d(TAG, "finishCreatingQRCode");
            Log.d(TAG, height + "," + width);
            return bitmap;
        } catch (WriterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }// getQRCode

}//MakeQRCode