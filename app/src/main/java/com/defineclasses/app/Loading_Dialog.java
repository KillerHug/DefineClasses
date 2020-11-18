package com.defineclasses.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.animation.LinearInterpolator;

import androidx.core.content.ContextCompat;

import com.agrawalsuneet.dotsloader.loaders.LazyLoader;

public class Loading_Dialog {
    Activity activity;
    AlertDialog alertDialog;
    public Loading_Dialog(Activity activity)
    {
        this.activity=activity;
    }
    public void startLoading()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        LayoutInflater inflater=activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_dialog,null));
        builder.setCancelable(true);
        alertDialog=builder.create();
        alertDialog.show();
    }
    public void dismissDialog()
    {
        alertDialog.dismiss();
    }
}
