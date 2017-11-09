package com.vudn.kit.organizer.view;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.vudn.kit.organizer.R;
import com.vudn.kit.organizer.note.Note;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

public class Footer implements View.OnClickListener, KeyboardVisibilityEventListener {

    private static final String TAG = Footer.class.getSimpleName();

    private final Activity activity;
    private final EditText editText;
    private InsertCallback insertCallback;

    public Footer(Activity activity) {
        this.activity = activity;
        editText = activity.findViewById(R.id.editText);
        activity.findViewById(R.id.insertButton).setOnClickListener(this);
        KeyboardVisibilityEvent.registerEventListener(activity, this);
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
        final String name = editText.getText().toString().trim();
        if (insertCallback != null && !name.equals("")) {
            insertCallback.perform(new Note(name));
        }
        clearEditText();
    }

    private void clearEditText() {
        editText.setText(null);
        editText.clearFocus();
        ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    @Override
    public void onVisibilityChanged(boolean isOpen) {
        if (!isOpen) {
            clearEditText();
        }
    }

    public interface InsertCallback {
        void perform(Note note);
    }
}
