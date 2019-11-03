package cz.dominik.smartnotes.models;

import java.util.ArrayList;

public interface IDatabase {
    Note getById(long noteId);
    ArrayList<Note> getAllNotes();
    Note insertNote(Note note);
    void deleteNote(long noteId);
}
