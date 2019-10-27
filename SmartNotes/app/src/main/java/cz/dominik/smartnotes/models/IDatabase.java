package cz.dominik.smartnotes.models;

import java.util.ArrayList;

public interface IDatabase {
    ArrayList<Note> GetAllNotes();
    void InsertNote(Note note);
    void DeleteNote(Note note);
}
