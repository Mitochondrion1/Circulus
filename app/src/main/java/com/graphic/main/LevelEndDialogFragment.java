package com.graphic.main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

// Defines the dialog that is displayed after clearing a level
public class LevelEndDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Build the dialog
        String language = Store.readString(getActivity().getApplicationContext(), R.string.language_key, "en");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if (language.equals("he")) {
            builder.setPositiveButton(R.string.button_continue, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ((MainActivity)getActivity()).setLevelEndDialogShown(false);
                }
            })
                    .setTitle(R.string.dialog_level_end_title_he)
                    .setMessage(((MainActivity)getActivity()).getIncreasedValueString());
        }
        else {
            builder.setPositiveButton(R.string.button_continue, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ((MainActivity)getActivity()).setLevelEndDialogShown(false);
                }
            })
                    .setTitle(R.string.dialog_level_end_title)
                    .setMessage(((MainActivity)getActivity()).getIncreasedValueString());
        }
        AlertDialog alertDialog = builder.create();
        if (language.equals("he")) {
            alertDialog.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        else {
            alertDialog.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
        return alertDialog;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        // Continue if the dialog is dismissed
        super.onDismiss(dialog);
        ((MainActivity)getActivity()).setLevelEndDialogShown(false);
    }
}
