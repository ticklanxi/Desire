package com.example.display_156_box.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ScreenUtils;
import com.example.display_156_box.R;

import static com.example.display_156_box.R.*;

/**
 * 确认弹窗
 */
public class AffirmDialog extends Dialog implements View.OnClickListener {

    private TextView hintMsg;
    private TextView cancel;
    private TextView affirm;

    private String content;
    private boolean isLeft;

    public AffirmDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(layout.dialog_affirm);
        Window window = getWindow();
        if (window != null) {
            //设置背景颜色,只有设置了这个属性,宽度才能全屏MATCH_PARENT
            window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            int width = ScreenUtils.getScreenWidth();
            window.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.CENTER);
        }
        setCanceledOnTouchOutside(true);

        findViews();
        setListeners();
        initView();
    }

    private void findViews() {
        hintMsg = findViewById(id.hint_msg);
        cancel = findViewById(id.cancel);
        affirm = findViewById(id.affirm);
        if (isLeft)
        {
            hintMsg.setGravity(Gravity.LEFT);
        }
    }

    private void setListeners() {
        cancel.setOnClickListener(this);
        affirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case id.cancel:
                if (mOnAffirmCancelListener != null) {
                    mOnAffirmCancelListener.onAffirmClick();
                }
                dismiss();
                break;
            case id.affirm:
                if (mOnAffirmListener != null) {
                    mOnAffirmListener.onAffirmClick();
                }
                dismiss();
                break;
            default:
                break;
        }
    }

    private void initView() {
        if (this.content != null) {
            hintMsg.setText(content);
        }
    }

    public AffirmDialog setContent(String content) {
        this.content = content;
        return this;
    }

    public AffirmDialog setTvGravity(){
        isLeft = true;
        return this;
    }

    private OnAffirmListener mOnAffirmListener;
    private OnAffirmListener mOnAffirmCancelListener;

    public interface OnAffirmListener {
        void onAffirmClick();
    }

    public AffirmDialog setOnAffirmListener(OnAffirmListener listener) {
        mOnAffirmListener = listener;
        return this;
    }

    public AffirmDialog setOnAffirmCancelListener(OnAffirmListener listener) {
        mOnAffirmCancelListener = listener;
        return this;
    }
}
