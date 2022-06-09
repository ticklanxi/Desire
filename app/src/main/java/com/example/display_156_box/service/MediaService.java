package com.example.display_156_box.service;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.display_156_box.bean.MessageEvent;
import com.example.display_156_box.utils.Constant;
import com.example.display_156_box.utils.EventBusUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MediaService extends Service {

    public static final String TAG = MediaService.class.getSimpleName();

    //媒体播放器对象
    private MediaPlayer mediaPlayer;

    /**
     * 需要播放的集合
     */
    private List<String> audios;

    @Override
    public void onCreate() {
        super.onCreate();
        EventBusUtils.registerEventBus(this);
        //创建媒体播放器对象
        mediaPlayer = new MediaPlayer();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        switch (event.getEventTag()) {
            case Constant.EVENT_PLAY_AUDIO://播放音频(打断前一次播放)
                playAudio((String) event.getEventData());
                break;
            case Constant.EVENT_PLAY_AUDIO_WITH_FORSAKE://播放音频(不打断前一次播放，该次放弃播放)
                playAudioWithForsake((String) event.getEventData());
                break;
            case Constant.EVENT_PLAY_AUDIO_WITH_DELAY://播放音频(不打断前一次播放，该次延时320ms)
                playAudioWithDelay((String) event.getEventData());
                break;
            case Constant.EVENT_PLAY_LOOPING_AUDIO://循环播放音频
                playLoopingAudio((String) event.getEventData());
                break;
            case Constant.EVENT_STOP_AUDIO://停止音频播放
                stopPlayMedia();
                break;
            case Constant.EVENT_PLAY_MULTI_AUDIO:
                playMultiAudio((String[]) event.getEventDatas());
                break;
            case Constant.EVENT_PLAY_BY_PATH:
                playByPath((String) event.getEventData());
                break;
        }
    }

    /**
     * 播放音频
     */
    public void playAudio(String fileName) {

        try {
            stopPlayMedia();
            if (mediaPlayer != null) {
                mediaPlayer.reset();
                AssetFileDescriptor fd = getAssets().openFd(fileName);
                mediaPlayer.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
                mediaPlayer.setVolume(0.7f, 0.7f);
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                    }
                });
                mediaPlayer.prepare();
                mediaPlayer.start();
            } else {
                Log.e("MediaService", "mediaPlayer空指针");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void playByPath(String path) {
        try {
            stopPlayMedia();
            Log.d("MediaService", "mediaPlayer播放：" + path );
            if (mediaPlayer != null) {
                Log.d("MediaService", "mediaPlayer播放中");
                mediaPlayer.reset();
                mediaPlayer.setDataSource(path);
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setOnCompletionListener(mp -> {
                    playComplete();
                });
                mediaPlayer.prepare();
                mediaPlayer.start();
            }
        } catch (IOException e) {
            Log.d("MediaService", "mediaPlayer播放失败");
            e.printStackTrace();
        }
    }

    /**
     * 播放音频(如果上一个音频没播放完，这次就不播放)
     */
    public void playAudioWithForsake(final String fileName) {
        try {
            if (!isPlaying()) {
                if (mediaPlayer != null) {
                    mediaPlayer.reset();
                    AssetFileDescriptor fd = getAssets().openFd(fileName);
                    mediaPlayer.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
                    mediaPlayer.setVolume(0.7f, 0.7f);
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } else {
                    Log.e("MediaService", "mediaPlayer空指针");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 播放音频(如果上一个音频没播放完，这次就延时播放)
     */
    public void playAudioWithDelay(final String fileName) {
        try {
            if (!isPlaying()) {
                if (mediaPlayer != null) {
                    mediaPlayer.reset();
                    AssetFileDescriptor fd = getAssets().openFd(fileName);
                    mediaPlayer.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
                    mediaPlayer.setVolume(0.7f, 0.7f);
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } else {
                    Log.e("MediaService", "mediaPlayer空指针");
                }
            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        playAudioWithDelay(fileName);
                    }
                }, 1000);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 循环播放音频
     */
    public void playLoopingAudio(String fileName) {
        try {
            stopPlayMedia();
            if (mediaPlayer != null) {
                mediaPlayer.reset();
                AssetFileDescriptor fd = getAssets().openFd(fileName);
                mediaPlayer.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
                mediaPlayer.setVolume(0.7f, 0.7f);
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setLooping(true);
                mediaPlayer.prepare();
                mediaPlayer.start();
            } else {
                Log.e("MediaService", "mediaPlayer空指针");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 按顺序播放多个语音组合
     *
     * @param fileNames fileNames
     */
    private void playMultiAudio(String... fileNames) {

        if (fileNames.length == 0) {
            Log.d(TAG, "playMultiAudio: fileNames数组为空");
            return;
        }

        if (audios == null) {
            audios = new ArrayList<>();
        }

        Collections.addAll(audios, fileNames);
        Log.d(TAG, "playMultiAudio1:"+ audios);
        if (audios != null && !audios.isEmpty()) {
            String s = audios.get(0);
            audios.remove(0);
            if(s.contains("bluetooth"))
            {
                playByPath(s);
            }
            else {
                play(s);
            }
        }
    }

    /**
     * 播放完毕
     */
    private void playComplete() {
        if (audios != null && !audios.isEmpty()) {
            Log.d(TAG, "playMultiAudio2:"+ audios);
            String s = audios.get(0);
            audios.remove(0);
            play(s);
        }
    }

    /**
     * 播放语音
     *
     * @param fileName 文件名
     */
    private void play(String fileName) {
        try {
            stopPlayMedia();
            if (mediaPlayer != null) {
                mediaPlayer.reset();
                AssetFileDescriptor fd = getAssets().openFd(fileName);
                mediaPlayer.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
                mediaPlayer.setVolume(0.7f, 0.7f);
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setOnCompletionListener(mp -> {
                    playComplete();
                });
                mediaPlayer.prepare();
                mediaPlayer.start();

            } else {
                Log.e("MediaService", "mediaPlayer空指针");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    @Override
    public void onDestroy() {
        stopPlayMedia();
        releaseMedia();
        EventBusUtils.unregisterEventBus(this);
        super.onDestroy();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 停止播放
     */
    public void stopPlayMedia() {
        if (mediaPlayer != null && isPlaying()) {
            mediaPlayer.stop();
        }
    }

    /**
     * 释放资源
     */
    private void releaseMedia() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }

    /**
     * 避免报错
     */
    private boolean isPlaying() {
        boolean isPlaying = false;
        try {
            if (mediaPlayer != null) {
                isPlaying = mediaPlayer.isPlaying();
            }
        } catch (IllegalStateException e) {
            mediaPlayer = null;
        }
        return isPlaying;
    }

}
