package com.example.display_156_box.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.example.display_156_box.bean.MessageEvent;
import com.example.display_156_box.utils.EventBusUtils;


/**
 * Created by an on 2018/2/23.
 * 外部存储，SD卡插拔广播监听器
 */

public class ExternalStorageReceiver extends BroadcastReceiver {

    public static final String TAG = ExternalStorageReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (TextUtils.isEmpty(action)) {
            return;
        }

        switch (action) {
            case Intent.ACTION_MEDIA_CHECKING:
                //当外存储刚被连接到手机上，手机要对外存储进行检测，还有就是手机开机后也要对外存储设置进行检测，在这检测过程中就是这一状态。
                Log.d(TAG, "onReceive: 外存储刚被连接到手机上");
                break;
            case Intent.ACTION_MEDIA_MOUNTED:
                String path = intent.getData().getPath();
//                //当外存储是可以读，也可以写的时候，也就是外存储正常的时候大多数的状态。
                EventBusUtils.postEventBus(new MessageEvent(Intent.ACTION_MEDIA_MOUNTED, path));
                Log.d(TAG, "onReceive: 外存储可以读，也可以写  " + path);
                break;
            case Intent.ACTION_MEDIA_EJECT:
                //外部存储没有被移出就直接拔掉后的状态
                Log.d(TAG, "onReceive: 外部存储没有被移出就直接拔掉");
                EventBusUtils.postEventBus(new MessageEvent(Intent.ACTION_MEDIA_EJECT));
                break;
            case Intent.ACTION_MEDIA_UNMOUNTED:
                Log.d(TAG, "onReceive: ACTION_MEDIA_UNMOUNTED");
//                ActivityUtils.finishActivity(ImportFileActivity.class);
                break;
            case Intent.ACTION_MEDIA_REMOVED:
                Log.d(TAG, "onReceive: ACTION_MEDIA_REMOVED");
                break;
        }
    }
}
