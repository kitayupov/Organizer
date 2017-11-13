package com.vudn.kit.organizer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.vudn.kit.organizer.fragment.DateDialogFragment;
import com.vudn.kit.organizer.fragment.TimeDialogFragment;
import com.vudn.kit.organizer.note.Note;
import com.vudn.kit.organizer.note.NoteDBHelper;
import com.vudn.kit.organizer.util.DateUtil;

public class EditorActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = EditorActivity.class.getSimpleName();

    private EditText nameEditText;
    private EditText bodyEditText;
    private TextView dateTargetTextView;
    private TextView timeTargetTextView;
    private CheckBox completedCheckBox;
    private View calendarButton;
    private View timePickerButton;
    private View dateLayout;
    private int position;
    private Note note;

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
        dateTargetTextView = (TextView) findViewById(R.id.dateTargetTextView);
        timeTargetTextView = (TextView) findViewById(R.id.timeTargetTextView);
        completedCheckBox = ((CheckBox) findViewById(R.id.completedCheckBox));
        calendarButton = findViewById(R.id.calendarButton);
        timePickerButton = findViewById(R.id.timePickerButton);
        dateLayout = findViewById(R.id.dateLayout);
    }

    private void setClickListeners() {
        findViewById(R.id.fab).setOnClickListener(this);
        dateTargetTextView.setOnClickListener(this);
        calendarButton.setOnClickListener(this);
        timeTargetTextView.setOnClickListener(this);
        timePickerButton.setOnClickListener(this);
    }

    private void getIntentData() {
        position = getIntent().getIntExtra(MainActivity.POSITION, MainActivity.DEFAULT_POSITION);
        note = getIntent().getParcelableExtra(Note.class.getCanonicalName());
        setNoteContent();
    }

    private void setNoteContent() {
        nameEditText.setText(note.getName());
        bodyEditText.setText(note.getBody());
        completedCheckBox.setChecked(note.isCompleted());
        setDateTarget(note.getDateTarget());
    }

    private void setDateTarget(long dateTarget) {
        final boolean isDateEmpty = (dateTarget == Note.DEFAULT_DATE_TARGET);
        if (!isDateEmpty) {
            dateTargetTextView.setText(DateUtil.getDateString(dateTarget));
        }
        calendarButton.setVisibility(getVisibility(isDateEmpty));
        dateLayout.setVisibility(getVisibility(!isDateEmpty));
    }

    private int getVisibility(boolean visible) {
        return visible ? View.VISIBLE : View.GONE;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                sendResult();
                break;
            case R.id.calendarButton:
            case R.id.dateTargetTextView:
                startDateSelectDialog();
                break;
            case R.id.timePickerButton:
            case R.id.timeTargetTextView:
                startTimeSelectDialog();
                break;
            default:
                Log.e(TAG, "Not implemented yet: " + view.getId());
        }
    }

    private void startDateSelectDialog() {
        final DateDialogFragment dateDialogFragment = new DateDialogFragment();
        final Bundle bundle = new Bundle();
        bundle.putLong(NoteDBHelper.DATE_TARGET, note.getDateTarget());
        dateDialogFragment.setArguments(bundle);
        dateDialogFragment.setDateSelectedCallback(new DateDialogFragment.OnDateSelectedCallback() {
            @Override
            public void onDateSelected(long date) {
                note.setDateTarget(date);
                setDateTarget(date);
            }
        });
        dateDialogFragment.show(getFragmentManager(), "Date");
    }

    private void startTimeSelectDialog() {
        final TimeDialogFragment timeDialogFragment = new TimeDialogFragment();
        timeDialogFragment.show(getFragmentManager(), "Time");
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
        final Note copy = note.copy();
        copy.setName(nameEditText.getText().toString());
        copy.setBody(bodyEditText.getText().toString());
        copy.setCompleted(completedCheckBox.isChecked());
        if (!note.equals(copy)) {
            return copy;
        } else {
            return note;
        }
    }
}
