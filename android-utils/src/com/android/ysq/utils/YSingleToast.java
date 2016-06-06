package com.android.ysq.utils;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.widget.Toast;

public class YSingleToast {

	private Toast mToast;
	final WeakReference<Activity> mWeakReference;
	private static YSingleToast mSelf;
	private YSingleToast(Activity context) {
		mWeakReference = new WeakReference<Activity>(context);
	}
	
	public static synchronized YSingleToast getInstance(Activity context) {
		if (mSelf == null) {
			mSelf = new YSingleToast(context);
		} else if (mSelf.mWeakReference.get() != context) {
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
		if (mWeakReference.get() != null) {
			this.show(mWeakReference.get().getString(resId), duration);
		}
	}

	public void show(CharSequence text, int duration) {
		dismiss();
		if (mWeakReference.get() != null && !mWeakReference.get().isFinishing()) {
			mToast = Toast.makeText(mWeakReference.get(), text, duration);
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
