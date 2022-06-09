package com.example.display_156_box.utils;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
public class GenericToast {
    private Toast mToast;
    private TimeCount timeCount;
    private String message;
    private int gravity;
    private Context mContext;
    private Handler mHandler = new Handler();
    private boolean canceled = true;

    public GenericToast(Context context, int gravity, String msg) {
        message = msg;
        mContext = context;
        this.gravity = gravity;
    }

    /**
     * 自定义时长、居中显示toast
     *
     * @param duration
     */
    public void show(int duration) {
        timeCount = new TimeCount(duration, 1000);
        if (canceled) {
            timeCount.start();
            canceled = false;
            showUntilCancel();
        }
    }

    /**
     * 隐藏toast
     */
    public void hide() {
        if (mToast != null) {
            mToast.cancel();
        }
        if (timeCount != null) {
            timeCount.cancel();
        }
        canceled = true;
    }

    private void showUntilCancel() {
        if (canceled) { //如果已经取消显示，就直接return
            return;
        }
        mToast = Toast.makeText(mContext, message, Toast.LENGTH_LONG);
        mToast.setGravity(gravity, 0, 0);
        mToast.show();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showUntilCancel();
            }
        }, 3500);
    }

    /**
     * 自定义计时器
     */
    private class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval); //millisInFuture总计时长，countDownInterval时间间隔(一般为1000ms)
        }

        @Override
        public void onTick(long millisUntilFinished) {
        }

        @Override
        public void onFinish() {
            hide();
        }
    }
}
