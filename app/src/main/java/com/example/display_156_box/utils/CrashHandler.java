package com.example.display_156_box.utils;

import android.content.Context;
import android.util.Log;


import com.example.display_156_box.App;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * @author WDS
 * @date 2020/4/7 8:36
 * description：
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    public static final String TAG = CrashHandler.class.getSimpleName();

    private Context context;
    private App app;

    // 系统默认的 UncaughtException 处理类
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    private static CrashHandler crashHandler;

    public CrashHandler() {

    }

    public static synchronized CrashHandler getInstance() {
        if (crashHandler == null) {
            crashHandler = new CrashHandler();
        }
        return crashHandler;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        Log.e(TAG, "uncaughtException: ", e);
        if (mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(t, e);
        } else {
            Log.d(TAG, "Crash_log:read write failed");
        }
    }

    /**
     * 初始化
     */
    public void init(Context context, App app) {
        // 传入app对象，为完美终止app
        this.context = context;
        this.app = app;
        // 获取系统默认的 UncaughtException 处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置该 CrashHandler 为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }
    /**
     * 自定义错误处理
     */
    private boolean handleException(final Throwable ex) {
        if (ex == null) {
            return true;
        }
        new Thread() {
            @Override
            public void run() {
                String fileName = "Crash_log.txt"; //这里创建文件名
                File file = new File(fileName);
                try {
                    FileOutputStream fos = new FileOutputStream(file, true);
                    fos.write(formatStackTrace(ex).getBytes());
                    fos.flush();
                    fos.close();
                } catch (Exception e) {
                    Log.d(TAG, "Crash_log:write failed");
                }
            }
        }.start();
        return true;
    }

    public void readCrashLog(){
        String fileName = "Crash_log.txt";
        File file = new File(fileName);
        try {
            FileInputStream inputStream = new FileInputStream(file);
            byte[] b = new byte[1024];
            inputStream.read(b);
            inputStream.close();
            Log.d(TAG, "Crash_log:  "+ new String(b));
        } catch (Exception e) {
            Log.d(TAG, "Crash_log:read failed");
        }
    }

    /***
     * 格式化堆栈信息
     */
    public String formatStackTrace(Throwable throwable) {
        if(throwable==null) return "";
        String rtn = throwable.getStackTrace().toString();
        try {
            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            throwable.printStackTrace(printWriter);
            printWriter.flush();
            writer.flush();
            rtn = writer.toString();
            printWriter.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex) {
        }
        return rtn;
    }


}
