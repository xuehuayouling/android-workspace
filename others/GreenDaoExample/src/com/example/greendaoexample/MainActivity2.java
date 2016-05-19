package com.example.greendaoexample;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity2 extends Activity {
	private NoteDao mNoteDao;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Note note = new Note(null, "test1", null, null);
		mNoteDao = ((MyApplication) getApplication()).getDaoSession().getNoteDao();
		mNoteDao.insert(note);
	}

}
