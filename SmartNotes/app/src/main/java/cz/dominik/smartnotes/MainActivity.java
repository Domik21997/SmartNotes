package cz.dominik.smartnotes;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.stream.Collectors;

import cz.dominik.smartnotes.models.Constants;
import cz.dominik.smartnotes.models.IDatabase;
import cz.dominik.smartnotes.models.Note;

public class MainActivity extends AppCompatActivity {
    private IDatabase database;
    private SimpleDateFormat sdfDatabaseFormat = new SimpleDateFormat(Constants.DATE_PATTERN_DATABASE);
    private StorageManager storageManager;

    private ArrayList<Note> notes = new ArrayList<>();
    private ArrayList<NoteTale> noteTales = new ArrayList<>();

    private LinearLayout firstColumn, secondColumn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.storageManager = new StorageManager(getApplicationContext());
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

        createNotificationChannel();
        //testNotification();
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

                    Note oldNote = database.getById(note.id);
                    if (oldNote.alertDate == null && alertDateString != null) {
                        setAlarm(note);
                    } else if (alertDateString != null && !sdfDatabaseFormat.format(oldNote.alertDate).equals(alertDateString)) {
                        setAlarm(note);
                    }

                    updateNote(note);
                }
            }
        } catch (Exception e) {

        }
    }

    private void setAlarm(Note note) {
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        alarmIntent.putExtra("noteId", note.id);
        alarmIntent.putExtra("alertDate", sdfDatabaseFormat.format(note.alertDate));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, new Random().nextInt(), alarmIntent, 0);
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 4);

        calendar.getTimeInMillis();


        manager.set(AlarmManager.RTC_WAKEUP, note.alertDate.getTime(), pendingIntent);
    }

    protected void openNoteActivity() {
        Intent intent = new Intent(this, NoteActivity.class);
        startActivityForResult(intent, 100);
    }

    public void addNote(Note note) {
        Note newNote = database.insertNote(note);
        if (newNote.alertDate != null) {
            setAlarm(newNote);
        }
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
            Note note = notes.get(i);
            Bitmap notePhotoBitmap = null;

            if (note.photoFileName != null) {
                String photoFilePath = storageManager.getPhotoFileByName(note.photoFileName).getAbsolutePath();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                notePhotoBitmap = BitmapFactory.decodeFile(photoFilePath, options);
            }

            NoteTale noteTale = new NoteTale(this, note, notePhotoBitmap);
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

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("alerts", "Alerts", importance);
            channel.setDescription("Note alerts");
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void testNotification() {
        Note testNote = database.getById(1);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.SECOND, 2);
        testNote.alertDate = c.getTime();
        updateNote(testNote);
        setAlarm(testNote);
    }
}
