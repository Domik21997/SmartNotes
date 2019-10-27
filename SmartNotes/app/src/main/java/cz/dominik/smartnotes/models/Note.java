package cz.dominik.smartnotes.models;

import java.util.Date;

public class Note {
    public String title;
    public String text;
    public Date createdDate;
    public Date alertDate;
    public int color;

    public Note() {

    }

    public Note(String title, String text, Date createdDate, Date alertDate, int color) {
        this.title = title;
        this.text = text;
        this.createdDate = createdDate;
        this.alertDate = alertDate;
        this.color = color;
    }
}
