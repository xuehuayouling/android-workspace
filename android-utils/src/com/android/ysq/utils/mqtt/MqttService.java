package com.android.ysq.utils.mqtt;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

import android.app.Application;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

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
			WifiManager wifi = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
			WifiInfo info = wifi.getConnectionInfo();
			mDeviceId = String.format("an_%s", info.getMacAddress());
			mClient = new MqttAndroidClient(mContext, serverURI, mDeviceId);
			inited = true;
		} else {
			throw new ExceptionInInitializerError("MqttService已经初始化过，不能再次初始化");
		}
	}

	public MqttAndroidClient getClient() {
		return mClient;
	}

	public void startService(MqttConnectOptions conOpt, IMqttActionListener listener) {
		if (!mClient.isConnected()) {
			try {
				mClient.connect(conOpt, null, listener);
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
