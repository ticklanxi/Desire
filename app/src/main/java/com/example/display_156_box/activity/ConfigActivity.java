package com.example.display_156_box.activity;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;

import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.display_156_box.R;
import com.example.display_156_box.bean.MessageEvent;
import com.example.display_156_box.dialog.ImportCarportDialog;
import com.example.display_156_box.receiver.ExternalStorageReceiver;
import com.example.display_156_box.service.VirtualKeyService;

import com.example.display_156_box.utils.AppConstant;
import com.example.display_156_box.utils.Constant;
import com.example.display_156_box.utils.Constants;
import com.example.display_156_box.utils.EventBusUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import com.example.display_156_box.utils.SPUtils;
import com.example.display_156_box.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConfigActivity extends AppCompatActivity {
    private Messenger messenger;
    private ExternalStorageReceiver externalStorageReceiver;
    private String usbPath = "";
    public static final String TAG = ConfigActivity.class.getSimpleName();
    private boolean showHome = false;
    RadioGroup secOptionsContainer;
    @BindView(R.id.rbSec0)
    RadioButton rbSec0;
    @BindView(R.id.rbSec1)
    RadioButton rbSec1;
    @BindView(R.id.rbSec2)
    RadioButton rbSec2;
    @BindView(R.id.rbSec3)
    RadioButton rbSec3;
    @BindView(R.id.edit1)
    EditText ipText;
    @BindView(R.id.edit2)
    EditText portText;
    private int sectionSelected;

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            messenger = new Messenger(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        EventBusUtils.registerEventBus(this);
        ButterKnife.bind(this);
        Intent intent = new Intent(this, VirtualKeyService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        //注册USB监听广播
        registerBroadCast();
        sectionSelected = SPUtils.getInt(this, AppConstant.SECTION,0);
        switch (sectionSelected){
            case 0:rbSec0.setChecked(true); break;
            case 1:rbSec1.setChecked(true); break;
            case 2:rbSec2.setChecked(true); break;
            case 3:rbSec3.setChecked(true); break;
        }
        ipText.setText(SPUtils.getString(this,AppConstant.SOCKET_IP,""));
        portText.setText(SPUtils.getString(this,AppConstant.SOCKET_PORT,""));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }



    @OnClick({R.id.tv_setting,R.id.tv_upan,R.id.tv_fileManger,R.id.tv_save, R.id.rbSec0, R.id.rbSec1, R.id.rbSec2, R.id.rbSec3,})

    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.tv_setting:
                showHome = true;
                if (TextUtils.equals("CM201-2-CH3", Build.MODEL)) {
                    Intent intent = new Intent("/");
                    ComponentName cm = new ComponentName("com.android.settings", "com.android.settings.Settings");
                    intent.setComponent(cm);
                    intent.setAction("android.intent.action.VIEW");
                    startActivityForResult(intent, 0);
                } else {
                    Intent intent = new Intent(Settings.ACTION_SETTINGS);
                    startActivity(intent);
                }
                break;
            case R.id.tv_upan:
                if (TextUtils.isEmpty(usbPath)) {
                    ToastUtil.toast(ConfigActivity.this, "请插入U盘");
                } else {
                    ImportCarportDialog importCarportDialog = new ImportCarportDialog(this, usbPath);
                    importCarportDialog.show();
                }
                break;
            case R.id.tv_fileManger:
                ImportCarportDialog importCarportDialog = new ImportCarportDialog(this, "");
                importCarportDialog.show();

                break;
            case R.id.tv_save:
                if (TextUtils.isEmpty(ipText.getText()))
                {
                    ToastUtil.toast(ConfigActivity.this, "请输入ip地址"); return;
                }
                if (TextUtils.isEmpty(portText.getText()))
                {
                    ToastUtil.toast(ConfigActivity.this, "请输入端口号"); return;
                }
                SPUtils.put(this,AppConstant.SOCKET_IP,ipText.getText());
                SPUtils.put(this,AppConstant.SOCKET_PORT,portText.getText());
                SPUtils.put(this,AppConstant.SECTION,sectionSelected);
                ToastUtil.toast(ConfigActivity.this, "保存成功");
                ipText.clearFocus();
                portText.clearFocus();

                break;
            case R.id.rbSec0:
                sectionSelected = 0;
                break;
            case R.id.rbSec1:
                sectionSelected = 1;
                break;
            case R.id.rbSec2:
                sectionSelected = 2;
                break;
            case R.id.rbSec3:
                sectionSelected = 3;
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        //当MainActivity由后台转到前台展示，隐藏悬浮球按钮
//        if (messenger != null) {
//            communicateWithVKService(Constants.INVISIBLE);
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //当MainActivity退到后台，显示悬浮球按钮
        if (messenger != null ) {
            communicateWithVKService(Constants.VISIBLE);
        }
    }

    /**
     * 与VirtualKeyService通信。多进程
     *
     * @param tag tag
     */
    private void communicateWithVKService(int tag) {
        Message message = Message.obtain(null, tag);
        Bundle bundle = new Bundle();
        message.setData(bundle);
        try {
            messenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterBroadCast();
        unbindService(serviceConnection);
        EventBusUtils.unregisterEventBus(this);
        if (messenger != null) {
            communicateWithVKService(Constants.INVISIBLE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        switch (event.getEventTag()) {

            case Intent.ACTION_MEDIA_MOUNTED: {
                usbPath = (String) event.getEventData();
                ToastUtil.toast(ConfigActivity.this, "U盘插入");
                break;
            }
            case Intent.ACTION_MEDIA_EJECT: {
                ToastUtil.toast(ConfigActivity.this, "U盘移除");
                usbPath = "";
                break;
            }
            case Constant.TEST:{
                Log.d(TAG,"测试");
                break;
            }
        }
    }

    /**
     * 注册外部存储监听广播
     */
    private void registerBroadCast() {
        if (externalStorageReceiver == null) {
            externalStorageReceiver = new ExternalStorageReceiver();
            IntentFilter iFilter = new IntentFilter();
            iFilter.addAction(Intent.ACTION_MEDIA_CHECKING);
            iFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
            iFilter.addAction(Intent.ACTION_MEDIA_EJECT);
            iFilter.setPriority(1000);
            iFilter.addDataScheme("file");
            registerReceiver(externalStorageReceiver, iFilter);
        }
    }

    /**
     * 解注册外部存储监听广播
     */
    private void unregisterBroadCast() {
        if (externalStorageReceiver != null) {
            unregisterReceiver(externalStorageReceiver);
            externalStorageReceiver = null;
        }
    }


}
