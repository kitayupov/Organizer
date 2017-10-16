package com.vudn.kit.organizer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.vudn.kit.organizer.note.NoteDBHelper;

public class EditorActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText bodyEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        bodyEditText = (TextInputEditText) findViewById(R.id.textInputEditText);

        findViewById(R.id.fab).setOnClickListener(this);
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
        intent.putExtra(NoteDBHelper.BODY, bodyEditText.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }
}
