package cz.dominik.smartnotes.models;

import android.graphics.Color;

public class NoteColor {
    public String white = "#FFFFFF";
    public String blue = "#00BFFF";
    public String green = "#7FFF00";
    public String red = "#FF4500";
    public String yellow = "#FFFF33";
    public String pink = "#FF1493";

    public NoteColor() {

    }

    public int hexColorToInt(String hexColor) {
        return Color.parseColor(hexColor);
    }
}
