package com.example.dolaapp.Others;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;

import com.example.dolaapp.R;

public class Loading {
    Activity contex;
    AlertDialog dialog;
    public Loading(Activity contex){
        this.contex = contex;
    }
    public void startLoading(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(contex);
        LayoutInflater inflater = contex.getLayoutInflater();
        dialogBuilder.setView(inflater.inflate(R.layout.loading, null));
        dialogBuilder.setCancelable(false);

        dialog = dialogBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }
    public void stopLoading(){
        dialog.dismiss();
    }
}
