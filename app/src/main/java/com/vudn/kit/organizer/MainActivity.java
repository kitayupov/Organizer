package com.vudn.kit.organizer;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.vudn.kit.organizer.note.Note;
import com.vudn.kit.organizer.note.NoteAdapter;
import com.vudn.kit.organizer.note.NoteDBHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener, AbsListView.MultiChoiceModeListener {

    public static final String POSITION = "position";
    public static final int DEFAULT_POSITION = -1;
    public static final int REQUEST_CODE = 200;

    private ArrayList<Note> arrayList;
    private NoteAdapter noteAdapter;
    private NoteDBHelper dbHelper;
    private ListView listView;

    private FloatingActionButton insertButton;
    private FloatingActionButton deleteButton;
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
        noteAdapter = new NoteAdapter(arrayList);
    }

    private void initFloatingButtons() {
        insertButton = (FloatingActionButton) findViewById(R.id.insert_fab);
        deleteButton = (FloatingActionButton) findViewById(R.id.delete_fab);
    }

    private void initListView() {
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(noteAdapter);
        listView.setOnItemClickListener(this);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(this);
    }

    private void setClickListeners() {
        insertButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
    }

    private void readDatabase() {
        dbHelper = new NoteDBHelper(this);
        final SQLiteDatabase database = dbHelper.getReadableDatabase();
        final Cursor cursor = database.query(NoteDBHelper.TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            final int bodyIndex = cursor.getColumnIndex(NoteDBHelper.BODY);
            do {
                final String body = cursor.getString(bodyIndex);
                final Note note = new Note(body);
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
        final ContentValues values = new ContentValues();
        values.put(NoteDBHelper.BODY, note.getBody());
        if (position == arrayList.size()) {
            insertNote(note, database, values);
        } else {
            updateNote(position, note, database, values);
        }
        noteAdapter.notifyDataSetChanged();
    }

    private void insertNote(Note note, SQLiteDatabase database, ContentValues values) {
        database.insert(NoteDBHelper.TABLE_NAME, null, values);
        arrayList.add(note);
    }

    private void updateNote(int position, Note note, SQLiteDatabase database, ContentValues values) {
        final String whereClause = NoteDBHelper.BODY + "=?";
        final Note oldNote = arrayList.get(position);
        final String[] whereArgs = new String[]{oldNote.getBody()};
        database.update(NoteDBHelper.TABLE_NAME, values, whereClause, whereArgs);
        arrayList.remove(oldNote);
        arrayList.add(position, note);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final Intent intent = new Intent(getApplicationContext(), EditorActivity.class);
        intent.putExtra(POSITION, position);
        intent.putExtra(Note.class.getCanonicalName(), noteAdapter.getItem(position));
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
        mode.setTitle(String.valueOf(listView.getCheckedItemCount()));
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        actionMode = mode;
        deleteButton.show();
        return true;
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
        deleteButton.hide();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.insert_fab:
                startActivityForResult(new Intent(this, EditorActivity.class), REQUEST_CODE);
                releaseActionMode();
                break;
            case R.id.delete_fab:
                releaseActionMode();
                break;
            default:
        }
    }

    private void releaseActionMode() {
        if (actionMode != null) {
            actionMode.finish();
            actionMode = null;
        }
    }
}
