package com.vudn.kit.organizer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class EditorActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        findViewById(R.id.fab).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                finish();
                break;
        }
    }
}
