package com.app.petsbay;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;

public class CustomDialog extends Dialog {
    public CustomDialog(@NonNull Context context) {
        super(context);

        WindowManager.LayoutParams params=getWindow().getAttributes();

        params.gravity= Gravity.CENTER;
        setTitle(null);
        setCancelable(false);
        setOnCancelListener(null);

        View view=LayoutInflater.from(context).inflate(R.layout.custom_dialog,null);
        setContentView(view);

    }
}
