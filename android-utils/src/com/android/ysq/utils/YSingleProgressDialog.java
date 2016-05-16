package com.android.ysq.utils;

import android.app.Activity;
import android.app.ProgressDialog;

public class YSingleProgressDialog {

	private ProgressDialog mProgressDialog;
	final Activity mContext;
	private static YSingleProgressDialog mSelf;
	private YSingleProgressDialog(Activity context) {
		mContext = context;
	}
	
	public static synchronized YSingleProgressDialog getInstance(Activity context) {
		if (mSelf == null) {
			mSelf = new YSingleProgressDialog(context);
		} else if (mSelf.mContext != context) {
			mSelf.dismiss();
			mSelf = new YSingleProgressDialog(context);
		}
		return mSelf;
	}

	public void show(String title, CharSequence content) {
		dismiss();
		mProgressDialog = ProgressDialog.show(mContext, title, content, true);
	}
	
	public void show() {
		show(null, null);
	}

	public void dismiss() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.cancel();
			mProgressDialog = null;
		}
	}
}
