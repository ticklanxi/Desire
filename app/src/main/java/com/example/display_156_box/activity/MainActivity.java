package com.example.display_156_box.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.blankj.utilcode.util.SPUtils;
import com.easysocket.EasySocket;
import com.easysocket.config.DefaultMessageProtocol;
import com.easysocket.config.EasySocketOptions;
import com.easysocket.entity.OriginReadData;
import com.easysocket.entity.SocketAddress;
import com.easysocket.interfaces.conn.ISocketActionListener;
import com.easysocket.interfaces.conn.SocketActionListener;
import com.easysocket.utils.LogUtil;
import com.example.display_156_box.R;
import com.example.display_156_box.bean.TestMessage;
import com.example.display_156_box.dialog.AffirmDialog;
import com.example.display_156_box.dialog.SpecialTipsDialog;
import com.example.display_156_box.socket.CallbackIDFactoryImpl;
import com.example.display_156_box.utils.CrashHandler;
import com.example.display_156_box.utils.EventBusUtils;
import com.example.display_156_box.utils.GenericToast;
import com.example.display_156_box.utils.KnowDialog;
import com.example.display_156_box.utils.StatusBarUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener   {

    private GenericToast genericToast;
    private TextView cnmwrq;
    private Button cnm;
    private Button cnmwc;
    private Button cnmwc2;
    private VideoView videoView;
    // ???????????????
    private boolean isConnected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.transparencyBar(this);
        setContentView(R.layout.activity_main);

        cnm = findViewById(R.id.caonima);
        cnmwc = findViewById(R.id.caonimaw);
        cnmwc2 = findViewById(R.id.caonimaw2);
        cnm.setOnClickListener(this);
        cnmwc.setOnClickListener(this);
        cnmwc2.setOnClickListener(this);
        videoView = (VideoView) findViewById(R.id.videoView);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initEasySocket();
        // ??????socket??????
        EasySocket.getInstance().subscribeSocketAction(socketActionListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // ??????socket??????
            case R.id.caonima: {
                startActivity(new Intent(this, ConfigActivity.class));

            }
            break;
            case R.id.caonimaw: {
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/BFHY/MDA/aaa.mp4");
                if (file.exists()) {
                    videoView.setVideoPath(file.getAbsolutePath());
                } else {
                    Toast.makeText(this, "?????????????????????????????????", Toast.LENGTH_SHORT).show();
                }
                videoView.setFocusable(true);
                videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.start();
                    }
                });
            }
            break;
            case R.id.caonimaw2: {
                sendTestMessage();
            }
            break;

        }

    }

    private void makeVideoControl(){
        videoView.setOnCompletionListener((MediaPlayer mediaPlayer) -> System.out.println("????????????"));
        String uri = "android.resource://" + getPackageName() + "/" + R.raw.my_video_file;
        videoView.setVideoURI(Uri.parse(uri));
        videoView.start();
    }


    /**
     * ????????????????????????
     */
    private void sendTestMessage() {
        TestMessage testMessage = new TestMessage();
        testMessage.setMsgId("test_msg");
        testMessage.setFrom("android");
        // ?????????????????????
        EasySocket.getInstance().upMessage(testMessage.pack());
    }

    private void initEasySocket() {
        EasySocketOptions options = new EasySocketOptions.Builder()
                // ?????????????????????????????????IP????????????getString???????????????????????????????????????IP??????
                .setSocketAddress(new SocketAddress(getResources().getString(R.string.local_ip), 9999))
                .setCallbackIDFactory(new CallbackIDFactoryImpl())
                // ????????????????????????????????? socket????????????????????????????????????????????????????????????????????????
                // ???????????????????????????????????????????????????????????????????????????????????????????????????????????????
                .setReaderProtocol(new DefaultMessageProtocol())
                .setHeartbeatFreq(5000)
                .build();

        // ?????????
        EasySocket.getInstance()
                .createConnection(options, this);// ????????????socket??????

    }
    /**
     * socket????????????
     */
    private ISocketActionListener socketActionListener = new SocketActionListener() {
        /**
         * socket????????????
         * @param socketAddress
         */
        @Override
        public void onSocketConnSuccess(SocketAddress socketAddress) {
            LogUtil.d("??????" + socketAddress.getPort() + "---> ????????????");
            Toast.makeText(MainActivity.this, socketAddress.getPort() + "??????" + "socket?????????", Toast.LENGTH_SHORT).show();
            isConnected = false;
        }

        /**
         * socket????????????
         * @param socketAddress
         * @param isNeedReconnect ??????????????????
         */
        @Override
        public void onSocketConnFail(SocketAddress socketAddress, boolean isNeedReconnect) {
            Toast.makeText(MainActivity.this, socketAddress.getPort() + "??????" + "socket???????????????", Toast.LENGTH_SHORT).show();
            isConnected = false;
        }

        /**
         * socket????????????
         * @param socketAddress
         * @param isNeedReconnect ??????????????????
         */
        @Override
        public void onSocketDisconnect(SocketAddress socketAddress, boolean isNeedReconnect) {
            LogUtil.d(socketAddress.getPort() + "??????" + "---> socket????????????????????????????????????" + isNeedReconnect);
            isConnected = false;
            Toast.makeText(MainActivity.this, socketAddress.getPort() + "??????" + "socket???????????????", Toast.LENGTH_SHORT).show();
        }

        /**
         * socket???????????????
         * @param socketAddress
         * @param readData
         */
        @Override
        public void onSocketResponse(SocketAddress socketAddress, String readData) {
//            LogUtil.d(socketAddress.getPort() + "??????" + "SocketActionListener????????????-->" + readData);

        }

        @Override
        public void onSocketResponse(SocketAddress socketAddress, OriginReadData originReadData) {
            super.onSocketResponse(socketAddress, originReadData);
//            LogUtil.d(socketAddress.getPort() + "??????" + "SocketActionListener????????????-->" + originReadData.getBodyString());
            TestMessage msg = new Gson().fromJson(originReadData.getBodyString(), TestMessage.class);
            Log.d("cc","????????????"+msg.getMsgId());

        }
    };



}
