package com.example.mediapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

public class CustomAllSetDialog {

    private Activity activity;
    private AlertDialog dialog;

    public CustomAllSetDialog(Activity myActivity) {
        activity = myActivity;
    }

    public void startAllSetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_all_set_dialog, null));
        builder.setCancelable(true);

        dialog = builder.create();
        dialog.show();
    }

    public void dismissAllSetDialog() {
        dialog.dismiss();
    }
}
