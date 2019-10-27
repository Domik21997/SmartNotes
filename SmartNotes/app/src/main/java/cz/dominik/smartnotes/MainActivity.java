package cz.dominik.smartnotes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import cz.dominik.smartnotes.models.IDatabase;
import cz.dominik.smartnotes.models.Note;

public class MainActivity extends AppCompatActivity {
    IDatabase database;

    ArrayList<Note> notes = new ArrayList<>();
    ArrayList<NoteView> noteViews = new ArrayList<>();

    LinearLayout.LayoutParams rowLayoutParams;
    LinearLayout noteListLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = new LocalDatabase(this, "smartnotes");
        initRowLayoutParams();

        notes.add(new Note("Poznámka 1 Poznámka 1 Poznámka 1 Poznámka 1 Poznámka 1 Poznámka 1 Poznámka 1", "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Fusce dui leo, imperdiet in, aliquam sit amet, feugiat eu, orci. Integer vulputate sem a nibh rutrum consequat", null, null, getColor(R.color.noteBlue)));
        notes.add(new Note("Poznámka 2", "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Fusce dui leo, imperdiet in, aliquam sit amet, feugiat eu, orci. Integer vulputate sem a nibh rutrum consequat.", null, null, getColor(R.color.noteRed)));
        notes.add(new Note("Poznámka 3", "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Fusce dui leo, imperdiet in, aliquam sit amet, feugiat eu, orci. Integer vulputate sem a nibh rutrum consequat. Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Fusce dui leo, imperdiet in, aliquam sit amet, feugiat eu, orci. Integer vulputate sem a nibh rutrum consequat.", null, null, getColor(R.color.noteGrey)));
        notes.add(new Note("Poznámka 4", "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Fusce dui leo, imperdiet in, aliquam sit amet, feugiat eu, orci. Integer vulputate sem a nibh rutrum consequat.", null, null, getColor(R.color.noteGreen)));
        notes.add(new Note("Poznámka 5", "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Fusce dui leo, imperdiet in, aliquam sit amet, feugiat eu, orci. Integer vulputate sem a nibh rutrum consequat.", null, null, getColor(R.color.noteYellow)));
        notes.add(new Note("Poznámka 6", "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Fusce dui leo, imperdiet in, aliquam sit amet, feugiat eu, orci. Integer vulputate sem a nibh rutrum consequat.", null, null, getColor(R.color.notePink)));

        notes.add(new Note("Poznámka 5", "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Fusce dui leo, imperdiet in, aliquam sit amet, feugiat eu, orci. Integer vulputate sem a nibh rutrum consequat.", null, null, getColor(R.color.noteYellow)));
        notes.add(new Note("Poznámka 6", "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Fusce dui leo, imperdiet in, aliquam sit amet, feugiat eu, orci. Integer vulputate sem a nibh rutrum consequat.", null, null, getColor(R.color.notePink)));
        notes.add(new Note("Poznámka 5", "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Fusce dui leo, imperdiet in, aliquam sit amet, feugiat eu, orci. Integer vulputate sem a nibh rutrum consequat.", null, null, getColor(R.color.noteYellow)));
        notes.add(new Note("Poznámka 6", "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Fusce dui leo, imperdiet in, aliquam sit amet, feugiat eu, orci. Integer vulputate sem a nibh rutrum consequat.", null, null, getColor(R.color.notePink)));
        notes.add(new Note("Poznámka 5", "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Fusce dui leo, imperdiet in, aliquam sit amet, feugiat eu, orci. Integer vulputate sem a nibh rutrum consequat.", null, null, getColor(R.color.noteYellow)));
        notes.add(new Note("Poznámka 6", "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Fusce dui leo, imperdiet in, aliquam sit amet, feugiat eu, orci. Integer vulputate sem a nibh rutrum consequat.", null, null, getColor(R.color.notePink)));
        notes.add(new Note("Poznámka 5", "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Fusce dui leo, imperdiet in, aliquam sit amet, feugiat eu, orci. Integer vulputate sem a nibh rutrum consequat.", null, null, getColor(R.color.noteYellow)));
        notes.add(new Note("Poznámka 6", "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Fusce dui leo, imperdiet in, aliquam sit amet, feugiat eu, orci. Integer vulputate sem a nibh rutrum consequat.", null, null, getColor(R.color.notePink)));

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.open_create_note_fab);
        fab.setOnClickListener(view -> openCreateNoteActivity());

        noteListLayout = findViewById(R.id.verticalNoteLayout);

        createNoteViews();

        //TODO: remove test calls
        //this.openCreateNoteActivity();
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

    public void addNote(View view) {
        Log.d("behaviorsubject", "test");
    }

    protected void openCreateNoteActivity() {
        Intent intent = new Intent(this, CreateNoteActivity.class);
        startActivity(intent);
    }

    public void addNote(Note note) {
        notes.add(note);

    }

    private void initRowLayoutParams() {
        rowLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        rowLayoutParams.rightMargin = 15;
    }

    public void createNoteViews() {
        int column = 1;
        LinearLayout verticalLayout = findViewById(R.id.verticalNoteLayout);
        LinearLayout currentRow = null;

        for (Note note : notes) {
            if (column == 1) {
                currentRow = new LinearLayout(this);
                currentRow.setOrientation(LinearLayout.HORIZONTAL);
                currentRow.setLayoutParams(rowLayoutParams);
                currentRow.setWeightSum(2);
                verticalLayout.addView(currentRow);
            }

            NoteView noteView = new NoteView(this, note);
            currentRow.addView(noteView.view);
            noteViews.add(noteView);
            if (column == 2) {
                column = 0;
            }

            column++;
        }
    }
}
