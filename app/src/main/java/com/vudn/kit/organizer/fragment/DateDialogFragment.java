package com.vudn.kit.organizer.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;

import com.vudn.kit.organizer.R;

public class DateDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_date, null);
        builder.setView(view);
        builder.setNegativeButton("No", null);
        builder.setPositiveButton("Yes", null);
        return builder.create();
    }
}
