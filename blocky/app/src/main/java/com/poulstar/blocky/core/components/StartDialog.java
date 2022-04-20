package com.poulstar.blocky.core.components;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.poulstar.blocky.R;
import com.poulstar.blocky.core.AbstractBlock;

public class StartDialog extends Dialog {

    protected TextView lblInfo;
    protected ImageView imgDialogImage;
    protected Button btnStart, btnBack;
    protected StartDialogListener listener;

    public StartDialog(@NonNull Activity activity) {
        super(activity);
        this.setCancelable(false);
        this.setCanceledOnTouchOutside(false);
    }

    public void setDialogListener(StartDialogListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_dialog);
        this.lblInfo = findViewById(R.id.lblInfo);
        this.btnBack = findViewById(R.id.btnBack);
        this.btnStart = findViewById(R.id.btnStart);
        this.imgDialogImage = findViewById(R.id.imgDialogImage);
        this.btnBack.setOnClickListener(v -> {
            if(this.listener != null) {
                this.listener.back();
            }
        });
        this.btnStart.setOnClickListener(v -> {
            if(this.listener != null) {
                this.listener.start();
            }
        });
    }

    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(lp);
    }

    public void setFailFace() {
        this.lblInfo.setText(getContext().getString(R.string.dialog_lost));
        this.imgDialogImage.setImageResource(R.drawable.lost);
        this.btnStart.setText(getContext().getString(R.string.button_restart));
    }

    public void setWinFace() {
        this.lblInfo.setText(getContext().getString(R.string.dialog_won));
        this.imgDialogImage.setImageResource(R.drawable.win);
        this.btnStart.setText(getContext().getString(R.string.button_restart));
    }
}
