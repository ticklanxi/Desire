package com.example.display_156_box.utils;

import android.content.Context;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import com.blankj.utilcode.util.SPUtils;
import com.example.display_156_box.bean.MessageEvent;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * Created by Raul_lsj on 2018/3/5.
 */

public class AppUtils {

    public final static String WIDTH = "width";

    public final static String HEIGHT = "height";
    private final static String TAG = AppUtils.class.getSimpleName();

    /**
     * px转dp
     *
     * @param context The context
     * @param px      the pixel value
     * @return value in dp
     */
    public static int pxToDp(Context context, float px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) ((px / displayMetrics.density) + 0.5f);
    }

    /**
     * dp转px
     *
     * @param context
     * @param dp
     * @return
     */
    public static int dpToPx(Context context, float dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) ((dp * displayMetrics.density) + 0.5f);
    }

    /**
     * 获取状态栏高度
     *
     * @param context Context
     * @return int
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    public static String getLocalMacAddressFromIp(Context context) {
        String mac_s = "";
        try {
            byte[] mac;
            NetworkInterface ne = NetworkInterface.getByInetAddress(InetAddress.getByName(getLocalIpAddress()));
            mac = ne.getHardwareAddress();
            mac_s = byte2hex(mac);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mac_s;
    }

    public static String byte2hex(byte[] b) {
        StringBuffer hs = new StringBuffer(b.length);
        String stmp = "";
        int len = b.length;
        for (int n = 0; n < len; n++) {
            stmp = Integer.toHexString(b[n] & 0xFF);
            if (stmp.length() == 1) {
                hs = hs.append("0").append(stmp);
            } else {
                hs = hs.append(stmp);
            }
        }
        return String.valueOf(hs);
    }


    public static String getLocalIpAddress() {
        try {
            Enumeration en = NetworkInterface.getNetworkInterfaces();
            for (; en.hasMoreElements(); ) {
                NetworkInterface intf = (NetworkInterface) en.nextElement();
                for (Enumeration enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e(TAG, "getLocalIpAddress: ", ex);
        }

        return null;
    }


    /**
     * @param command command
     * @return String
     */
    public static String execCommand(String command) {
        Runtime runtime;
        Process proc = null;
        StringBuffer stringBuffer = null;
        try {
            runtime = Runtime.getRuntime();
            proc = runtime.exec(command);
            stringBuffer = new StringBuffer();
            if (proc.waitFor() != 0) {
                System.err.println("exit value = " + proc.exitValue());
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    proc.getInputStream()));

            String line = null;
            while ((line = in.readLine()) != null) {
                stringBuffer.append(line + " ");
            }

        } catch (Exception e) {
            System.err.println(e);
        } finally {
            try {
                proc.destroy();
            } catch (Exception e2) {
                Log.e(TAG, "execCommand: ", e2);
            }
        }
        return stringBuffer.toString();
    }

    /**
     * 判断网线拔插状态
     * 通过命令cat /sys/class/net/eth0/carrier，如果插有网线的话，读取到的值是1，否则为0
     *
     * @return boolean
     */
    public static boolean isWirePluggedIn() {
        String state = execCommand("adb shell cat /sys/class/net/eth0/carrier");
        //有网线插入时返回1，拔出时返回0
        return state.trim().equals("1");
    }

    /**
     * 执行adb命令
     *
     * @param command 命令
     */
    public static void executeAdb(String command) {
        Process process;
        DataOutputStream os;

        try {
            process = Runtime.getRuntime().exec("adb shell");
            os = new DataOutputStream(process.getOutputStream());

            os.writeBytes(command + "\n");
            os.flush();
            os.writeBytes("exit\n");
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    /**
     * 累加校验（修改）2019/12/31 19:00
     *
     * @param ascii ascii码
     * @return string
     */
    public static String verifySum1(String ascii) {
        if (ascii == null || ascii.equals("")) {
            return "";
        }

        //转换成16进制字符串
        String hexStr = bytes2HexString(ascii.getBytes());


        int total = 0;
        int len = hexStr.length();
        int num = 0;
        while (num < len) {
            String s = hexStr.substring(num, num + 2);
            total += Integer.parseInt(s, 16);
            num = num + 2;
        }
        /**
         * 用256求余最大是255，即16进制的FF
         */
        int mod = total % 256;
        String hex = Integer.toHexString(mod).toUpperCase();
        len = hex.length();
        // 如果不够校验位的长度，补0,这里用的是两位校验
        if (len < 2) {
            hex = "0" + hex;
        }
        return hex;
    }

    /**
     * byte[]转16进制字符串
     */
    public static String bytes2HexString(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        String hex;
        for (byte b : bytes) {
            hex = Integer.toHexString(b & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            result.append(hex);
        }
        return result.toString().toUpperCase();
    }


    public static String readTxtFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            Log.e(TAG, "readTxtFile: 文件不存在" + path);
            return "";
        }

        StringBuilder stringBuilder = new StringBuilder();
        try {
            InputStream is = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            inputStreamReader.close();

            Log.d(TAG, "readTxtFile: " + stringBuilder.toString());

            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "readTxtFile: 读取文件失败");
            return "";
        }
    }



    /**
     * 获取随机字符
     */
    public static String getRandomStr(int count) {
        String temp = "QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm{}:<>?:[]!@#$%^&*(";
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            Random random = new Random();
            int t = random.nextInt(70);
            builder.append(temp.charAt(t));
        }
        return builder.toString();
    }


    /**
     * 将车位号补全为四位
     */
    public static String completionCarport(String carport) {
        //将负数层转换为串口通讯指令码
        if (carport.startsWith("-1")) {
            carport = carport.replace("-1", "E");
        } else if (carport.startsWith("-2")) {
            carport = carport.replace("-2", "D");
        } else if (carport.startsWith("-3")) {
            carport = carport.replace("-3", "C");
        }

        //串口指令不足四位的补全为四位
        StringBuilder carportOfSend = new StringBuilder();
        switch (carport.length()) {
            case 1:
                carportOfSend.append('0');
                carportOfSend.append('0');
                carportOfSend.append('0');
                break;
            case 2:
                carportOfSend.append('0');
                carportOfSend.append('0');
                break;
            case 3:
                carportOfSend.append('0');
                break;
        }
        carportOfSend.append(carport);
        return carportOfSend.toString();
    }


    public static void playAudio(int mode, String audioName) {
        switch (mode) {
            case 0://打断前一次播放
                EventBusUtils.postEventBus(new MessageEvent(Constant.EVENT_PLAY_AUDIO, audioName));
                break;
            case 1://不打断前一次播放，下一次延时1000ms
                EventBusUtils.postEventBus(new MessageEvent(Constant.EVENT_PLAY_AUDIO_WITH_DELAY, audioName));
                break;
            case 2://不打断前一次播放，该次放弃播放
                EventBusUtils.postEventBus(new MessageEvent(Constant.EVENT_PLAY_AUDIO_WITH_FORSAKE, audioName));
                break;
            case 3://循环播放
                EventBusUtils.postEventBus(new MessageEvent(Constant.EVENT_PLAY_LOOPING_AUDIO, audioName));
                break;
        }
    }

    /**
     * 判断是否为整数
     *
     * @param str 传入的字符串
     * @return 是整数返回true, 否则返回false
     */
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * 二维码时间戳解密（崔经理&王凯写）
     */
    public static String decryption(String data) {
        String diffTime = "";
        byte[] bytes = data.getBytes();
        for (int i = 0; i < bytes.length; i++) {
            diffTime += String.valueOf((char) (bytes[i] - 49));
        }
        String decryption_data = "" + (Long.valueOf(diffTime) + 1550002019l);
        return decryption_data;
    }


}
