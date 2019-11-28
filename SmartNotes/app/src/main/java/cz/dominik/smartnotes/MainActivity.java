package cz.dominik.smartnotes;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.stream.Collectors;

import cz.dominik.smartnotes.models.Constants;
import cz.dominik.smartnotes.models.IDatabase;
import cz.dominik.smartnotes.models.Note;

public class MainActivity extends AppCompatActivity {
    IDatabase database;
    SimpleDateFormat sdfDatabaseFormat = new SimpleDateFormat(Constants.DATE_PATTERN_DATABASE);

    ArrayList<Note> notes = new ArrayList<>();
    ArrayList<NoteTale> noteTales = new ArrayList<>();

    LinearLayout firstColumn, secondColumn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = new LocalDatabase(this, "smartnotes");
        notes = database.getAllNotes();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.open_create_note_fab);
        fab.setOnClickListener(view -> openNoteActivity());

        firstColumn = findViewById(R.id.first_column);
        secondColumn = findViewById(R.id.second_column);
        drawNoteViews();

        //TODO: remove testing code
        //openNoteActivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            //adding note
            if (requestCode == 100) {
                Note note = new Note();
                note.title = data.getStringExtra("noteTitle");
                note.text = data.getStringExtra("noteText");
                String alertDateString = data.getStringExtra("alertDate");
                note.alertDate = alertDateString != null ? sdfDatabaseFormat.parse(data.getStringExtra("alertDate")) : null;
                note.createdDate = Calendar.getInstance().getTime();
                note.color = data.getIntExtra("color", R.color.noteGrey);
                note.photoFileName = data.getStringExtra("photoFileName");
                note.recordFileName = data.getStringExtra("recordFileName");
                addNote(note);
            } else if (requestCode == 200) {
                long noteIdToDelete = data.getLongExtra("noteIdToDelete", 0);
                //deleting note
                if (noteIdToDelete != 0) {
                    deleteNote(noteIdToDelete);
                    //editing note
                } else {
                    Note note = new Note();
                    note.id = data.getLongExtra("noteId", 0);
                    if (note.id == 0)
                        return;
                    note.title = data.getStringExtra("noteTitle");
                    note.text = data.getStringExtra("noteText");
                    String alertDateString = data.getStringExtra("alertDate");
                    note.alertDate = alertDateString != null ? sdfDatabaseFormat.parse(data.getStringExtra("alertDate")) : null;
                    note.color = data.getIntExtra("color", R.color.noteGrey);
                    note.photoFileName = data.getStringExtra("photoFileName");
                    note.recordFileName = data.getStringExtra("recordFileName");
                    updateNote(note);
                }
            }
        } catch (Exception e) {

        }
    }

    protected void openNoteActivity() {
        Intent intent = new Intent(this, NoteActivity.class);
        startActivityForResult(intent, 100);
    }

    public void addNote(Note note) {
        Note newNote = database.insertNote(note);
        notes.add(newNote);
        drawNoteViews();
    }

    public void deleteNote(long noteId) {
        database.deleteNote(noteId);
        notes = (ArrayList<Note>) notes
                .stream()
                .filter(n -> n.id != noteId)
                .collect(Collectors.toList());
        drawNoteViews();
    }

    public void updateNote(Note updatedNote) {
        try {
            Note updatingNote = notes
                    .stream()
                    .filter(n -> n.id == updatedNote.id)
                    .findFirst()
                    .orElse(null);

            int updatingIndex = notes.indexOf(updatingNote);
            database.updateNote(updatedNote);
            notes.set(updatingIndex, updatedNote);
            drawNoteViews();
        } catch (Exception e) {

        }
    }

    public void drawNoteViews() {
        LinearLayout[] columns = new LinearLayout[]{firstColumn, secondColumn};

        for (LinearLayout column : columns) {
            column.removeAllViews();
        }

        for (int i = 0; i < notes.size(); i++) {
            NoteTale noteTale = new NoteTale(this, notes.get(i));
            columns[i % 2].addView(noteTale.view);
            final Intent intent = new Intent(this, NoteActivity.class);
            //on click listener
            noteTale.view.setOnClickListener(v -> {
                intent.putExtra("id", noteTale.note.id);
                intent.putExtra("createdDate", sdfDatabaseFormat.format(noteTale.note.createdDate));
                intent.putExtra("alertDate", noteTale.note.alertDate != null ? sdfDatabaseFormat.format(noteTale.note.alertDate) : null);
                intent.putExtra("title", noteTale.note.title);
                intent.putExtra("text", noteTale.note.text);
                intent.putExtra("color", noteTale.note.color);
                intent.putExtra("photoFileName", noteTale.note.photoFileName);
                intent.putExtra("recordFileName", noteTale.note.recordFileName);

                startActivityForResult(intent, 200);
            });

            //gestures
            noteTale.view.setOnLongClickListener(v -> {
                new AlertDialog.Builder(this)
                        .setTitle("Delete " + noteTale.note.title)
                        .setMessage("Are you sure you want to delete this note?")
                        .setIcon(android.R.drawable.ic_delete)
                        .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                            this.deleteNote(noteTale.note.id);
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
                return false;
            });

            noteTales.add(noteTale);
        }
    }
}
