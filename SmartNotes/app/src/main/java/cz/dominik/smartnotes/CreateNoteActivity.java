package cz.dominik.smartnotes;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import cz.dominik.smartnotes.models.NoteColor;

public class CreateNoteActivity extends AppCompatActivity {
    final Calendar calendar = Calendar.getInstance();

    NoteColor noteColor;

    ConstraintLayout layout;
    TextView alertTextView;
    FloatingActionButton addNoteFab;
    SimpleDateFormat sdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        layout = findViewById(R.id.createNoteLayout);
        alertTextView = findViewById(R.id.alertTextView);
        addNoteFab = findViewById(R.id.addNoteFab);

        sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        noteColor = new NoteColor();

        addNoteFab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());


        setBackgroundColor();

        //TODO: remove test calls
        this.setColorDialog(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.create_note_buttons, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void setColorDialog(MenuItem item) {
        SetColorDialog setColorDialog = new SetColorDialog();
        setColorDialog.show(getSupportFragmentManager(), "color");
    }

    public void setAlertDialog(MenuItem item) {
        openDatePickerDialog();
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
                    updateAlertTextLabel();
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true

        );
        timePickerDialog.show();
    }

    private void updateAlertTextLabel() {
        if (calendar.before(Calendar.getInstance())) {
            Snackbar.make(addNoteFab, "The alert date cannot be set before the current date.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }

        String formatedAlertDate = sdf.format(calendar.getTime());
        alertTextView.setText("Alert: " + formatedAlertDate);
    }

    private void setBackgroundColor() {
        Log.d("behaviorsubject", "" + noteColor.hexColorToInt(noteColor.pink));
        layout.setBackgroundColor(noteColor.hexColorToInt(noteColor.pink));
    }
}
