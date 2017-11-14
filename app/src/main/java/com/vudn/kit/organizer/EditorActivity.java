package com.vudn.kit.organizer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.vudn.kit.organizer.fragment.DateDialogFragment;
import com.vudn.kit.organizer.fragment.TimeDialogFragment;
import com.vudn.kit.organizer.note.Note;
import com.vudn.kit.organizer.note.NoteDBHelper;
import com.vudn.kit.organizer.view.DateTimeTextView;

import java.util.Calendar;

public class EditorActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = EditorActivity.class.getSimpleName();

    private EditText nameEditText;
    private EditText bodyEditText;
    private DateTimeTextView dateTimeTextView;
    private CheckBox completedCheckBox;
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
        dateTimeTextView = (DateTimeTextView) findViewById(R.id.dateTimeTextView);
        completedCheckBox = ((CheckBox) findViewById(R.id.completedCheckBox));
        timePickerButton = findViewById(R.id.timePickerButton);
        dateLayout = findViewById(R.id.dateLayout);
    }

    private void setClickListeners() {
        findViewById(R.id.fab).setOnClickListener(this);
        findViewById(R.id.calendarButton).setOnClickListener(this);
        timePickerButton.setOnClickListener(this);
        dateTimeTextView.setOnClickListener(this);
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
        setDateTimeTarget();
    }

    private void setDateTimeTarget() {
        final long dateTarget = note.getDateTarget();
        final boolean isDateEmpty = (dateTarget == Note.DEFAULT_DATE_TARGET);
        if (!isDateEmpty) {
            dateTimeTextView.setTarget(note.getTimeTarget(), dateTarget);
        }
        timePickerButton.setVisibility(getVisibility(!isDateEmpty));
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
            case DateTimeTextView.dateTextViewId:
                startDateSelectDialog();
                break;
            case R.id.timePickerButton:
            case DateTimeTextView.timeTextViewId:
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
            public void onDateSelected(int day, int month, int year) {
                if (day != Note.DEFAULT_DATE_TARGET
                        && month != Note.DEFAULT_DATE_TARGET
                        && year != Note.DEFAULT_DATE_TARGET) {
                    final Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(note.getDateTarget());
                    calendar.set(Calendar.DAY_OF_MONTH, day);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.YEAR, year);
                    note.setDateTarget(calendar.getTimeInMillis());
                } else {
                    note.setDateTarget(Note.DEFAULT_DATE_TARGET);
                }
                setDateTimeTarget();
            }
        });
        dateDialogFragment.show(getFragmentManager(), "Date");
    }

    private void startTimeSelectDialog() {
        final TimeDialogFragment timeDialogFragment = new TimeDialogFragment();
        final Bundle bundle = new Bundle();
        bundle.putString(NoteDBHelper.TIME_TARGET, note.getTimeTarget().name());
        bundle.putLong(NoteDBHelper.DATE_TARGET, note.getDateTarget());
        timeDialogFragment.setArguments(bundle);
        timeDialogFragment.setTimeSelectedCallback(new TimeDialogFragment.OnTimeSelectedCallback() {
            @Override
            public void onTimeSelected(int hour, int minute) {
                if (hour != Note.DEFAULT_DATE_TARGET && minute != Note.DEFAULT_DATE_TARGET) {
                    final Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(note.getDateTarget());
                    calendar.set(Calendar.HOUR_OF_DAY, hour);
                    calendar.set(Calendar.MINUTE, minute);
                    note.setDateTarget(calendar.getTimeInMillis());
                    note.setTimeTarget(Note.TimeTarget.SINGLE);
                } else {
                    note.setTimeTarget(Note.TimeTarget.NONE);
                }
                setDateTimeTarget();
            }
        });
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
