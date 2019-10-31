package cz.dominik.smartnotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import cz.dominik.smartnotes.models.Constants;
import cz.dominik.smartnotes.models.IDatabase;
import cz.dominik.smartnotes.models.Note;

public class LocalDatabase implements IDatabase {
    private Context context;
    private String databaseName;
    private SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_PATTERN_DATABASE);

    private String noteTable = "Notes";

    public LocalDatabase(Context context, String databaseName) {
        this.context = context;
        this.databaseName = databaseName;
        this.initializeDatabase();
    }

    private void initializeDatabase() {
        SQLiteDatabase db = context.openOrCreateDatabase(databaseName, Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS " + noteTable + "(title TEXT, text TEXT, createdDate TEXT, alertDate TEXT, color INT);");
        db.close();
    }

    public ArrayList<Note> getAllNotes() {
        ArrayList<Note> notes = new ArrayList<>();

        SQLiteDatabase db = context.openOrCreateDatabase(databaseName, Context.MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("SELECT rowid _id, * FROM " + noteTable, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Note note = new Note();
            note.id = cursor.getLong(cursor.getColumnIndex("_id"));
            note.title = cursor.getString(cursor.getColumnIndex("title"));
            note.text = cursor.getString(cursor.getColumnIndex("text"));
            try {
                String createdDateString = cursor.getString(cursor.getColumnIndex("createdDate"));
                if (createdDateString != null) {
                    note.createdDate = sdf.parse(createdDateString);
                }
            } catch (ParseException e) {
                note.createdDate = null;
            }
            try {
                String alertDateString = cursor.getString(cursor.getColumnIndex("alertDate"));
                if (alertDateString != null)
                    note.alertDate = sdf.parse(alertDateString);
            } catch (ParseException e) {
                note.alertDate = null;
            }
            note.color = cursor.getInt(cursor.getColumnIndex("color"));

            notes.add(note);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();

        return notes;
    }

    public void insertNote(Note note) {
        SQLiteDatabase db = context.openOrCreateDatabase(databaseName, Context.MODE_PRIVATE, null);
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", note.title);
        contentValues.put("text", note.text);
        contentValues.put("createdDate", note.createdDate != null ? sdf.format(note.createdDate) : sdf.format(Calendar.getInstance().getTime()));
        contentValues.put("alertDate", note.alertDate != null ? sdf.format(note.alertDate) : null);
        contentValues.put("color", note.color);
        db.insert(noteTable, null, contentValues);
        db.close();
    }

    public void deleteNote(Note note) {
        SQLiteDatabase db = context.openOrCreateDatabase(databaseName, Context.MODE_PRIVATE, null);
        String whereClause = "_id=?";
        String[] whereArgs = new String[]{String.valueOf(note)};
        db.delete(noteTable, whereClause, whereArgs);
        db.close();
    }


}
