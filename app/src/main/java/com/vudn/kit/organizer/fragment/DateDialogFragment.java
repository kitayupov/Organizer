package com.vudn.kit.organizer.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;

import com.vudn.kit.organizer.R;
import com.vudn.kit.organizer.note.Note;
import com.vudn.kit.organizer.note.NoteDBHelper;

public class DateDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setView(getContentView())
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", null)
                .create();
    }

    private View getContentView() {
        final View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_date, null);
        long dateTarget = getArguments().getLong(NoteDBHelper.DATE_TARGET);
        if (dateTarget != Note.DEFAULT_DATE_TARGET) {
            final CalendarView calendarView = view.findViewById(R.id.calendarView);
            calendarView.setDate(dateTarget);
        }
        return view;
    }
}
