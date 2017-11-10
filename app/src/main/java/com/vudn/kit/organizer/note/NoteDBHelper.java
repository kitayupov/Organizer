package com.vudn.kit.organizer.note;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.Date;

public class NoteDBHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "Notes";
    public static final String NAME = "name";
    public static final String BODY = "body";
    public static final String DATE_TARGET = "dateTarget";
    public static final String TIME_CREATED = "timeCreated";
    public static final String TIME_UPDATED = "timeUpdated";
    public static final String COMPLETED = "completed";

    public static final String WHERE_CLAUSE = NAME + "=? and " + BODY + "=? and " + DATE_TARGET + "=? and "
            + TIME_CREATED + "=? and " + TIME_UPDATED + "=? and " + COMPLETED + "=?";

    private static final String NOTES_DB = "notes.db";
    private static final int VERSION = 5;

    public NoteDBHelper(Context context) {
        this(context, NOTES_DB, null, VERSION);
    }

    private NoteDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(NotesTable.CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                db.execSQL(
                        String.format(
                                "alter table %s add column %s numeric default %d",
                                TABLE_NAME, TIME_CREATED, new Date().getTime())
                );
                db.execSQL(
                        String.format(
                                "alter table %s add column %s numeric default %d",
                                TABLE_NAME, TIME_UPDATED, new Date().getTime())
                );
                onUpgrade(db, oldVersion + 1, newVersion);
                break;
            case 2:
                db.execSQL(
                        String.format(
                                "alter table %s add column %s numeric default %d",
                                TABLE_NAME, COMPLETED, 0)
                );
                onUpgrade(db, oldVersion + 1, newVersion);
                break;
            case 3:
                db.execSQL(
                        String.format(
                                "alter table %s add column %s text default %s",
                                TABLE_NAME, NAME, "Task")
                );
                onUpgrade(db, oldVersion + 1, newVersion);
                break;
            case 4:
                db.execSQL(
                        String.format(
                                "alter table %s add column %s text default %d",
                                TABLE_NAME, DATE_TARGET, -1)
                );
                onUpgrade(db, oldVersion + 1, newVersion);
                break;
            default:
        }
    }

    private static class NotesTable implements BaseColumns {
        static final String CREATE_QUERY =
                String.format(
                        "create table %s (%s integer primary key autoincrement, " +
                                "%s text, %s text, %s numeric, %s numeric, %s numeric, %s numeric)",
                        TABLE_NAME, _ID, NAME, BODY, DATE_TARGET, TIME_CREATED, TIME_UPDATED, COMPLETED
                );
    }
}
