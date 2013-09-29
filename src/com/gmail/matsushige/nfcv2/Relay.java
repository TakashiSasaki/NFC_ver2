package com.gmail.matsushige.nfcv2;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbManager;
import android.os.ParcelFileDescriptor;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class Relay{

    static public String getRelayStateString(int target){
        if(Relay.getRelay(target).isClosed()){
            return " ON";
        }else if(Relay.getRelay(target).isOpened()){
            return " OFF";
        }//if
        return " UNKNOWN";
    }

    private static Relay relay0;
	private static Relay relay1;
	
	private Date mLastClosed;
	private Date mLastOpened;
	private int mRelayId;
	private boolean mIsOpened = true;
	
	private static FileInputStream mInputStream;
	private static FileOutputStream mOutputStream;
	private static ParcelFileDescriptor mFileDescriptor;
	private static boolean mPermissionRequestPending;
	private static PendingIntent mPermissionIntent;
	private static final String ACTION_USB_PERMISSION = "com.gmail.matsushige.RelaySample.action.USB_PERMISSION";
	private static UsbManager mUsbManager;
	private static UsbAccessory mAccessory;

	public static Relay getRelay(int relay_id) {
		if (relay_id == 0) {
			if (relay0 != null) {
				return relay0;
			} else {
				relay0 = new Relay(0);
				return relay0;
			}// if
		} else if (relay_id == 1) {
			if (relay1 != null) {
				return relay1;
			} else {
				relay1 = new Relay(1);
				return relay1;
			}// if
		} else {
			throw new IllegalArgumentException("Relay ID should be 0 or 1.");
		}
	}// getRelay

	private Relay(int relay_id) {
		assert (relay_id == 0 || relay_id == 1);
		this.mRelayId = relay_id;
		this.open();
	}// the constructor

	public Date getLastClosed() {
		return this.mLastClosed;
	}// getLastClosed

	public Date getLastOpened() {
		return this.mLastOpened;
	}// getLastOpened

	public boolean isOpened() {
		return this.mIsOpened;
	}// isOpened

	public boolean isClosed() {
		return !this.mIsOpened;
	}// isClosed

	public void open() {
		sendRelayCommand(this.mRelayId, 0);
		mLastOpened = Calendar.getInstance().getTime();
		this.mIsOpened = true;
	}// open

	public void close() {
		sendRelayCommand(this.mRelayId, 1);
		mLastClosed = Calendar.getInstance().getTime();
		this.mIsOpened = false;
		}// close

	private void sendRelayCommand(int target, int value) {
		byte[] buffer = new byte[3];

		buffer[0] = (byte) 0x3;
		buffer[1] = (byte) target;
		buffer[2] = (byte) value;
		if (mOutputStream != null && buffer[1] != -1) {
			try {
				mOutputStream.write(buffer);
			} catch (IOException e) {

			}
		}
	}// sendRelayCommand
	
	public static void closeAccessory() {
		try {
			if (mFileDescriptor != null) {
				mFileDescriptor.close();
			}// if
		} catch (IOException e) {
		} finally {
			mFileDescriptor = null;
			mAccessory = null;
		}
	}// closeAccesory
	
	public static void openAccessory(UsbAccessory accessory) {
		mFileDescriptor = mUsbManager.openAccessory(accessory);
		if (mFileDescriptor != null) {
			mAccessory = accessory;
			FileDescriptor fd = mFileDescriptor.getFileDescriptor();
			mInputStream = new FileInputStream(fd);
			mOutputStream = new FileOutputStream(fd);
			Thread thread = new Thread(new Runnable() {

				public void run() {
					// TODO Auto-generated method stub
					int ret = 0;
					byte[] buffer = new byte[16384];

					while (ret >= 0) {
						try {
							ret = mInputStream.read(buffer);

						} catch (IOException e) {
							break;
						}// catch
					}// while
				}// run
			});
			thread.start();
		} else {
			// Accessoryが存在しない
		}
	}// openAccessory

	public static final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (ACTION_USB_PERMISSION.equals(action)) {
				synchronized (this) {
					UsbAccessory accessory = (UsbAccessory) intent
							.getParcelableExtra(UsbManager.EXTRA_ACCESSORY);
					if (intent.getBooleanExtra(
							UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
						Relay.openAccessory(accessory);
					} else {

					}// else
					mPermissionRequestPending = false;
				}
			} else if (UsbManager.ACTION_USB_ACCESSORY_DETACHED.equals(action)) {
				UsbAccessory accessory = (UsbAccessory) intent
						.getParcelableExtra(UsbManager.EXTRA_ACCESSORY);
				if (accessory != null && accessory.equals(mAccessory)) {
					Relay.closeAccessory();
				} //if(accessory ...)
			}// else if(UsbManager ...)
		}// onReceive
	};
	
	public static void setRelayOnResume(){
		UsbAccessory[] accessories = mUsbManager.getAccessoryList();
		UsbAccessory accessory = (accessories == null ? null : accessories[0]);
		if (accessory != null) {
			if (mUsbManager.hasPermission(accessory)) {
				Relay.openAccessory(accessory);
			} else {
				synchronized (Relay.mUsbReceiver) {
					if (!mPermissionRequestPending) {
						mUsbManager.requestPermission(accessory,
								mPermissionIntent);
						mPermissionRequestPending = true;
					}// if
				}// synchronized
			}// else
		} else {

		}// else
	}// setRelayOnResume
	
	public static void test(Context context){
		mUsbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
		mPermissionIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), 0);
		IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
		filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
		context.registerReceiver(mUsbReceiver, filter);
	}// test
	
	public static void unRegisterReceiver(Context context){
		context.unregisterReceiver(mUsbReceiver);
	}// unRegisterReceiver
}// Relay
