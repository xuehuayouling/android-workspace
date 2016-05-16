package com.android.ysq.utils;

import android.app.Activity;
import android.widget.Toast;

public class YSingleToast {

	private Toast mToast;
	final Activity mContext;
	private static YSingleToast mSelf;
	private YSingleToast(Activity context) {
		mContext = context;
	}
	
	public static synchronized YSingleToast getInstance(Activity context) {
		if (mSelf == null) {
			mSelf = new YSingleToast(context);
		} else if (mSelf.mContext != context) {
			mSelf.dismiss();
			mSelf = new YSingleToast(context);
		}
		return mSelf;
	}

	public void show(int resId) {
		this.show(resId, Toast.LENGTH_SHORT);
	}

	public void show(CharSequence text) {
		this.show(text, Toast.LENGTH_SHORT);
	}

	public void show(int resId, int duration) {
		this.show(mContext.getString(resId), duration);
	}

	public void show(CharSequence text, int duration) {
		dismiss();
		if (mContext != null && !mContext.isFinishing()) {
			mToast = Toast.makeText(mContext, text, duration);
			mToast.show();
		}
	}

	public void dismiss() {
		if (mToast != null) {
			mToast.cancel();
			mToast = null;
		}
	}
}
