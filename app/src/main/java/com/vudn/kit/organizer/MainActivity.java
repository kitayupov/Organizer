package com.vudn.kit.organizer;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.vudn.kit.organizer.note.Note;
import com.vudn.kit.organizer.note.NoteAdapter;
import com.vudn.kit.organizer.note.NoteDBHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    public static final String POSITION = "position";
    public static final int REQUEST_CODE = 200;

    private ArrayList<Note> arrayList;
    private NoteAdapter noteAdapter;
    private NoteDBHelper dbHelper;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getApplicationContext(), EditorActivity.class), REQUEST_CODE);
            }
        });

        initControls();
        readDatabase();
    }

    private void initControls() {
        arrayList = new ArrayList<>();
        noteAdapter = new NoteAdapter(arrayList);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(noteAdapter);
        listView.setOnItemClickListener(this);
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
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                insertNote(data);
            }
        }
    }

    private void insertNote(Intent data) {
        if (data != null) {
            int position = data.getIntExtra(POSITION, -1);
            position = (position >= 0 && position < arrayList.size()) ? position : arrayList.size();
            final Note note = data.getParcelableExtra(Note.class.getCanonicalName());
            final SQLiteDatabase database = dbHelper.getWritableDatabase();
            final ContentValues values = new ContentValues();
            values.put(NoteDBHelper.BODY, note.getBody());
            if (position == arrayList.size()) {
                database.insert(NoteDBHelper.TABLE_NAME, null, values);
                arrayList.add(note);
            } else {
                final String whereClause = NoteDBHelper.BODY + "=?";
                final Note oldNote = arrayList.get(position);
                final String[] whereArgs = new String[]{oldNote.getBody()};
                database.update(NoteDBHelper.TABLE_NAME, values, whereClause, whereArgs);
                arrayList.remove(oldNote);
                arrayList.add(position, note);
            }
            noteAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final Intent intent = new Intent(getApplicationContext(), EditorActivity.class);
        intent.putExtra(POSITION, position);
        intent.putExtra(Note.class.getCanonicalName(), noteAdapter.getItem(position));
        startActivityForResult(intent, REQUEST_CODE);
    }
}
