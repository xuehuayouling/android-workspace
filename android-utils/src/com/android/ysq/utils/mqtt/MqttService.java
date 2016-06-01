package com.android.ysq.utils.mqtt;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttException;

import android.app.Application;
import android.content.Context;
import android.telephony.TelephonyManager;

public class MqttService {

	private static MqttService mSelf;

	private Context mContext;
	private String mDeviceId;
	private MqttAndroidClient mClient;
	private boolean inited = false;

	private MqttService() {
	}

	public synchronized static MqttService getInstance() {
		if (mSelf == null) {
			mSelf = new MqttService();
		}
		return mSelf;
	}

	public synchronized void init(Application context, String serverURI) throws ExceptionInInitializerError {
		if (!inited) {
			mContext = context;
			TelephonyManager TelephonyMgr = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
			String szImei = TelephonyMgr.getDeviceId();
			mDeviceId = String.format("an_%s", szImei);
			mClient = new MqttAndroidClient(mContext, serverURI, mDeviceId);
			inited = true;
		} else {
			throw new ExceptionInInitializerError("MqttService已经初始化过，不能再次初始化");
		}
	}

	public MqttAndroidClient getClient() {
		return mClient;
	}

	public void startService() {
		if (!mClient.isConnected()) {
			try {
				mClient.connect(null, null, null);
			} catch (MqttException e) {
				e.printStackTrace();
			}
		}
	}

	public void stopService() {
		if (mClient.isConnected()) {
			try {
				mClient.disconnect();
			} catch (MqttException e) {
				e.printStackTrace();
			}
		}
	}
}
