package com.geon.lbs.ui.customview;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AlertDialog;

/**
 * Created by Babu on 2/1/2018.
 */

public class TransientDialog {

    private Context mContext;

    public TransientDialog(Context mContext){
        this.mContext = mContext;
    }

    public void showTransientDialog(String title,String message){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(mContext)
                .setTitle(title).setMessage(message);
        final AlertDialog alert = dialog.create();
        alert.show();

        // Hide after some seconds
        final Handler handler  = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (alert.isShowing()) {
                    alert.dismiss();
                }
            }
        };

        alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(runnable);
            }
        });
        handler.postDelayed(runnable, 2000);
    }
}
