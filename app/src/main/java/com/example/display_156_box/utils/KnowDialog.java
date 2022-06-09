package com.example.display_156_box.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.display_156_box.activity.MainActivity;
import com.example.display_156_box.R;

/**
 * @author WDS
 * @date 2020/3/20 9:59
 * description：
 */
public class KnowDialog extends Dialog {

    private TextView tvMessage;
    private TextView tvOk;

    private OnClickListener listener;
    private String content;


    public KnowDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public KnowDialog(MainActivity mainActivity) {
        super(mainActivity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_know);

        tvOk = findViewById(R.id.tv_ok);
        tvMessage = findViewById(R.id.tv_message);
        tvMessage.setText(content);

        tvOk.setOnClickListener(v -> {
            dismiss();
        });
    }

    public interface OnClickListener {
        void onOk();
    }

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    public void setMessage(String msg) {
        content = msg;

    }
}
