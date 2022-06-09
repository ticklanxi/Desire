package com.example.display_156_box.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class ToastUtil {
    private static Toast toast;

    public static void toast(Context context, String s) {
        if (toast == null) {
            toast = Toast.makeText(context, s, Toast.LENGTH_SHORT);
        } else {
            toast.setText(s);//直接覆盖还在显示的toast内容，不用等待上一条显示完
        }
        /*
        第一个参数：设置toast在屏幕中显示的位置。我现在的设置是居中靠顶  
        第二个参数：相对于第一个参数设置toast位置的横向X轴的偏移量，正数向右偏移，负数向左偏移  
        第三个参数：同的第二个参数道理一样  
        如果你设置的偏移量超过了屏幕的范围，toast将在屏幕内靠近超出的那个边界显示  
         */
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void toastLong(Context context, String s) {
        if (toast == null) {
            toast = Toast.makeText(context, s, Toast.LENGTH_LONG);
        } else {
            toast.setText(s);//直接覆盖还在显示的toast内容，不用等待上一条显示完
        }
        /*
        第一个参数：设置toast在屏幕中显示的位置。我现在的设置是居中靠顶  
        第二个参数：相对于第一个参数设置toast位置的横向X轴的偏移量，正数向右偏移，负数向左偏移  
        第三个参数：同的第二个参数道理一样  
        如果你设置的偏移量超过了屏幕的范围，toast将在屏幕内靠近超出的那个边界显示  
         */
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void toast(Context context, String s, int x, int y) {
        if (toast == null) {
            toast = Toast.makeText(context, s, Toast.LENGTH_SHORT);
        } else {
            toast.setText(s);//直接覆盖还在显示的toast内容，不用等待上一条显示完
        }
        /*
        第一个参数：设置toast在屏幕中显示的位置。我现在的设置是居中靠顶  
        第二个参数：相对于第一个参数设置toast位置的横向X轴的偏移量，正数向右偏移，负数向左偏移  
        第三个参数：同的第二个参数道理一样  
        如果你设置的偏移量超过了屏幕的范围，toast将在屏幕内靠近超出的那个边界显示  
         */
        toast.setGravity(Gravity.TOP, x, y);

        toast.show();
    }

//    public static void showToast(final Activity activity, final String word, final long time){
//        activity.runOnUiThread(new Runnable() {
//            public void run() {
//                final Toast toast = Toast.makeText(activity, word, Toast.LENGTH_LONG);
//                toast.show();
//                Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    public void run() {
//                        toast.cancel();
//                    }
//                }, time);
//            }
//        });

}
