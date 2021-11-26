package com.game.main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.DialogFragment;

// Defines the dialog that pops up when the game is over
public class GameOverDialogFragment extends DialogFragment {
    private boolean dismissed;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Build the dialog
        dismissed = true;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_game_over)
                .setPositiveButton(R.string.restart, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismissed = false;
                        ((MainActivity)getActivity()).restartGame();
                    }
                })
                .setNegativeButton(R.string.to_menu, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismissed = false;
                        ((MainActivity)getActivity()).returnToMenu();
                    }
                })
                .setTitle(R.string.dialog_game_over_title);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        return alertDialog;
    }

    // Return to menu when the dialog is dismissed
    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (dismissed) {
            ((MainActivity)getActivity()).returnToMenu();
        }
    }
}