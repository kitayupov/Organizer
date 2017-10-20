package com.vudn.kit.organizer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.vudn.kit.organizer.note.Note;

import java.util.Date;

public class EditorActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText nameEditText;
    private EditText bodyEditText;
    private CheckBox completedCheckBox;
    private int position;
    private long timeCreated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        initControls();
        setClickListeners();
        getIntentData();
    }

    private void initControls() {
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        bodyEditText = (EditText) findViewById(R.id.bodyEditText);
        completedCheckBox = ((CheckBox) findViewById(R.id.completedCheckBox));
    }

    private void setClickListeners() {
        findViewById(R.id.fab).setOnClickListener(this);
    }

    private void getIntentData() {
        position = getIntent().getIntExtra(MainActivity.POSITION, MainActivity.DEFAULT_POSITION);
        final Note note = getIntent().getParcelableExtra(Note.class.getCanonicalName());
        if (note != null) {
            nameEditText.setText(note.getName());
            bodyEditText.setText(note.getBody());
            completedCheckBox.setChecked(note.isCompleted());
            timeCreated = note.getTimeCreated();
        } else {
            timeCreated = new Date().getTime();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                sendResult();
                break;
        }
    }

    private void sendResult() {
        final Intent intent = new Intent();
        intent.putExtra(MainActivity.POSITION, position);
        intent.putExtra(Note.class.getCanonicalName(), getNote());
        setResult(RESULT_OK, intent);
        finish();
    }

    @NonNull
    private Note getNote() {
        final String name = nameEditText.getText().toString();
        final String body = bodyEditText.getText().toString();
        final boolean completed = completedCheckBox.isChecked();
        return new Note(name, body, timeCreated, new Date().getTime(), completed);
    }
}
