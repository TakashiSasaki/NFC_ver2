package com.gmail.matsushige.nfcv2;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.gmail.matsushige.nfcv2.db.RegisteredUsersDatabase;
import com.gmail.matsushige.nfcv2.util.Preference;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class ReceiveDataService extends IntentService {
    private static String TAG = "ReceiveDataService";
    private static String fileName = "usersData.csv";
    private static boolean isUsed = false;


    public ReceiveDataService() {
        super("ReceiveDataService");
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // TODO Auto-generated method stub
        isUsed = true;

        Log.d(TAG, "start_service");
        downloadUsersData();
        writeDatabase();

//		TemporaryUsersDatabaseOperate.deleteOldData(getApplicationContext());
        isUsed = false;
    }// onHandleIntent

    private void downloadUsersData() {
        HttpsURLConnection httpsConnect = null;
        InputStream in = null;
        FileOutputStream out = null;
        BufferedReader bRead = null;
        BufferedWriter bWrite = null;
        try {
            String outletId = Preference.getTheInstance(getApplicationContext()).getOutletId();
            URL url = new URL("https://odenkiapi.appspot.com/api/Cards?outletId=" + outletId + "&format=csv");
            httpsConnect = (HttpsURLConnection) url.openConnection();
            httpsConnect.connect();

            in = httpsConnect.getInputStream();
            out = openFileOutput(fileName, MODE_PRIVATE);
            bRead = new BufferedReader(new InputStreamReader(in));
            bWrite = new BufferedWriter(new OutputStreamWriter(out));

            String line = "";
            while ((line = bRead.readLine()) != null) {
                bWrite.write(line + "\n");
            }// while

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e("ReceiveDataService", e.toString());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e("ReceiveDataService", e.toString());
        } finally {
            if (httpsConnect != null) {
                httpsConnect.disconnect();
            }//if(httpsConnect != null)
            if (bRead != null) {
                try {
                    bRead.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Log.e("ReceiveDataService", e.toString());
                }
            }// if(bRead != null)
            if (bWrite != null) {
                try {
                    bWrite.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Log.e("ReceiveDataService", e.toString());
                }
            }// if(bWrite != null)
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Log.e("ReceiveDataService", e.toString());
                }// catch
            }// if(in != null)
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Log.e("ReceiveDataService", e.toString());
                }// catch
            }// if(out != null)
        }// finally
        Log.d("ReceiveDataService", "end");
    }// ReceiveData

    /**
     * downloadData -> regularUsersDatabase
     */
    private void writeDatabase() {
        try {
            InputStream in = openFileInput(fileName);
            if (in != null) {
                InputStreamReader inRead = new InputStreamReader(in);
                BufferedReader bufRead = new BufferedReader(inRead);
                String str;
                RegisteredUsersDatabase.deleteAllRecord(getApplicationContext());

                while ((str = bufRead.readLine()) != null) {
                    Log.d(TAG, str);
                    String[] strSplit = str.split(",", 7);
                    /** OpenOfficeでcsvファイルを作成すると文字は""で囲まれるため、
                     * これを取り除く必要がある */
                    for (int i = 0; i < 7; i++) {
                        if (strSplit[i].contains("\"")) {
                            Log.d(TAG, "split");
                            strSplit[i] = strSplit[i].replace("\"", "");
                        }// if
                        Log.d(TAG, strSplit[i] + "@" + i);
                    }// for
                    RegisteredUsersDatabase.write(getApplicationContext(), strSplit[0], strSplit[1], strSplit[2], strSplit[3], strSplit[4], strSplit[5], strSplit[6]);
                }// while
                in.close();
            }// in
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }// writeDatabase

    public static boolean getIsUsed() {
        return isUsed;
    }// getIsUsed
}
