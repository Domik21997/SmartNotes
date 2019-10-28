package cz.dominik.smartnotes.models;

import java.util.ArrayList;

public interface IDatabase {
    ArrayList<Note> getAllNotes();
    void insertNote(Note note);
    void deleteNote(Note note);
}
