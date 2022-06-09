package com.example.display_156_box.utils;

import com.example.display_156_box.bean.MessageEvent;

public class PlayAudioUtil {

    /**
     *
     */
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
     * 播放组合语音
     *
     * @param audios String... audios 音频数组
     */
    public static void playAudio(String... audios) {
        EventBusUtils.postEventBus(new MessageEvent(Constant.EVENT_PLAY_MULTI_AUDIO, audios));
    }



    public static void playAudioByPath(String path) {
        EventBusUtils.postEventBus(new MessageEvent(Constant.EVENT_PLAY_BY_PATH, path));
    }


}
