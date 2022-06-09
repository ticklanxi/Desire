package com.example.display_156_box.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ScreenUtils;
import com.example.display_156_box.R;


public class SpecialTipsDialog extends Dialog implements View.OnClickListener {
    private TextView hintMsg;
    private TextView cancel;
    private TextView affirm;

    private String content;

    public SpecialTipsDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        Window window = getWindow();
//        if (window != null) {
//            //设置背景颜色,只有设置了这个属性,宽度才能全屏MATCH_PARENT
//            window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
//            int width = ScreenUtils.getScreenWidth() / 5 * 1;
//            window.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);
//            window.setGravity(Gravity.CENTER);
//        }
        setContentView(R.layout.dialong_special_tips);
        setCanceledOnTouchOutside(false);
//        WindowManager m = getWindow().getWindowManager();
//        Display d = m.getDefaultDisplay();
//        WindowManager.LayoutParams p = getWindow().getAttributes();
//        Point size = new Point();
//        d.getSize(size);
//        p.width = (int)(size.x * 0.9);//设置dialog的宽度为当前手机屏幕的宽度*0.8
//        getWindow().setAttributes(p);

        findViews();
        setListeners();
        initView();
    }

    private void findViews() {
        hintMsg = findViewById(R.id.hint_msg_sp);
        cancel = findViewById(R.id.cancel_sp);
        affirm = findViewById(R.id.affirm_sp);
    }

    private void setListeners() {
        cancel.setOnClickListener(this);
        affirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_sp:
                if (mOnAffirmCancelListener != null) {
                    mOnAffirmCancelListener.onAffirmClick();
                }
                dismiss();
                break;
            case R.id.affirm_sp:
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

    public SpecialTipsDialog setContent(String content) {
        this.content = content;
        return this;
    }

    private SpecialTipsDialog.OnAffirmListener mOnAffirmListener;
    private SpecialTipsDialog.OnAffirmListener mOnAffirmCancelListener;

    public interface OnAffirmListener {
        void onAffirmClick();
    }

    public SpecialTipsDialog setOnAffirmListener(SpecialTipsDialog.OnAffirmListener listener) {
        mOnAffirmListener = listener;
        return this;
    }

    public SpecialTipsDialog setOnAffirmCancelListener(SpecialTipsDialog.OnAffirmListener listener) {
        mOnAffirmCancelListener = listener;
        return this;
    }
}
