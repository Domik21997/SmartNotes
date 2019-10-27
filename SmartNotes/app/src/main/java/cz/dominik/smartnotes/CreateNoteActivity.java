package cz.dominik.smartnotes;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.function.Consumer;

import cz.dominik.smartnotes.models.Note;

public class CreateNoteActivity extends AppCompatActivity {
    final Calendar calendar = Calendar.getInstance();

    Note note;
    SimpleDateFormat sdf;
    Consumer<Integer> setColorObservable;
    int selectedColor;

    ConstraintLayout layout;
    TextView alertTextView;
    FloatingActionButton addNoteFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        layout = findViewById(R.id.createNoteLayout);
        alertTextView = findViewById(R.id.alertTextView);
        addNoteFab = findViewById(R.id.addNoteFab);

        sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");

        addNoteFab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        selectedColor = getColor(R.color.noteGrey);
        setColorObservable = (value) -> setBackgroundColor(value);
        setBackgroundColor(selectedColor);

        note = new Note();
        note.color = selectedColor;

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
        SetColorDialog setColorDialog = new SetColorDialog(setColorObservable, (value) -> confirmSelection(value));
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
            Snackbar.make(addNoteFab, "The alert date cannot be set before the current date.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }

        note.alertDate = date;
        updateAlertTextLabel(date);
    }

    private void updateAlertTextLabel(Date date) {
        String formatedAlertDate = sdf.format(date);
        alertTextView.setText("Alert: " + formatedAlertDate);
    }

    private void setBackgroundColor(int value) {
        selectedColor = value;
        layout.setBackgroundColor(value);
    }

    private void confirmSelection(boolean value) {
        if(value) {
            note.color = selectedColor;
        } else {
            setBackgroundColor(note.color);
        }
    }
}
