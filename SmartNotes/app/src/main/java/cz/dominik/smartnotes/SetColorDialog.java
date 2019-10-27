package cz.dominik.smartnotes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class SetColorDialog extends DialogFragment {
    LinearLayout layout;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        layout = (LinearLayout) inflater.inflate(R.layout.set_color_dialog_layout, null);
        builder.setView(layout)
                .setPositiveButton(R.string.ok, (dialog, id) -> {

                })
                .setNegativeButton(R.string.cancel, (dialog, id) -> {
                    dialog.cancel();
                });



        Button b = layout.findViewById(R.id.blueButton);
        b.setOnClickListener((View v) -> {
            Log.d("behaviorsubject", "aaaaaaa");
        });


        return builder.create();
    }


}
