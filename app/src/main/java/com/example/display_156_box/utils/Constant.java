package com.example.display_156_box.utils;

public class Constant {
    //播放音频(打断前一次播放)
    public static final String EVENT_PLAY_AUDIO = "播放语音";
    //播放音频(不打断前一次播放，该次放弃播放)
    public static final String EVENT_PLAY_AUDIO_WITH_FORSAKE = "放弃播放音频";
    //播放音频(不打断前一次播放，该次延时320ms)
    public static final String EVENT_PLAY_AUDIO_WITH_DELAY = "延时播放音频";
    //播放音频(循环播放)
    public static final String EVENT_PLAY_LOOPING_AUDIO = "循环播放音频";
    //停止语音播放
    public static final String EVENT_STOP_AUDIO = "停止语音";
    //播放多个语音
    public static final String EVENT_PLAY_MULTI_AUDIO = "播放多个语音";
    public static final String EVENT_PLAY_BY_PATH = "EVENT_PLAY_BY_PATH";

    public static final String TEST = "测试";
}
