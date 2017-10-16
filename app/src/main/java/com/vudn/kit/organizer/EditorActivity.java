package com.vudn.kit.organizer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.vudn.kit.organizer.note.Note;

public class EditorActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText bodyEditText;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        bodyEditText = (TextInputEditText) findViewById(R.id.textInputEditText);

        findViewById(R.id.fab).setOnClickListener(this);

        getIntentData();
    }

    private void getIntentData() {
        position = getIntent().getIntExtra(MainActivity.POSITION, -1);
        final Note note = getIntent().getParcelableExtra(Note.class.getCanonicalName());
        if (note != null) {
            bodyEditText.setText(note.getBody());
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
        final String body = bodyEditText.getText().toString();
        final Note note = new Note(body);
        final Intent intent = new Intent();
        intent.putExtra(MainActivity.POSITION, position);
        intent.putExtra(Note.class.getCanonicalName(), note);
        setResult(RESULT_OK, intent);
        finish();
    }
}
