package cz.dominik.smartnotes;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class StorageManager {
    public static final String PHOTOS_FOLDER = "photos";
    public static final String RECORDS_FOLDER = "records";

    private Context context;

    public StorageManager(Context context) {
        this.context = context;
    }

    private String generateUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.randomUUID().toString();
    }

    public String saveBitmap(Bitmap bitmapImage) {
        String fileName = this.generateUUID() + ".jpg";

        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir(this.PHOTOS_FOLDER, Context.MODE_PRIVATE);
        File filePath = new File(directory, fileName);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filePath);
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileName;
    }

    public File createFileForRecord() {
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir(this.RECORDS_FOLDER, Context.MODE_PRIVATE);
        return new File(directory, generateUUID() + ".3gp");
    }

    public File getRecordFileByName(String name) {
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir(this.RECORDS_FOLDER, Context.MODE_PRIVATE);
        return new File(directory, name);
    }

    public File getPhotoFileByName(String name) {
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir(this.PHOTOS_FOLDER, Context.MODE_PRIVATE);
        return new File(directory, name);
    }

    public void deletePhoto(String name) {
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir(this.PHOTOS_FOLDER, Context.MODE_PRIVATE);
        try {
            new File(directory, name).delete();
        } catch (Exception e) {
            //whatever..
        }
    }

    public void deleteRecord(String name) {
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir(this.RECORDS_FOLDER, Context.MODE_PRIVATE);
        try {
            new File(directory, name).delete();
        } catch (Exception e) {
            //whatever..
        }
    }
}
