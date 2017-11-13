package com.vudn.kit.organizer.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;

import com.vudn.kit.organizer.note.Note;
import com.vudn.kit.organizer.note.NoteDBHelper;

import java.util.Calendar;

public class DateDialogFragment extends DialogFragment {

    private CalendarView calendarView;
    private OnDateSelectedCallback dateSelectedCallback;

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
        outState.putLong(NoteDBHelper.DATE_TARGET, calendarView.getDate());
    }

    public void setDateSelectedCallback(OnDateSelectedCallback dateSelectedCallback) {
        this.dateSelectedCallback = dateSelectedCallback;
    }

    private View getContentView(Bundle savedInstanceState) {
        calendarView = new CalendarView(getActivity());
        setDateTarget(savedInstanceState);
        return calendarView;
    }

    private void setDateTarget(Bundle savedInstanceState) {
        final long dateTarget;
        if (savedInstanceState == null) {
            dateTarget = getArguments().getLong(NoteDBHelper.DATE_TARGET);
        } else {
            dateTarget = savedInstanceState.getLong(NoteDBHelper.DATE_TARGET);
        }
        if (dateTarget != Note.DEFAULT_DATE_TARGET) {
            calendarView.setDate(dateTarget);
        }
    }

    private DialogInterface.OnClickListener clickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            final int day;
            final int month;
            final int year;
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    final Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(calendarView.getDate());
                    day = calendar.get(Calendar.DAY_OF_MONTH);
                    month = calendar.get(Calendar.MONTH);
                    year = calendar.get(Calendar.YEAR);
                    break;
                default:
                    day = month = year = Note.DEFAULT_DATE_TARGET;
            }
            if (dateSelectedCallback != null) {
                dateSelectedCallback.onDateSelected(day, month, year);
            }
        }
    };

    public interface OnDateSelectedCallback {
        void onDateSelected(int day, int month, int year);
    }
}
