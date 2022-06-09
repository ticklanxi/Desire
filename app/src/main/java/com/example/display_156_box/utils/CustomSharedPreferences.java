package com.example.display_156_box.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Field;

/**
 * 修改SharedPreferences存储路径，返回SharedPreferences对象
 */
public class CustomSharedPreferences {

    /**
     * 修改SP存储路径
     */
    public static SharedPreferences getSharedPreferences(Context context, String spPath, String fileName) {
        try {
            //利用java反射机制将XML文件自定义存储
            Field field;
            // 获取ContextWrapper对象中的mBase变量。该变量保存了ContextImpl对象
            field = ContextWrapper.class.getDeclaredField("mBase");
            field.setAccessible(true);
            // 获取mBase变量
            Object obj = field.get(context);
            // 获取ContextImpl。mPreferencesDir变量，该变量保存了数据文件的保存路径
            field = obj.getClass().getDeclaredField("mPreferencesDir");
            field.setAccessible(true);
            // 创建自定义路径
            File file = new File(spPath);
            //路径不存在则创建路径
            if (!file.exists()) {
                file.mkdirs();
            }
            // 修改mPreferencesDir变量的值
            field.set(obj, file);

            return context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        } catch (Exception e) {
            Log.e("CustomSharedPreferences", "自定义SharedPreferences存储路径" + e.getMessage());
        }

        return null;
    }

}
