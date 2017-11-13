package com.vudn.kit.organizer.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;

import com.vudn.kit.organizer.note.Note;
import com.vudn.kit.organizer.note.NoteDBHelper;

import java.util.Calendar;

public class TimeDialogFragment extends DialogFragment {

    private static final String HOUR = "hour";
    private static final String MINUTE = "minute";

    private TimePicker timePicker;
    private OnTimeSelectedCallback timeSelectedCallback;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setView(getContentView(savedInstanceState))
                .setPositiveButton("Yes", clickListener)
                .setNegativeButton("No", clickListener)
                .create();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(HOUR, timePicker.getHour());
        outState.putInt(MINUTE, timePicker.getMinute());
    }

    public void setTimeSelectedCallback(OnTimeSelectedCallback timeSelectedCallback) {
        this.timeSelectedCallback = timeSelectedCallback;
    }

    private View getContentView(Bundle savedInstanceState) {
        timePicker = new TimePicker(getActivity());
        timePicker.setIs24HourView(android.text.format.DateFormat.is24HourFormat(getActivity()));
        setTimeTarget(savedInstanceState);
        return timePicker;
    }

    private void setTimeTarget(Bundle savedInstanceState) {
        final String stringExtra = getArguments().getString(NoteDBHelper.TIME_TARGET);
        final Note.TimeTarget timeTarget = Note.TimeTarget.valueOf(stringExtra);
        if (!timeTarget.equals(Note.TimeTarget.NONE)) {
            final int hour;
            final int minute;
            if (savedInstanceState != null) {
                hour = savedInstanceState.getInt(HOUR);
                minute = savedInstanceState.getInt(MINUTE);
            } else {
                final Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(getArguments().getLong(NoteDBHelper.DATE_TARGET));
                hour = calendar.get(Calendar.HOUR_OF_DAY);
                minute = calendar.get(Calendar.MINUTE);
            }
            timePicker.setHour(hour);
            timePicker.setMinute(minute);
        }
    }

    private DialogInterface.OnClickListener clickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            final int hour, minute;
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    hour = timePicker.getHour();
                    minute = timePicker.getMinute();
                    break;
                default:
                    hour = minute = Note.DEFAULT_DATE_TARGET;
            }
            if (timeSelectedCallback != null) {
                timeSelectedCallback.onTimeSelected(hour, minute);
            }
        }
    };

    public interface OnTimeSelectedCallback {
        void onTimeSelected(int hour, int minute);
    }
}
