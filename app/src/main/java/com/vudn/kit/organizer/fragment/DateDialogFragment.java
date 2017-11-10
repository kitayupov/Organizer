package com.vudn.kit.organizer.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;

import com.vudn.kit.organizer.R;
import com.vudn.kit.organizer.note.Note;
import com.vudn.kit.organizer.note.NoteDBHelper;

public class DateDialogFragment extends DialogFragment {

    private CalendarView calendarView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setView(getContentView())
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", clickListener)
                .create();
    }

    private View getContentView() {
        final View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_date, null);
        calendarView = view.findViewById(R.id.calendarView);
        setDateTarget();
        return view;
    }

    private void setDateTarget() {
        final long dateTarget = getArguments().getLong(NoteDBHelper.DATE_TARGET);
        if (dateTarget != Note.DEFAULT_DATE_TARGET) {
            calendarView.setDate(dateTarget);
        }
    }

    private DialogInterface.OnClickListener clickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            final long selectedDate = calendarView.getDate();
            System.out.println(selectedDate);
        }
    };
}
