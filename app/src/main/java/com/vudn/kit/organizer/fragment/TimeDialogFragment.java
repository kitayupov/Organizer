package com.vudn.kit.organizer.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;

import com.vudn.kit.organizer.R;

public class TimeDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setView(getContentView())
                .setPositiveButton("Yes", null)
                .setNegativeButton("No", null)
                .create();
    }

    private View getContentView() {
        return getActivity().getLayoutInflater().inflate(R.layout.dialog_time, null);
    }
}
