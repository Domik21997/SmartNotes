package cz.dominik.smartnotes;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class CreateNoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        FloatingActionButton fab = findViewById(R.id.add_note_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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
        SetAlertDialog setAlertDialog = new SetAlertDialog();
        setAlertDialog.show(getSupportFragmentManager(), "alert");
    }
}
