package com.game.main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.DialogFragment;

// Defines the dialog that is displayed after clearing a level
public class LevelEndDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Build the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setPositiveButton(R.string.button_continue, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((MainActivity)getActivity()).setLevelEndDialogShown(false);
                ((MainActivity)getActivity()).hideNavigation();
            }
        })
                .setNegativeButton(R.string.to_menu, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Store.saveLong(getActivity().getApplicationContext(), R.string.time_key,
                                ((MainActivity)getActivity()).getManager().getTimeMilis());
                        Store.saveInt(getActivity().getApplicationContext(), R.string.level_key,
                                ((MainActivity)getActivity()).getManager().getLevel() + 1);
                        Store.saveInt(getActivity().getApplicationContext(), R.string.kills_key,
                                ((MainActivity)getActivity()).getManager().getKills());
                        getActivity().finish();
                    }
                })
                .setTitle(R.string.dialog_level_end_title)
                .setMessage(((MainActivity)getActivity()).getIncreasedValueString());
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        return alertDialog;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        // Continue if the dialog is dismissed
        super.onDismiss(dialog);
        ((MainActivity)getActivity()).setLevelEndDialogShown(false);
    }
}
