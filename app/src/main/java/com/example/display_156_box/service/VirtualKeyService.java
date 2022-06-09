package com.example.display_156_box.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.example.display_156_box.R;
import com.example.display_156_box.utils.AppUtils;
import com.example.display_156_box.utils.Constants;

public class VirtualKeyService extends Service {

    public static final String TAG = VirtualKeyService.class.getSimpleName();
    private ImageView imgHome;

    private Messenger messenger = new Messenger(new MessengerHandler());

    @Override
    public void onCreate() {
        initVirtualKey();
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

    /**
     * 初始化状态栏
     */
    @SuppressLint("ClickableViewAccessibility")
    private void initVirtualKey() {

        View view = LayoutInflater.from(this).inflate(R.layout.layout_virtual_key, null);
        /*
        设置WindowManger布局参数以及相关属性
         */
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
//                TYPE_PHONE
//                TYPE_APPLICATION_OVERLAY
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        /*
        初始化位置
         */
        params.gravity = Gravity.TOP | Gravity.END;
        params.y = 10;
        params.x = 20;

        /*
        获取WindowManager对象
         */
        WindowManager mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(view, params);

        imgHome = view.findViewById(R.id.img_home);

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                imgHome.setVisibility(View.GONE);
//                Intent intent = VirtualKeyService.this.getPackageManager().getLaunchIntentForPackage(VirtualKeyService.this.getPackageName());
//                VirtualKeyService.this.startActivity(intent);

                Log.d(TAG, "keyevent: back");
                AppUtils.executeAdb("input keyevent 4");

//                Intent intent = new Intent(Intent.ACTION_MAIN);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.addCategory(Intent.CATEGORY_HOME);
//                startActivity(intent);
            }
        });
    }


    public class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.INVISIBLE: {
                    Log.d(TAG, "handleMessage: INVISIBLE");
                    imgHome.setVisibility(View.GONE);
                    break;
                }

                case Constants.VISIBLE: {
                    Log.d(TAG, "handleMessage: VISIBLE");
                    imgHome.setVisibility(View.VISIBLE);
                    break;
                }
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    }
}
