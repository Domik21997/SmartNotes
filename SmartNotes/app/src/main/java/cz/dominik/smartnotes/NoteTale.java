package cz.dominik.smartnotes;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import cz.dominik.smartnotes.models.Constants;
import cz.dominik.smartnotes.models.Note;

public class NoteTale {
    Context context;
    Note note;
    SimpleDateFormat sdfDatabaseFormat = new SimpleDateFormat(Constants.DATE_PATTERN_DATABASE);

    //views
    View view;
    TextView noteTitleTextView;
    TextView noteTextTextView;

    public NoteTale(Context context, Note note) {
        this.context = context;
        this.note = note;
        initializeLayout(context);
        initializeViews();
        setNoteValues();
    }

    private void initializeLayout(Context context) {
        view = LinearLayout.inflate(context, R.layout.note_tale, null);
        view.setLayoutParams(setUpParams());
        Drawable drawable = context.getDrawable(R.drawable.note_border);
        drawable.setTint(note.color);
        view.setBackground(drawable);
    }

    private void initializeViews() {
        noteTitleTextView = view.findViewById(R.id.note_title_text_view);
        noteTextTextView = view.findViewById(R.id.note_text_text_view);
    }

    private LinearLayout.LayoutParams setUpParams() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
        );

        params.bottomMargin = 20;
        params.leftMargin = 15;

        return params;
    }

    private void setNoteValues() {
        noteTitleTextView.setText(note.title);
        noteTextTextView.setText(note.text);
    }

}
