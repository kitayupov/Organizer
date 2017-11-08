package com.vudn.kit.organizer.view;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.vudn.kit.organizer.R;
import com.vudn.kit.organizer.note.Note;

import java.util.Date;

public class Footer implements View.OnClickListener {

    private static final String TAG = Footer.class.getSimpleName();

    private final EditText editText;
    private InsertCallback insertCallback;

    public Footer(Activity activity) {
        editText = activity.findViewById(R.id.editText);
        activity.findViewById(R.id.insertButton).setOnClickListener(this);
    }

    public void setInsertCallback(InsertCallback insertCallback) {
        this.insertCallback = insertCallback;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.insertButton:
                insertNote();
                break;
            default:
                Log.e(TAG, "Not implemented yet: " + view.getId());
        }
    }

    private void insertNote() {
        if (insertCallback != null) {
            final long currentTime = new Date().getTime();
            final String name = editText.getText().toString();
            insertCallback.perform(new Note(name, "", currentTime, currentTime, false));
        }
        editText.setText(null);
        editText.clearFocus();
    }

    public interface InsertCallback {
        void perform(Note note);
    }
}
