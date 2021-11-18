package com.graphic.main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Debug;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

// Defines the dialog that pops up when the game is over
public class GameOverDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Build the dialog
        String language = Store.readString(getActivity().getApplicationContext(), R.string.language_key, "en");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if (language.equals("he")) {
            builder.setMessage(R.string.dialog_game_over_he)
                    .setPositiveButton(R.string.restart_he, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ((MainActivity)getActivity()).restart();
                        }
                    })
                    .setNegativeButton(R.string.to_menu_he, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ((MainActivity)getActivity()).returnToMenu();
                        }
                    })
                    .setTitle(R.string.dialog_game_over_title_he);
        }
        else {
            builder.setMessage(R.string.dialog_game_over)
                    .setPositiveButton(R.string.restart, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ((MainActivity)getActivity()).restart();
                        }
                    })
                    .setNegativeButton(R.string.to_menu, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ((MainActivity)getActivity()).returnToMenu();
                        }
                    })
                    .setTitle(R.string.dialog_game_over_title);
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

    // Return to menu when the dialog is dismissed
    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        ((MainActivity)getActivity()).returnToMenu();
    }
}
