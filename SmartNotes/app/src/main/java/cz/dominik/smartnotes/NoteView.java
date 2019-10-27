package cz.dominik.smartnotes;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import cz.dominik.smartnotes.models.Note;

public class NoteView {
    View view;
    Note note;

    TextView noteTitleTextView;
    TextView noteTextTextView;

    public NoteView(Context context, Note note) {
        this.note = note;

        view = LinearLayout.inflate(context, R.layout.note, null);
        view.setLayoutParams(setUpParams());
        //view.setBackgroundColor(note.color);
        Drawable drawable = context.getDrawable(R.drawable.note_border);
        drawable.setTint(note.color);
        view.setBackground(drawable);

        noteTitleTextView = view.findViewById(R.id.noteTitleTextView);
        noteTextTextView = view.findViewById(R.id.noteTextTextView);

        setNoteValues();
    }

    private LinearLayout.LayoutParams setUpParams() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
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
