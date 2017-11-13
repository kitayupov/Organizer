package com.vudn.kit.organizer.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;

import com.vudn.kit.organizer.R;
import com.vudn.kit.organizer.note.Note;
import com.vudn.kit.organizer.note.NoteDBHelper;

import java.util.Calendar;

public class TimeDialogFragment extends DialogFragment {

    private TimePicker timePicker;
    private OnTimeSelectedCallback timeSelectedCallback;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setView(getContentView())
                .setPositiveButton("Yes", clickListener)
                .setNegativeButton("No", clickListener)
                .create();
    }

    public void setTimeSelectedCallback(OnTimeSelectedCallback timeSelectedCallback) {
        this.timeSelectedCallback = timeSelectedCallback;
    }

    private View getContentView() {
        final View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_time, null);
        timePicker = view.findViewById(R.id.timePicker);
        setTimeTarget();
        return view;
    }

    private void setTimeTarget() {
        final String stringExtra = getArguments().getString(NoteDBHelper.TIME_TARGET);
        final Note.TimeTarget timeTarget = Note.TimeTarget.valueOf(stringExtra);
        final long dateTarget = getArguments().getLong(NoteDBHelper.DATE_TARGET);
        if (timeTarget.equals(Note.TimeTarget.NONE)) {
            final Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(dateTarget);
            timePicker.setHour(calendar.get(Calendar.HOUR));
            timePicker.setMinute(calendar.get(Calendar.MINUTE));
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
                    hour = Note.DEFAULT_DATE_TARGET;
                    minute = Note.DEFAULT_DATE_TARGET;
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
