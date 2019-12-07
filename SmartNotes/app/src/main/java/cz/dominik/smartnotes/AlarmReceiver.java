package cz.dominik.smartnotes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.text.SimpleDateFormat;
import java.util.Random;

import cz.dominik.smartnotes.models.Constants;
import cz.dominik.smartnotes.models.IDatabase;
import cz.dominik.smartnotes.models.Note;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        IDatabase database = new LocalDatabase(context, "smartnotes");
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_PATTERN_DATABASE);
        long noteId = intent.getLongExtra("noteId", 0);

        Log.d("behaviorsubject", noteId + "");
        if (noteId == 0)
            return;

        Note note = database.getById(noteId);

        //note could be deleted or alert canceled
        if (note == null || note.alertDate == null)
            return;

        String alertDate = intent.getStringExtra("alertDate");

        if (alertDate == null)
            return;

        //make sure that alert date was not changed
        if (!sdf.format(note.alertDate).equals(alertDate)) {
            return;
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "alerts")
                .setSmallIcon(R.drawable.ic_notifications_white_24dp)
                .setContentTitle(note.title)
                .setContentText(note.text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(new Random().nextInt(), builder.build());
    }
}