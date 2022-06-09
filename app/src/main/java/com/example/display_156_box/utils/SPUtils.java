package com.example.display_156_box.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

/**
 * 类描述：SP数据存储工具类
 */
public class SPUtils {

    //存储的SharedPreferences路径
    private static final String SP_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/BFHY/SP";
    //存储的SharedPreferences文件名
    private static final String FILE_NAME = "desire_sp_data";

    /**
     * 获取自定义存储路径的SharedPreferences
     */
    public static SharedPreferences getCustomSharedPreferences(Context context) {
        return CustomSharedPreferences.getSharedPreferences(context, SP_PATH, FILE_NAME);
    }

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param context
     * @param key
     * @param object
     */
    public static void put(Context context, String key, Object object) {

        String s = String.valueOf(object);
        if (TextUtils.isEmpty(s)) {
            SharedPreferences.Editor editor = getCustomSharedPreferences(context).edit();
            editor.putString(key, "");
            editor.commit();
        }else {
            String encode = Base64.encodeToString(s.getBytes(), Base64.NO_PADDING);
            StringBuilder builder = new StringBuilder(encode);
            builder.insert(2, AppUtils.getRandomStr(5));

            SharedPreferences.Editor editor = getCustomSharedPreferences(context).edit();
            editor.putString(key, builder.toString());
            editor.commit();
        }



//        SharedPreferences.Editor editor = getCustomSharedPreferences(context).edit();
//
//        if (object instanceof String) {
//            editor.putString(key, (String) object);
//        } else if (object instanceof Integer) {
//            editor.putInt(key, (Integer) object);
//        } else if (object instanceof Boolean) {
//            editor.putBoolean(key, (Boolean) object);
//        } else if (object instanceof Float) {
//            editor.putFloat(key, (Float) object);
//        } else if (object instanceof Long) {
//            editor.putLong(key, (Long) object);
//        } else {
//            editor.putString(key, object.toString());
//        }
//
//        editor.commit();
//        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 得到保存的String数据
     *
     * @param context
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getString(Context context, String key, String defaultValue) {
        SharedPreferences sp = getCustomSharedPreferences(context);
        if (sp != null) {
            String encode = sp.getString(key, defaultValue);
            if (defaultValue.equals(encode)) {
                return defaultValue;
            } else {
                StringBuilder builder = new StringBuilder(encode);
                builder.delete(2, 7);
                byte[] decode = Base64.decode(builder.toString(), Base64.NO_PADDING);
                return new String(decode);
            }
        }
        return defaultValue;
    }

    /**
     * 得到保存的int数据
     *
     * @param context
     * @param key
     * @param defaultValue
     * @return
     */
    public static int getInt(Context context, String key, int defaultValue) {
        SharedPreferences sp = getCustomSharedPreferences(context);
        if (sp != null) {
            String defStr = String.valueOf(defaultValue);
            String encode = sp.getString(key, defStr);
            if (defStr.equals(encode)) {
                return defaultValue;
            } else {
                StringBuilder builder = new StringBuilder(encode);
                builder.delete(2, 7);
                byte[] decode = Base64.decode(builder.toString(), Base64.NO_PADDING);
                return Integer.valueOf(new String(decode));
            }
        }
        return defaultValue;
    }

    /**
     * 得到保存的Boolean数据
     *
     * @param context
     * @param key
     * @param defaultValue
     * @return
     */
    public static Boolean getBoolean(Context context, String key, Boolean defaultValue) {
        SharedPreferences sp = getCustomSharedPreferences(context);
        if (sp != null) {
            String defStr = String.valueOf(defaultValue);
            String encode = sp.getString(key, defStr);
            if (defStr.equals(encode)) {
                return defaultValue;
            } else {
                StringBuilder builder = new StringBuilder(encode);
                builder.delete(2, 7);
                byte[] decode = Base64.decode(builder.toString(), Base64.NO_PADDING);
                return Boolean.valueOf(new String(decode));
            }
        }
        return defaultValue;
    }

    /**
     * 得到保存的Float数据
     *
     * @param context
     * @param key
     * @param defaultValue
     * @return
     */
    public static Float getFloat(Context context, String key, Float defaultValue) {
        SharedPreferences sp = getCustomSharedPreferences(context);
        if (sp != null) {
            String defStr = String.valueOf(defaultValue);
            String encode = sp.getString(key, defStr);
            if (defStr.equals(encode)) {
                return defaultValue;
            } else {
                StringBuilder builder = new StringBuilder(encode);
                builder.delete(2, 7);
                byte[] decode = Base64.decode(builder.toString(), Base64.NO_PADDING);
                return Float.valueOf(new String(decode));
            }
        }
        return defaultValue;
    }

    /**
     * 得到保存的Long数据
     *
     * @param context
     * @param key
     * @param defaultValue
     * @return
     */
    public static Long getLong(Context context, String key, Long defaultValue) {
        SharedPreferences sp = getCustomSharedPreferences(context);
        if (sp != null) {
            String defStr = String.valueOf(defaultValue);
            String encode = sp.getString(key, defStr);
            if (defStr.equals(encode)) {
                return defaultValue;
            } else {
                StringBuilder builder = new StringBuilder(encode);
                builder.delete(2, 7);
                byte[] decode = Base64.decode(builder.toString(), Base64.NO_PADDING);
                return Long.valueOf(new String(decode));
            }
        }
        return defaultValue;
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param context
     * @param key
     */
    public static void remove(Context context, String key) {
        SharedPreferences sp = getCustomSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.commit();
//        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 清除所有数据
     *
     * @param context
     */
    public static void clear(Context context) {
        SharedPreferences sp = getCustomSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
//        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean contains(Context context, String key) {
        SharedPreferences sp = getCustomSharedPreferences(context);
        return sp.contains(key);
    }

    /**
     * 返回所有的键值对
     *
     * @param context
     * @return
     */
    public static Map<String, ?> getAll(Context context) {
        SharedPreferences sp = getCustomSharedPreferences(context);
        return sp.getAll();
    }

//    /**
//     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
//     *
//     * @author zhy
//     */
//    private static class SharedPreferencesCompat {
//        private static final Method sApplyMethod = findApplyMethod();
//
//        /**
//         * 反射查找apply的方法
//         *
//         * @return
//         */
//        @SuppressWarnings({"unchecked", "rawtypes"})
//        private static Method findApplyMethod() {
//            try {
//                Class clz = SharedPreferences.Editor.class;
//                return clz.getMethod("apply");
//            } catch (NoSuchMethodException e) {
//            }
//
//            return null;
//        }
//
//        /**
//         * 如果找到则使用apply执行，否则使用commit
//         *
//         * @param editor
//         */
//        public static void apply(SharedPreferences.Editor editor) {
//            try {
//                if (sApplyMethod != null) {
//                    sApplyMethod.invoke(editor);
//                    return;
//                }
//            } catch (IllegalArgumentException e) {
//            } catch (IllegalAccessException e) {
//            } catch (InvocationTargetException e) {
//            }
//            editor.commit();
//        }
//    }

    /**
     * 保存序列化对象到本地
     *
     * @param context
     * @param key
     * @param object
     */
    public static void saveSerializableObject(Context context, String key, Object object) throws IOException {
        SharedPreferences.Editor spEdit = getCustomSharedPreferences(context).edit();
        //先将序列化结果写到byte缓存中，其实就分配一个内存空间
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(bos);
        os.writeObject(object);//将对象序列化写入byte缓存
        //将序列化的数据转为16进制保存
        String bytesToHexString = bytesToHexString(bos.toByteArray());
        //保存该16进制数组
        spEdit.putString(key, bytesToHexString);
        spEdit.commit();
    }

    /**
     * desc:将数组转为16进制
     *
     * @param bArray
     * @return
     */
    public static String bytesToHexString(byte[] bArray) {
        if (bArray == null) {
            return null;
        }
        if (bArray.length == 0) {
            return "";
        }
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 从本地反序列化获取对象
     *
     * @param context
     * @param key
     * @return
     */
    public static Object getSerializableObject(Context context, String key) throws IOException, ClassNotFoundException {
        SharedPreferences sp = getCustomSharedPreferences(context);
        if (sp.contains(key)) {
            String string = sp.getString(key, "");
            if (TextUtils.isEmpty(string)) {
                return null;
            } else {
                //将16进制的数据转为数组，准备反序列化
                byte[] stringToBytes = StringToBytes(string);
                ByteArrayInputStream bis = new ByteArrayInputStream(stringToBytes);
                ObjectInputStream is = new ObjectInputStream(bis);
                //返回反序列化得到的对象
                Object readObject = is.readObject();
                return readObject;
            }
        }
        return null;
    }

    /**
     * desc:将16进制的数据转为数组
     *
     * @param data
     * @return
     */
    public static byte[] StringToBytes(String data) {
        String hexString = data.toUpperCase().trim();
        if (hexString.length() % 2 != 0) {
            return null;
        }
        byte[] retData = new byte[hexString.length() / 2];
        for (int i = 0; i < hexString.length(); i++) {
            int int_ch;  // 两位16进制数转化后的10进制数
            char hex_char1 = hexString.charAt(i); //两位16进制数中的第一位(高位*16)
            int int_ch1;
            if (hex_char1 >= '0' && hex_char1 <= '9')
                int_ch1 = (hex_char1 - 48) * 16;   // 0 的Ascll - 48
            else if (hex_char1 >= 'A' && hex_char1 <= 'F')
                int_ch1 = (hex_char1 - 55) * 16; // A 的Ascll - 65
            else
                return null;
            i++;
            char hex_char2 = hexString.charAt(i); //两位16进制数中的第二位(低位)
            int int_ch2;
            if (hex_char2 >= '0' && hex_char2 <= '9')
                int_ch2 = (hex_char2 - 48); // 0 的Ascll - 48
            else if (hex_char2 >= 'A' && hex_char2 <= 'F')
                int_ch2 = hex_char2 - 55; // A 的Ascll - 65
            else
                return null;
            int_ch = int_ch1 + int_ch2;
            retData[i / 2] = (byte) int_ch;//将转化后的数放入Byte里
        }
        return retData;
    }
}
