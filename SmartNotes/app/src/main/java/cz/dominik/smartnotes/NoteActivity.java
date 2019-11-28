package cz.dominik.smartnotes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.function.Consumer;

import cz.dominik.smartnotes.models.Constants;
import cz.dominik.smartnotes.models.Note;

public class NoteActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private StorageManager storageManager;
    private final Calendar calendar = Calendar.getInstance();
    private Note note;
    private SimpleDateFormat sdfUserFormat = new SimpleDateFormat(Constants.DATE_PATTERN_USER);
    private SimpleDateFormat sdfDatabaseFormat = new SimpleDateFormat(Constants.DATE_PATTERN_DATABASE);
    private int selectedColor;
    private Consumer<Integer> colorObserver;
    private Consumer<String> recordObserver;

    //views
    private EditText titleEditView;
    private EditText textEditView;
    private ConstraintLayout layout;
    private TextView alertTextView;
    private FloatingActionButton fabButton;
    private MenuItem deleteNoteMenuItem;
    private ImageView photoImageView;
    private FloatingActionButton playRecordFabButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        initializeViews();
        initializeNoteData();
        bindFabButtonOnClickListener();
        this.storageManager = new StorageManager(getApplicationContext());

        //TODO: remove testing calls
        this.openRecordDialog(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.create_note_buttons, menu);
        deleteNoteMenuItem = menu.findItem(R.id.delete_note_menu_item);
        //show delete note menu item if view is in edit mode
        if (note.id > 0)
            deleteNoteMenuItem.setVisible(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            photoImageView.setImageBitmap(imageBitmap);
            String fileName = storageManager.saveBitmap(imageBitmap);
            this.note.photoFileName = fileName;
            Log.d("behaviorsubject", fileName);
        }
    }

    private void initializeViews() {
        titleEditView = findViewById(R.id.note_title_edit_view);
        textEditView = findViewById(R.id.note_text_edit_view);
        layout = findViewById(R.id.create_note_layout);
        alertTextView = findViewById(R.id.alert_text_view);
        fabButton = findViewById(R.id.editing_fab_button);
        photoImageView = findViewById(R.id.note_photo_image_view);
        playRecordFabButton = findViewById(R.id.play_record_sound_fab_button);
    }

    private void initializeNoteData() {
        Intent intent = getIntent();
        note = new Note();

        try {
            note.id = intent.getLongExtra("id", -1);
            if (intent.getStringExtra("createdDate") != null)
                note.createdDate = sdfDatabaseFormat.parse(intent.getStringExtra("createdDate"));
            String alertDateString = intent.getStringExtra("alertDate");
            note.alertDate = alertDateString != null ? sdfDatabaseFormat.parse(alertDateString) : null;
            note.title = intent.getStringExtra("title");
            note.text = intent.getStringExtra("text");
            note.color = intent.getIntExtra("color", 0);
            selectedColor = note.color;
            bindViewsData(note);
            fabButton.setImageResource(R.drawable.ic_edit_white_24dp);
        } catch (Exception e) {
            e.printStackTrace();
            //create new note_tale
            selectedColor = getColor(R.color.noteGrey);
            note.color = selectedColor;
        }

        colorObserver = (value) -> setBackgroundColor(value);
        setBackgroundColor(selectedColor);

        recordObserver = (value) -> setRecordFile(value);
    }

    private void bindViewsData(Note note) {
        titleEditView.setText(note.title);
        textEditView.setText(note.text);
        if (note.alertDate != null)
            updateAlertTextLabel(note.alertDate);
    }

    private void bindFabButtonOnClickListener() {
        fabButton.setOnClickListener(view -> {
                    String noteTitle = titleEditView.getText().toString();
                    String noteText = textEditView.getText().toString();

                    if (noteTitle.isEmpty() && noteText.isEmpty()) {
                        Snackbar.make(view, "Note is empty.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        return;
                    }

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("noteId", note.id);
                    resultIntent.putExtra("noteTitle", noteTitle);
                    resultIntent.putExtra("noteText", noteText);
                    String alertDateString = null;
                    if (note.alertDate != null) {
                        alertDateString = sdfDatabaseFormat.format(note.alertDate);
                    }
                    resultIntent.putExtra("alertDate", alertDateString);
                    resultIntent.putExtra("color", note.color);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                }
        );
    }

    private void confirmSelection(boolean value) {
        if (value) {
            note.color = selectedColor;
        } else {
            setBackgroundColor(note.color);
        }
    }

    private void openDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, monthOfYear);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    //Log.d("behaviorsubject", year + " " + monthOfYear + " " + dayOfMonth);
                    openTimePickerDialog();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void openTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {
                    //Log.d("behaviorsubject", hourOfDay + ":" + minute);
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                    setAlert(calendar.getTime());
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true

        );
        timePickerDialog.show();
    }

    private void setAlert(Date date) {
        if (date.before(Calendar.getInstance().getTime())) {
            Snackbar.make(fabButton, "The alert date cannot be set before the current date.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }

        note.alertDate = date;
        updateAlertTextLabel(date);
    }

    private void updateAlertTextLabel(Date date) {
        String formatedAlertDate = sdfUserFormat.format(date);
        alertTextView.setText("Alert: " + formatedAlertDate);
    }

    private void setBackgroundColor(int value) {
        selectedColor = value;
        layout.setBackgroundColor(value);
    }

    @SuppressLint("RestrictedApi")
    private void setRecordFile(String recordFileName) {
        playRecordFabButton.setVisibility(View.VISIBLE);
        this.note.recordFileName = recordFileName;
        Log.d("behaviorsubject", recordFileName);
    }

    //menu onclick listeners
    public void setColorDialog(MenuItem item) {
        SetColorDialog setColorDialog = new SetColorDialog(colorObserver, (value) -> confirmSelection(value));
        setColorDialog.show(getSupportFragmentManager(), "color");
    }

    public void setAlertDialog(MenuItem item) {
        openDatePickerDialog();
    }

    public void deleteNote(MenuItem item) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("noteIdToDelete", note.id);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    public void takePhoto(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void openRecordDialog(View view) {
        RecordDialog recordDialog = new RecordDialog(storageManager, recordObserver);
        recordDialog.show(getSupportFragmentManager(), "openRecordDialog");
    }

    public void loadPhoto() {
        String photoFilePath = storageManager.getPhotoFileByName(note.photoFileName).getAbsolutePath();
        Log.d("behaviorsubject", photoFilePath);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(photoFilePath, options);
        photoImageView.setImageBitmap(bitmap);
    }

    public void playRecord(View view) {
        String recordFilePath = storageManager.getRecordFileByName(this.note.recordFileName).getAbsolutePath();
        Log.d("behaviorsubject", recordFilePath);

        MediaPlayer mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(recordFilePath);
            mPlayer.prepare();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mPlayer.start();
    }
}
















