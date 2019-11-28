package cz.dominik.smartnotes.models;

import java.util.Calendar;
import java.util.Date;

public class Note {
    public long id = -1;

    public String title;
    public String text;
    public Date createdDate = Calendar.getInstance().getTime();
    public Date alertDate;
    public int color;
    public String photoFileName;
    public String recordFileName;

    public Note() {

    }

    public Note(String title, String text, Date createdDate, Date alertDate, int color) {
        this(title, text, createdDate, alertDate, color, null, null);
    }

    public Note(String title, String text, Date createdDate, Date alertDate, int color, String photoFileName, String recordFileName) {
        this.title = title;
        this.text = text;
        this.createdDate = createdDate;
        this.alertDate = alertDate;
        this.color = color;
        this.photoFileName = photoFileName;
        this.recordFileName = recordFileName;
    }
}
