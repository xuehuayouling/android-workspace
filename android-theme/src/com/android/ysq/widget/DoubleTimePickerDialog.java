package com.android.ysq.widget;

import com.android.ysq.theme.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

public class DoubleTimePickerDialog extends AlertDialog implements OnClickListener {

	private static final String START_HOUR_OF_DAY = "startHourOfDay";
	private static final String START_MINUTE = "startMinute";
	private static final String END_HOUR_OF_DAY = "endHourOfDay";
	private static final String END_MINUTE = "endMinute";
	
	private final TimePicker mTimePickerStart;
	private final TimePicker mTimePickerEnd;
	private final OnTimeSetListener mCallBack;

	/**
	 * The callback used to indicate the user is done filling in the time.
	 */
	public interface OnTimeSetListener {

		/**
		 * @param startTimePicker
		 * @param startHourOfDay
		 * @param startMinute
		 * @param endTimePicker
		 * @param endHourOfDay
		 * @param endMinute
		 */
		void onTimeSet(TimePicker startTimePicker, int startHourOfDay, int startMinute, TimePicker endTimePicker,
				int endHourOfDay, int endMinute);
	}

	/**
	 * @param context
	 * @param theme
	 * @param callBack
	 * @param startHourOfDay
	 * @param startMinute
	 * @param endHourOfDay
	 * @param endMinute
	 * @param is24HourView
	 */
	public DoubleTimePickerDialog(Context context, int theme, OnTimeSetListener callBack, int startHourOfDay,
			int startMinute, int endHourOfDay, int endMinute, Boolean is24HourView) {
		super(context, theme);

		mCallBack = callBack;

		Context themeContext = getContext();
		setButton(BUTTON_POSITIVE, context.getText(android.R.string.ok), this);
		setButton(BUTTON_NEGATIVE, context.getText(android.R.string.cancel), this);

		setIcon(0);

		LayoutInflater inflater = (LayoutInflater) themeContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.time_double_picker_dialog, null);
		setView(view);
		mTimePickerStart = (TimePicker) view.findViewById(R.id.tp_start);
		mTimePickerEnd = (TimePicker) view.findViewById(R.id.tp_end);
		mTimePickerStart.setIs24HourView(is24HourView);
		mTimePickerEnd.setIs24HourView(is24HourView);
		mTimePickerStart.setCurrentHour(startHourOfDay);
		mTimePickerStart.setCurrentMinute(startMinute);
		mTimePickerEnd.setCurrentHour(endHourOfDay);
		mTimePickerEnd.setCurrentMinute(endMinute);
	}

	public void onClick(DialogInterface dialog, int which) {
		if (which == BUTTON_POSITIVE)
			tryNotifyDateSet();
	}

	private void tryNotifyDateSet() {
		if (mCallBack != null) {
			mTimePickerStart.clearFocus();
			mTimePickerEnd.clearFocus();
			mCallBack.onTimeSet(mTimePickerStart, mTimePickerStart.getCurrentHour(),
					mTimePickerStart.getCurrentMinute(), mTimePickerEnd, mTimePickerEnd.getCurrentHour(),
					mTimePickerEnd.getCurrentMinute());
		}
	}

	@Override
	public Bundle onSaveInstanceState() {
		Bundle state = super.onSaveInstanceState();
		state.putInt(START_HOUR_OF_DAY, mTimePickerStart.getCurrentHour());
		state.putInt(START_MINUTE, mTimePickerStart.getCurrentMinute());
		state.putInt(END_HOUR_OF_DAY, mTimePickerEnd.getCurrentHour());
		state.putInt(END_MINUTE, mTimePickerEnd.getCurrentMinute());
		return state;
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mTimePickerStart.setCurrentHour(savedInstanceState.getInt(START_HOUR_OF_DAY));
		mTimePickerStart.setCurrentMinute(savedInstanceState.getInt(START_MINUTE));
		mTimePickerEnd.setCurrentHour(savedInstanceState.getInt(END_HOUR_OF_DAY));
		mTimePickerEnd.setCurrentMinute(savedInstanceState.getInt(END_MINUTE));
	}
}
