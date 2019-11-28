package cz.dominik.smartnotes;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import java.io.IOException;
import java.util.function.Consumer;

public class RecordDialog extends DialogFragment {
    private LinearLayout layout;
    private Button recordButton;

    private String recordFileName;

    private MediaRecorder recorder;
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    private StorageManager storageManager;
    private Consumer<String> recordObserver;

    public RecordDialog(StorageManager storageManager, Consumer<String> recordObserver) {
        this.storageManager = storageManager;
        this.recordObserver = recordObserver;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        layout = (LinearLayout) inflater.inflate(R.layout.record_dialog_layout, null);
        builder.setView(layout);

        recordButton = layout.findViewById(R.id.record_button);
        recordButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    //Log.d("behaviorsubject", "down");
                    startRecording();
                } else if(event.getAction() == MotionEvent.ACTION_UP) {
                    //Log.d("behaviorsubject", "up");
                    stopRecording();
                    returnRecord();
                }

                return false;
            }
        });

        ActivityCompat.requestPermissions(getActivity(), permissions, 123);

        return builder.create();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
        if (!permissionToRecordAccepted ) dismiss();
    }

    private void startRecording() {
        recordFileName = storageManager.getFileForRecording().getAbsolutePath();

        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(recordFileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e("behaviorsubject", "prepare() failed");
        }

        recorder.start();
    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
    }

    private void returnRecord() {
        recordObserver.accept(this.recordFileName);
        dismiss();
    }
}
