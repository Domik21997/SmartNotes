package cz.dominik.smartnotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import cz.dominik.smartnotes.models.IDatabase;
import cz.dominik.smartnotes.models.Note;

public class LocalDatabase implements IDatabase {
    private Context context;
    private String databaseName;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    private String NoteTable = "Notes";

    public LocalDatabase(Context context, String databaseName) {
        this.context = context;
        this.databaseName = databaseName;

        SQLiteDatabase db = context.openOrCreateDatabase(databaseName, Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS " + NoteTable + "(title TEXT, text TEXT, createDate TEXT, alertDate TEXT, color INT);");
        db.close();
    }

    public ArrayList<Note> GetAllNotes() {
        ArrayList<Note> notes = new ArrayList<>();

        SQLiteDatabase db = context.openOrCreateDatabase(databaseName, Context.MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("SELECT * FROM " + NoteTable, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Note note = new Note();
            note.id = cursor.getLong(cursor.getColumnIndex("_id"));
            note.title = cursor.getString(cursor.getColumnIndex("title"));
            note.text = cursor.getString(cursor.getColumnIndex("text"));
            try {
                note.createdDate = sdf.parse(cursor.getString(cursor.getColumnIndex("createdDate")));
            } catch (ParseException e) {
                note.createdDate = null;
            }
            try {
                note.alertDate = sdf.parse(cursor.getString(cursor.getColumnIndex("alertDate")));
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

    public void InsertNote(Note note) {
        SQLiteDatabase db = context.openOrCreateDatabase(databaseName, Context.MODE_PRIVATE, null);
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", note.title);
        contentValues.put("text", note.text);
        contentValues.put("createdDate", sdf.format(note.createdDate));
        contentValues.put("title", sdf.format(note.alertDate));
        contentValues.put("color", note.color);
        db.close();
    }

    public void DeleteNote(Note note) {
        SQLiteDatabase db = context.openOrCreateDatabase(databaseName, Context.MODE_PRIVATE, null);
        String whereClause = "_id=?";
        String[] whereArgs = new String[]{String.valueOf(note)};
        db.delete(NoteTable, whereClause, whereArgs);
        db.close();
    }


}
