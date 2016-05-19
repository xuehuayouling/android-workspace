package com.example.greendaoexample;

import com.android.ysq.utils.application.BaseApplication;

import android.database.sqlite.SQLiteDatabase;

public class MyApplication extends BaseApplication {
	private SQLiteDatabase db;
	private DaoMaster daoMaster;
	private DaoSession daoSession;

	@Override
	public void onCreate() {
		super.onCreate();
		DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "notes-db", null);
		db = helper.getWritableDatabase();
		// 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
		daoMaster = new DaoMaster(db);
		daoSession = daoMaster.newSession();
	}

	public DaoSession getDaoSession() {
		return daoSession;
	}

}
