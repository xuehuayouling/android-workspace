package com.android.ysq.widget;

import java.io.File;
import java.io.IOException;

import com.android.ysq.theme.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RecordView extends RelativeLayout {

	private static final File SAVED_RECORD_DIR_PATH = Environment
			.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
	private View mLayout;
	private TextView mTvCancel;
	private ImageButton mImgBtnStartRecord;
	private ImageButton mImgBtnStopRecord;
	private TextView mTvStatus;
	private RecordListener mListener;
	private MediaRecorder mMediaRecorder;
	protected String currentRecordPath;
	private Handler mTimeHandler = new Handler() {
		public void handleMessage(Message msg) {
			long time = SystemClock.elapsedRealtime() - msg.getData().getLong("start_time");
			mTvStatus.setText("" + time);
			Message msg1 = mTimeHandler.obtainMessage(0);
			msg1.setData(msg.getData());
			mTimeHandler.sendMessageDelayed(msg1, 1000);
		};
	};

	public interface RecordListener {
		public void onStartRecording();

		public void onStopRecording(String path);

		public void onCancel(boolean isRecording);
	}

	public RecordView(Context context) {
		super(context);
		initView(context);
	}

	public RecordView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public RecordView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView(context);
	}

	private void initView(Context context) {
		mLayout = LayoutInflater.from(context).inflate(R.layout.make_record_layout, null);
		mLayout.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		addView(mLayout);
		mTvCancel = (TextView) findViewById(R.id.tv_cancel);
		mTvCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				AlertDialog alertDialog = new AlertDialog.Builder(getContext(), AlertDialog.THEME_HOLO_LIGHT)
						.setTitle(getContext().getString(R.string.are_you_sure_abandon_current_record))
						.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (mListener != null) {
							mListener.onCancel(mMediaRecorder != null);
						}
						reset();
					}
				}).setNegativeButton(android.R.string.cancel, null).create();
				alertDialog.show();
			}
		});
		mImgBtnStartRecord = (ImageButton) findViewById(R.id.imgbtn_start_record);
		mImgBtnStopRecord = (ImageButton) findViewById(R.id.imgbtn_stop_record);
		mTvStatus = (TextView) findViewById(R.id.tv_status);
		mImgBtnStartRecord.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String state = Environment.getExternalStorageState();
				if (state.equals(Environment.MEDIA_MOUNTED)) {
					File dir = SAVED_RECORD_DIR_PATH;
					if (!dir.exists()) {
						dir.mkdirs();
					}
					currentRecordPath = dir.getPath() + "/" + System.currentTimeMillis() + ".mp3";
					initMediaRecorder();
					mMediaRecorder.setOutputFile(currentRecordPath);
					try {
						mMediaRecorder.prepare();
					} catch (IOException e) {
						e.printStackTrace();
					}
					Message msg = mTimeHandler.obtainMessage(0);
					Bundle data = new Bundle();
					data.putLong("start_time", SystemClock.elapsedRealtime());
					msg.setData(data);
					mTimeHandler.sendMessage(msg);
					mMediaRecorder.start();
					if (mListener != null) {
						mListener.onStartRecording();
					}
					mImgBtnStartRecord.setVisibility(View.GONE);
					mImgBtnStopRecord.setVisibility(View.VISIBLE);
				} else {
					Toast.makeText(getContext(), "请确认已经插入SD卡", Toast.LENGTH_LONG).show();
				}
			}
		});
		mImgBtnStopRecord.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mListener != null) {
					mListener.onStopRecording(currentRecordPath);
				}
				reset();
			}

		});
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		reset();
	}

	private void reset() {
		if (mMediaRecorder != null) {
			mMediaRecorder.stop();
			mMediaRecorder.release();
			currentRecordPath = null;
			mMediaRecorder = null;
		}
		mTimeHandler.removeMessages(0);
		mImgBtnStartRecord.setVisibility(View.VISIBLE);
		mImgBtnStopRecord.setVisibility(View.GONE);
		mTvStatus.setText(getContext().getText(R.string.prepare_record));
	}

	public void setRecordListenter(RecordListener recordListener) {
		this.mListener = recordListener;
	}

	private void initMediaRecorder() {
		mMediaRecorder = new MediaRecorder();
		mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
	}

}
