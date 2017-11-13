package com.vudn.kit.organizer;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.vudn.kit.organizer.note.Note;
import com.vudn.kit.organizer.note.NoteDBHelper;
import com.vudn.kit.organizer.note.RecyclerAdapter;
import com.vudn.kit.organizer.view.Footer;
import com.vudn.kit.organizer.view.SpacesItemDecoration;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener, View.OnLongClickListener, ActionMode.Callback,
        RecyclerAdapter.OnCompletedStateChangeListener, RecyclerAdapter.OnEditButtonClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    public static final String POSITION = "position";
    public static final int DEFAULT_POSITION = -1;
    public static final int REQUEST_CODE = 200;

    private NoteDBHelper dbHelper;
    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    private ActionMode actionMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();
        initNoteList();
        initListView();
        readDatabase();
        initFooter();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initNoteList() {
        recyclerAdapter = new RecyclerAdapter();
        recyclerAdapter.setOnClickListener(this);
        recyclerAdapter.setOnLongClickListener(this);
        recyclerAdapter.setOnCompletedStateChangeListener(this);
        recyclerAdapter.setOnEditButtonClickListener(this);
    }

    private void initListView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new SpacesItemDecoration(16));
        recyclerView.setAdapter(recyclerAdapter);
    }

    private void readDatabase() {
        dbHelper = new NoteDBHelper(this);
        final SQLiteDatabase database = dbHelper.getReadableDatabase();
        final Cursor cursor = database.query(NoteDBHelper.TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            final int nameIndex = cursor.getColumnIndex(NoteDBHelper.NAME);
            final int bodyIndex = cursor.getColumnIndex(NoteDBHelper.BODY);
            final int dateTargetIndex = cursor.getColumnIndex(NoteDBHelper.DATE_TARGET);
            final int timeTargetIndex = cursor.getColumnIndex(NoteDBHelper.TIME_TARGET);
            final int createdIndex = cursor.getColumnIndex(NoteDBHelper.TIME_CREATED);
            final int updatedIndex = cursor.getColumnIndex(NoteDBHelper.TIME_UPDATED);
            final int completedIndex = cursor.getColumnIndex(NoteDBHelper.COMPLETED);
            do {
                final String name = cursor.getString(nameIndex);
                final String body = cursor.getString(bodyIndex);
                final long dateTarget = cursor.getLong(dateTargetIndex);
                final Note.TimeTarget timeTarget = Note.TimeTarget.valueOf(cursor.getString(timeTargetIndex));
                final long timeCreated = cursor.getLong(createdIndex);
                final long timeUpdated = cursor.getLong(updatedIndex);
                final boolean completed = cursor.getInt(completedIndex) == 1;
                final Note note = new Note(name, body, dateTarget, timeTarget, timeCreated, timeUpdated, completed);
                recyclerAdapter.insertNote(note);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    private void initFooter() {
        new Footer(this).setInsertCallback(new Footer.InsertCallback() {
            @Override
            public void perform(Note note) {
                insertNote(note);
            }
        });
    }

    private void insertNote(Note note) {
        final SQLiteDatabase database = dbHelper.getWritableDatabase();
        final ContentValues values = getContentValues(note);
        database.insert(NoteDBHelper.TABLE_NAME, null, values);
        recyclerAdapter.insertNote(note);
    }

    @NonNull
    private ContentValues getContentValues(Note note) {
        final ContentValues values = new ContentValues();
        values.put(NoteDBHelper.NAME, note.getName());
        values.put(NoteDBHelper.BODY, note.getBody());
        values.put(NoteDBHelper.DATE_TARGET, note.getDateTarget());
        values.put(NoteDBHelper.TIME_TARGET, note.getTimeTarget().name());
        values.put(NoteDBHelper.TIME_CREATED, note.getTimeCreated());
        values.put(NoteDBHelper.TIME_UPDATED, note.getTimeUpdated());
        values.put(NoteDBHelper.COMPLETED, note.isCompleted() ? 1 : 0);
        return values;
    }

    @NonNull
    private String[] getWhereArgs(Note note) {
        return new String[]{note.getName(), note.getBody(),
                String.valueOf(note.getDateTarget()), note.getTimeTarget().name(),
                String.valueOf(note.getTimeCreated()), String.valueOf(note.getTimeUpdated()),
                String.valueOf(note.isCompleted() ? 1 : 0)};
    }

    private void showDeleteAlertDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_delete_title)
                .setMessage(R.string.dialog_delete_message)
                .setPositiveButton(R.string.button_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteSelectedNotes();
                        releaseActionMode();
                    }
                })
                .setNegativeButton(R.string.button_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        releaseActionMode();
                    }
                })
                .create()
                .show();
    }

    private void deleteSelectedNotes() {
        final SQLiteDatabase database = dbHelper.getWritableDatabase();
        for (Note note : recyclerAdapter.getSelectedItems()) {
            database.delete(NoteDBHelper.TABLE_NAME, NoteDBHelper.WHERE_CLAUSE, getWhereArgs(note));
            recyclerAdapter.removeNote(note);
        }
    }

    private void showCompleteAlertDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_completed_title)
                .setMessage(R.string.dialog_completed_message)
                .setPositiveButton(R.string.button_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        completeSelectedNotes();
                        releaseActionMode();
                    }
                })
                .setNegativeButton(R.string.button_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        releaseActionMode();
                    }
                })
                .create()
                .show();
    }

    private void completeSelectedNotes() {
        for (Integer position : recyclerAdapter.getSelectedPositions()) {
            final Note copy = recyclerAdapter.getItem(position).copy();
            copy.setCompleted(true);
            updateNote(position, copy);
        }
    }

    private void updateNote(int position, Note note) {
        final Note oldNote = recyclerAdapter.getItem(position);
        if (!oldNote.equals(note)) {
            note.setUpdated();
            final SQLiteDatabase database = dbHelper.getWritableDatabase();
            final ContentValues values = getContentValues(note);
            database.update(NoteDBHelper.TABLE_NAME, values, NoteDBHelper.WHERE_CLAUSE, getWhereArgs(oldNote));
            recyclerAdapter.updateNote(position, note);
        }
    }

    private void releaseActionMode() {
        if (actionMode != null) {
            actionMode.finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE && data != null) {
            getNote(data);
        }
    }

    private void getNote(@NonNull Intent data) {
        final int position = data.getIntExtra(POSITION, DEFAULT_POSITION);
        final Note note = data.getParcelableExtra(Note.class.getCanonicalName());
        updateNote(position, note);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cardView:
                final int position = recyclerView.getChildPosition(view);
                if (actionMode != null) {
                    recyclerAdapter.toggleSelected(position);
                    final int count = recyclerAdapter.getSelectedItemsCount();
                    if (count != 0) {
                        actionMode.setTitle(String.valueOf(count));
                    } else {
                        releaseActionMode();
                    }
                } else {
                    recyclerAdapter.toggleExpanded(position);
                }
                break;
            default:
                Log.e(TAG, "Not implemented yet: " + view.getId());
        }
    }

    @Override
    public boolean onLongClick(View view) {
        if (actionMode == null) {
            actionMode = startActionMode(MainActivity.this);
        }
        onClick(view);
        return true;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        actionMode = mode;
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.menu_contextual, menu);
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.contextual_menu_complete:
                showCompleteAlertDialog();
                return true;
            case R.id.contextual_menu_delete:
                showDeleteAlertDialog();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        recyclerAdapter.clearSelections();
        actionMode = null;
    }

    @Override
    public void onCompletedStateChanged(int position) {
        final Note copy = recyclerAdapter.getItem(position).copy();
        copy.setCompleted(!copy.isCompleted());
        updateNote(position, copy);
    }

    @Override
    public void onEditButtonClicked(int position) {
        final Intent intent = new Intent(getApplicationContext(), EditorActivity.class);
        intent.putExtra(POSITION, position);
        intent.putExtra(Note.class.getCanonicalName(), recyclerAdapter.getItem(position));
        startActivityForResult(intent, REQUEST_CODE);
    }
}
