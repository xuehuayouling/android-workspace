package com.example.greendaoexample;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	private NoteDao mNoteDao;
	private TextView mTvTitle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mTvTitle = (TextView) findViewById(R.id.tv_title);
		Button button = (Button) findViewById(R.id.btn_goto_next);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, MainActivity2.class));
			}
		});
		mNoteDao = ((MyApplication) getApplication()).getDaoSession().getNoteDao();
		mNoteDao.registerObserver(new DataSetObserver() {
			@Override
			public void onChanged() {
				List<Note> list = mNoteDao.loadAll();
				mTvTitle.setText(list.get(list.size() - 1).getText());
			}
			
			@Override
			public void onInvalidated() {
				super.onInvalidated();
			}
		});
		Note note = new Note(null, "test", null, null);
		mNoteDao.insert(note);
	}

}
