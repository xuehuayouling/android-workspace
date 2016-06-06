package com.android.ysq.utils;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.app.ProgressDialog;

public class YSingleProgressDialog {

	private ProgressDialog mProgressDialog;
	final WeakReference<Activity> mContext;
	private static YSingleProgressDialog mSelf;
	private YSingleProgressDialog(Activity context) {
		mContext = new WeakReference<Activity>(context);
	}
	
	public static synchronized YSingleProgressDialog getInstance(Activity context) {
		if (mSelf == null) {
			mSelf = new YSingleProgressDialog(context);
		} else if (mSelf.mContext.get() != context) {
			mSelf.dismiss();
			mSelf = new YSingleProgressDialog(context);
		}
		return mSelf;
	}

	public void show(String title, CharSequence content) {
		dismiss();
		if (mContext.get() != null) {
			mProgressDialog = ProgressDialog.show(mContext.get(), title, content, true);
		}
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
