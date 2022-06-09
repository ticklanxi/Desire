package com.example.display_156_box.activity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.display_156_box.R;

public class VideoActivity extends AppCompatActivity {
    private VideoView videoView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        videoView = (VideoView) findViewById(R.id.videoView);
        String uri = "android.resource://" + getPackageName() + "/" + R.raw.my_video_file;
        videoView.setVideoURI(Uri.parse(uri));
        videoView.start();
    }
}
