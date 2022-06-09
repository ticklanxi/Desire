package com.example.display_156_box.utils;

import com.example.display_156_box.bean.MessageEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Juna on 2018/4/10.
 * EventBus方法统一管理调用的类
 */

public class EventBusUtils {

    /**
     * 注册EventBus
     */
    public static void registerEventBus(Object object) {
        if (!EventBus.getDefault().isRegistered(object)) {
            EventBus.getDefault().register(object);
        }
    }

    /**
     * 注册EventBus
     */
    public static void registerEventBus(boolean ifNeedEventBus, Object object) {
        if (ifNeedEventBus) {
            registerEventBus(object);
        }
    }

    /**
     * 解注册EventBus
     */
    public static void unregisterEventBus(Object object) {
        if (EventBus.getDefault().isRegistered(object)) {
            EventBus.getDefault().unregister(object);
        }
    }

    /**
     * 解注册EventBus
     */
    public static void unregisterEventBus(boolean ifNeedEventBus, Object object) {
        if (ifNeedEventBus) {
            unregisterEventBus(object);
        }
    }

    /**
     * 发送EventBus
     */
    public static void postEventBus(MessageEvent event) {
        EventBus.getDefault().post(event);
    }
}
