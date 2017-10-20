package com.vudn.kit.organizer;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.AbsListView;

import com.vudn.kit.organizer.note.Note;
import com.vudn.kit.organizer.note.NoteDBHelper;
import com.vudn.kit.organizer.note.RecyclerAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        AbsListView.MultiChoiceModeListener, RecyclerView.OnItemTouchListener {

    public static final String POSITION = "position";
    public static final int DEFAULT_POSITION = -1;
    public static final int REQUEST_CODE = 200;

    private ArrayList<Note> arrayList;
    private NoteDBHelper dbHelper;

    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    private GestureDetectorCompat gestureDetector;

    private FloatingActionButton insertButton;
    private FloatingActionButton deleteButton;
    private FloatingActionButton completeButton;
    private ActionMode actionMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();
        initNoteList();
        initListView();
        initFloatingButtons();
        setClickListeners();
        readDatabase();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initNoteList() {
        arrayList = new ArrayList<>();
        recyclerAdapter = new RecyclerAdapter(arrayList);
    }

    private void initFloatingButtons() {
        insertButton = (FloatingActionButton) findViewById(R.id.insert_fab);
        deleteButton = (FloatingActionButton) findViewById(R.id.delete_fab);
        completeButton = (FloatingActionButton) findViewById(R.id.complete_fab);
    }

    private void initListView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.addOnItemTouchListener(this);
        gestureDetector = new GestureDetectorCompat(this, gestureListener);
    }

    private void setClickListeners() {
        insertButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
        completeButton.setOnClickListener(this);
    }

    private void readDatabase() {
        dbHelper = new NoteDBHelper(this);
        final SQLiteDatabase database = dbHelper.getReadableDatabase();
        final Cursor cursor = database.query(NoteDBHelper.TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            final int bodyIndex = cursor.getColumnIndex(NoteDBHelper.BODY);
            final int createdIndex = cursor.getColumnIndex(NoteDBHelper.TIME_CREATED);
            final int updatedIndex = cursor.getColumnIndex(NoteDBHelper.TIME_UPDATED);
            final int completedIndex = cursor.getColumnIndex(NoteDBHelper.COMPLETED);
            do {
                final String body = cursor.getString(bodyIndex);
                final long timeCreated = cursor.getLong(createdIndex);
                final long timeUpdated = cursor.getLong(updatedIndex);
                final boolean completed = cursor.getInt(completedIndex) == 1;
                final Note note = new Note(body, timeCreated, timeUpdated, completed);
                arrayList.add(note);
            } while (cursor.moveToNext());
        }
        cursor.close();
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
        int position = data.getIntExtra(POSITION, DEFAULT_POSITION);
        position = (position >= 0 && position < arrayList.size()) ? position : arrayList.size();
        final Note note = data.getParcelableExtra(Note.class.getCanonicalName());
        final SQLiteDatabase database = dbHelper.getWritableDatabase();
        if (position == arrayList.size()) {
            insertNote(note, database);
        } else {
            updateNote(position, note, database);
        }
        recyclerAdapter.notifyDataSetChanged();
    }

    private void insertNote(Note note, SQLiteDatabase database) {
        final ContentValues values = getContentValues(note);
        database.insert(NoteDBHelper.TABLE_NAME, null, values);
        arrayList.add(note);
    }

    private void updateNote(int position, Note note, SQLiteDatabase database) {
        final Note oldNote = arrayList.get(position);
        final ContentValues values = getContentValues(note);
        database.update(NoteDBHelper.TABLE_NAME, values, NoteDBHelper.WHERE_CLAUSE, getWhereArgs(oldNote));
        arrayList.remove(oldNote);
        arrayList.add(position, note);
    }

    @NonNull
    private ContentValues getContentValues(Note note) {
        final ContentValues values = new ContentValues();
        values.put(NoteDBHelper.BODY, note.getBody());
        values.put(NoteDBHelper.TIME_CREATED, note.getTimeCreated());
        values.put(NoteDBHelper.TIME_UPDATED, note.getTimeUpdated());
        values.put(NoteDBHelper.COMPLETED, note.isCompleted() ? 1 : 0);
        return values;
    }

    @NonNull
    private String[] getWhereArgs(Note note) {
        return new String[]{note.getBody(), String.valueOf(note.getTimeCreated()),
                String.valueOf(note.getTimeUpdated()), String.valueOf(note.isCompleted() ? 1 : 0)};
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        setButtonsStateStarted();
        actionMode = mode;
        return true;
    }

    private void setButtonsStateStarted() {
        changeInsertButtonForward();
        deleteButton.show();
        completeButton.show();
    }

    private void changeInsertButtonForward() {
        rotateInsertButton(45.0F);
        changeInsertButtonColor(R.color.colorCancel);
    }

    private void rotateInsertButton(float value) {
        ViewCompat.animate(insertButton)
                .rotation(value)
                .withLayer()
                .setDuration(500L)
                .setInterpolator(new OvershootInterpolator())
                .start();
    }

    private void changeInsertButtonColor(int color) {
        insertButton.setBackgroundTintList(
                ColorStateList.valueOf(ContextCompat.getColor(this, color)));
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        recyclerAdapter.clearSelections();
        setButtonsStateFinished();
        actionMode = null;
    }

    private void setButtonsStateFinished() {
        changeInsertButtonBackward();
        deleteButton.hide();
        completeButton.hide();
    }

    private void changeInsertButtonBackward() {
        rotateInsertButton(0.0F);
        changeInsertButtonColor(R.color.colorAccent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.insert_fab:
                if (actionMode == null) {
                    startActivityForResult(new Intent(this, EditorActivity.class), REQUEST_CODE);
                }
                releaseActionMode();
                break;
            case R.id.delete_fab:
                showDeleteAlertDialog();
                break;
            case R.id.complete_fab:
                showCompleteAlertDialog();
                break;
            case R.id.item_layout:
                final int position = recyclerView.getChildPosition(v);
                if (actionMode != null) {
                    toggleSelection(position);
                    return;
                }
                final Intent intent = new Intent(getApplicationContext(), EditorActivity.class);
                intent.putExtra(POSITION, position);
                intent.putExtra(Note.class.getCanonicalName(), recyclerAdapter.getItem(position));
                startActivityForResult(intent, REQUEST_CODE);
                break;
            default:
        }
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
        for (Integer position : recyclerAdapter.getSelectedItems()) {
            final Note note = recyclerAdapter.getItem(position);
            database.delete(NoteDBHelper.TABLE_NAME, NoteDBHelper.WHERE_CLAUSE, getWhereArgs(note));
            arrayList.remove(note);
        }
        recyclerAdapter.notifyDataSetChanged();
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
        final SQLiteDatabase database = dbHelper.getWritableDatabase();
        final ArrayList<Integer> selectedItems = recyclerAdapter.getSelectedItems();
        for (Integer position : selectedItems) {
            final Note copy = arrayList.get(position).copy();
            copy.setCompleted();
            copy.setUpdated();
            updateNote(position, copy, database);
        }
        recyclerAdapter.notifyDataSetChanged();
    }

    private void releaseActionMode() {
        if (actionMode != null) {
            actionMode.finish();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        gestureDetector.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    private void toggleSelection(int position) {
        recyclerAdapter.toggleSelection(position);
        actionMode.setTitle(String.valueOf(recyclerAdapter.getSelectedItemCount()));
    }

    private GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            final View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
            onClick(view);
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            final View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (actionMode != null) {
                return;
            }
            actionMode = startActionMode(MainActivity.this);
            final int position = recyclerView.getChildPosition(view);
            toggleSelection(position);
            super.onLongPress(e);
        }
    };
}
