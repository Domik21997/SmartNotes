package cz.dominik.smartnotes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.function.Consumer;

public class ExitWithoutSaveDialog extends DialogFragment {
    private Consumer<Boolean> resultConsumer;

    public ExitWithoutSaveDialog(Consumer<Boolean> resultConsumer) {
        this.resultConsumer = resultConsumer;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.exit_without_save_dialog_layout, null);
        builder.setView(layout)
                .setPositiveButton(R.string.yes, (dialog, id) -> {
                    resultConsumer.accept(true);
                })
                .setNegativeButton(R.string.no, (dialog, id) -> {
                    resultConsumer.accept(false);
                    dialog.cancel();
                });

        return builder.create();
    }

}
