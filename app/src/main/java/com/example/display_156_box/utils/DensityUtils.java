package com.example.display_156_box.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.AppUtils;

/**
 * Comments:常用单位转换的辅助类
 */
public class DensityUtils {
    private static float appDensity;
    private static float appScaledDensity;
    private static DisplayMetrics appDisplayMetrics;
    private static int barHeight;

    private DensityUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * dp转px
     *
     * @param context
     * @param dpVal
     * @return
     */
    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    /**
     * sp转px
     *
     * @param context
     * @param spVal
     * @return
     */
    public static int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }
//	/**
//	 * 将sp值转换为px值，保证文字大小不变
//	 *
//	 * @param spValue
//	 *            （DisplayMetrics类中属性scaledDensity）
//	 * @return
//	 */
//	public static int sp2px(Context context, float spValue) {
//		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
//		return (int) (spValue * fontScale + 0.5f);
//	}

    /**
     * px转dp
     *
     * @param context
     * @param pxVal
     * @return
     */
    public static float px2dp(Context context, float pxVal) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (pxVal / scale);
    }

    /**
     * px转sp
     *
     * @param context
     * @param pxVal
     * @return
     */
    public static float px2sp(Context context, float pxVal) {
        return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
    }

//    public static void setDensity(@NonNull Application application) {
//        //获取application的DisplayMetrics
//        appDisplayMetrics = application.getResources().getDisplayMetrics();
//        //获取状态栏高度
//        barHeight = AppUtils.getStatusBarHeight(application);
//
//        if (appDensity == 0) {
//            //初始化的时候赋值
//            appDensity = appDisplayMetrics.density;
//            appScaledDensity = appDisplayMetrics.scaledDensity;
//
//            //添加字体变化的监听
//            application.registerComponentCallbacks(new ComponentCallbacks() {
//                @Override
//                public void onConfigurationChanged(Configuration newConfig) {
//                    //字体改变后,将appScaledDensity重新赋值
//                    if (newConfig != null && newConfig.fontScale > 0) {
//                        appScaledDensity = application.getResources().getDisplayMetrics().scaledDensity;
//                    }
//                }
//
//                @Override
//                public void onLowMemory() {
//
//                }
//            });
//        }
//    }

    //此方法在BaseActivity中做初始化(如果不封装BaseActivity的话,直接用下面那个方法就好)
    public static void setDefault(Activity activity) {
        setAppOrientation(activity, "width");
    }

    //此方法用于在某一个Activity里面更改适配的方向
    public static void setOrientation(Activity activity, String orientation) {
        setAppOrientation(activity, orientation);
    }

    /**
     * targetDensity
     * targetScaledDensity
     * targetDensityDpi
     * 这三个参数是统一修改过后的值
     * <p>
     * orientation:方向值,传入width或height
     */
    private static void setAppOrientation(@Nullable Activity activity, String orientation) {

        float targetDensity;

        if (orientation.equals("height") && appDisplayMetrics != null ) {
            targetDensity = (appDisplayMetrics.heightPixels - barHeight) / 667f;
        } else {
            targetDensity = appDisplayMetrics.widthPixels / 360f;
        }

        float targetScaledDensity = targetDensity * (appScaledDensity / appDensity);
        int targetDensityDpi = (int) (160 * targetDensity);

        /**
         *
         * 最后在这里将修改过后的值赋给系统参数
         *
         * 只修改Activity的density值
         */
        DisplayMetrics activityDisplayMetrics = activity.getResources().getDisplayMetrics();
        activityDisplayMetrics.density = targetDensity;
        activityDisplayMetrics.scaledDensity = targetScaledDensity;
        activityDisplayMetrics.densityDpi = targetDensityDpi;
    }

}
