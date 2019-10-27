package cz.dominik.smartnotes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.function.Consumer;
import java.util.function.IntConsumer;

import cz.dominik.smartnotes.models.NoteColor;

public class SetColorDialog extends DialogFragment {
    NoteColor noteColor;
    Consumer<Integer> setColorConsumer;
    Consumer<Boolean> confirmConsumer;

    LinearLayout layout;

    public SetColorDialog(NoteColor noteColor, Consumer<Integer> setColorConsumer, Consumer<Boolean> confirmConsumer) {
        this.noteColor = noteColor;
        this.setColorConsumer = setColorConsumer;
        this.confirmConsumer = confirmConsumer;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        layout = (LinearLayout) inflater.inflate(R.layout.set_color_dialog_layout, null);
        builder.setView(layout)
                .setPositiveButton(R.string.ok, (dialog, id) -> {
                    confirmConsumer.accept(true);
                })
                .setNegativeButton(R.string.cancel, (dialog, id) -> {
                    confirmConsumer.accept(false);
                    dialog.cancel();
                });

        layout.findViewById(R.id.whiteButton).setOnClickListener((View v) -> setColorConsumer.accept(noteColor.hexColorToInt(noteColor.white)));
        layout.findViewById(R.id.blueButton).setOnClickListener((View v) -> setColorConsumer.accept(noteColor.hexColorToInt(noteColor.blue)));
        layout.findViewById(R.id.greenButton).setOnClickListener((View v) -> setColorConsumer.accept(noteColor.hexColorToInt(noteColor.green)));
        layout.findViewById(R.id.redButton).setOnClickListener((View v) -> setColorConsumer.accept(noteColor.hexColorToInt(noteColor.red)));
        layout.findViewById(R.id.yellowButton).setOnClickListener((View v) -> setColorConsumer.accept(noteColor.hexColorToInt(noteColor.yellow)));
        layout.findViewById(R.id.pinkButton).setOnClickListener((View v) -> setColorConsumer.accept(noteColor.hexColorToInt(noteColor.pink)));

        return builder.create();
    }


}
