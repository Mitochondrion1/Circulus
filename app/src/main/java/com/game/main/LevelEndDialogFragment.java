package com.game.main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.DialogFragment;

/**
 * The dialog that is shown after finishing each level.
 */
public class LevelEndDialogFragment extends DialogFragment {
    /**
     * Builds the dialog.
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
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
                                ((MainActivity)getActivity()).getManager().getTimeMillis());
                        Store.saveInt(getActivity().getApplicationContext(), R.string.level_key,
                                ((MainActivity)getActivity()).getManager().getLevel() + 1);
                        Store.saveInt(getActivity().getApplicationContext(), R.string.kills_key,
                                ((MainActivity)getActivity()).getManager().getKills());
                        Store.saveInt(getActivity().getApplicationContext(), R.string.score_key,
                                ((MainActivity)getActivity()).getManager().getScore());
                        ((MainActivity)getActivity()).getManager().getPlayer().saveMultipliers();
                        getActivity().finish();
                    }
                })
                .setTitle(R.string.dialog_level_end_title)
                .setMessage(((MainActivity)getActivity()).getIncreasedValueString());
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        return alertDialog;
    }

    /**
     * Continues if the dialog is dismissed.
     */
    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        ((MainActivity)getActivity()).setLevelEndDialogShown(false);
        ((MainActivity)getActivity()).hideNavigation();
    }
}
