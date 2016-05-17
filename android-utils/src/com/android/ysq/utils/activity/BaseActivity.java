package com.android.ysq.utils.activity;

import com.android.ysq.utils.YSingleProgressDialog;
import com.android.ysq.utils.YSingleToast;

import android.app.Activity;
import android.os.Bundle;

public class BaseActivity extends Activity {

	/**
	 * 用于显示Toast，立即取消上一个显示新的。
	 */
	protected YSingleToast mToast;

	/**
	 * 显示模态圆形进度条
	 */
	protected YSingleProgressDialog mProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mToast = YSingleToast.getInstance(this);
		mProgressDialog = YSingleProgressDialog.getInstance(this);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		mToast.dismiss();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mToast = null;
		mProgressDialog.dismiss();
		mProgressDialog = null;
	}
}
