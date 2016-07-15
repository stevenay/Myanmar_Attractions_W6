package me.naylinaung.myanmar_attractions_w6.utils;

import android.app.Activity;
import android.content.DialogInterface;

import me.naylinaung.myanmar_attractions_w6.MyanmarAttractionsApp;
import me.naylinaung.myanmar_attractions_w6.R;

/**
 * Created by NayLinAung on 7/15/2016.
 */
public class MyAlertDialog {

    public static android.support.v7.app.AlertDialog createAlertDialog(Activity context, String title) {
        android.support.v7.app.AlertDialog alertDialog = new android.support.v7.app.AlertDialog.Builder(
                context, R.style.myDialog).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage("Please enter your email");
        alertDialog.setButton(android.support.v7.app.AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        return alertDialog;
    }

}
