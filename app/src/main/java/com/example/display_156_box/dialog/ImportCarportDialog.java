package com.example.display_156_box.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.display_156_box.R;
import com.example.display_156_box.adapter.FileAdapter;
import com.example.display_156_box.adapter.RecyclerViewDivider;
import com.example.display_156_box.utils.AppUtils;
import com.example.display_156_box.utils.Constants;
import com.example.display_156_box.utils.ToastUtil;


import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author ...
 * @date 2021-04-08 16:20
 * description：
 */
public class ImportCarportDialog extends Dialog {

    public static final String TAG = ImportCarportDialog.class.getSimpleName();

    @BindView(R.id.rv_fileList)
    RecyclerView rvFileList;
    @BindView(R.id.tv_fileImport)
    TextView fileImport;

    private String usbPath = "";
    private Context context;
    private String titleStr;

    public ImportCarportDialog(@NonNull Context context, String usbPath) {
        super(context, R.style.dialog);
        if (TextUtils.isEmpty(usbPath)) {
            titleStr = "文件管理(长按删除)";
            this.usbPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/BFHY/MDA";
        } else {
            this.usbPath = usbPath + "/import";
        }
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_import_carport);
        ButterKnife.bind(this);
        if (!TextUtils.isEmpty(usbPath)) {
            initUsbData(usbPath);
        }
        if (!TextUtils.isEmpty(titleStr)) fileImport.setText(titleStr);
    }

    List<File> files;
    FileAdapter adapter;

    private void initUsbData(String path) {
        File usbFile = new File(path);
        if (usbFile.exists()) {
            files = FileUtils.listFilesInDir(path);
            Log.d(TAG, "initUsbData: " + files);
            adapter = new FileAdapter(files);
            rvFileList.setAdapter(adapter);
            rvFileList.setLayoutManager(new LinearLayoutManager(context));
            rvFileList.addItemDecoration(new RecyclerViewDivider(context, LinearLayoutManager.HORIZONTAL));
            adapter.setOnItemClickListener(new FileAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(File file) {
                    dealOptions(file);
                }

                @Override
                public void onItemLongClick(File file) {
                    if (usbPath.contains("import")) {
                        return;
                    }
                    AffirmDialog affirmDialog = new AffirmDialog(getContext());
                    affirmDialog.setContent("删除" + file.getName() + "？");
                    affirmDialog.setOnAffirmListener(new AffirmDialog.OnAffirmListener() {
                        @Override
                        public void onAffirmClick() {
                            if (FileUtils.delete(file)) {
                                ToastUtils.showShort("删除成功");
                            } else {
                                ToastUtils.showShort("删除失败");
                            }
                            files.clear();
//                            File f = new File(usbPath);
//                            files.addAll(convertList(f.listFiles()));
                            List<File> listFiles = FileUtils.listFilesInDir(path);
                            Collections.reverse(listFiles);
                            files.addAll(listFiles);
                            adapter.notifyDataSetChanged();
                        }
                    });
                    affirmDialog.show();
                }
            });
        } else {
            Log.d(TAG, "initUsbData: " + path);
        }
    }

    private void dealOptions(File file) {
        Log.d(TAG, "onItemClick: " + file.toString());

        String name = file.getName();
//        String s = null;
//        if (!name.contains(".jpg") && !name.contains(".png") && !name.contains(".jepg")) {
//            s = AppUtils.readTxtFile(file.getAbsolutePath());
//            if (TextUtils.isEmpty(s)) {
//                Log.d(TAG, "onItemClick: s is null");
//                return;
//            }
//        }
        if (name.equals("aaa.mp4")) {
            String maskPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/BFHY/MDA/aaa.mp4";
            FileUtils.delete(maskPath);
            boolean isSuccess = FileUtils.copy(file.getAbsolutePath(), maskPath);
            if (isSuccess) {
                ToastUtil.toast(context, "视频导入成功");
            } else {
                ToastUtil.toast(context, "视频导入失败");
            }
            return;
        }
        if (name.equals("bbb.mp4")) {
            String maskPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/BFHY/MDA/bbb.mp4";
            FileUtils.delete(maskPath);
            boolean isSuccess = FileUtils.copy(file.getAbsolutePath(), maskPath);
            if (isSuccess) {
                ToastUtil.toast(context, "视频导入成功");
            } else {
                ToastUtil.toast(context, "视频导入失败");
            }
            return;
        }
        if (name.equals("ccc.mp4")) {
            String maskPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/BFHY/MDA/ccc.mp4";
            FileUtils.delete(maskPath);
            boolean isSuccess = FileUtils.copy(file.getAbsolutePath(), maskPath);
            if (isSuccess) {
                ToastUtil.toast(context, "视频导入成功");
            } else {
                ToastUtil.toast(context, "视频导入失败");
            }
            return;
        }



//        if (name.equals("mask.jpg")) {
//            String maskPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/BFHY/Mask/mask.jpg";
//            String maskDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/BFHY/Mask";
//            FileUtils.deleteFilesInDir(maskDir);
//            boolean isSuccess = FileUtils.copy(file.getAbsolutePath(), maskPath);
//            if (isSuccess) {
//                ToastUtil.toast(context, "图片导入成功");
//            } else {
//                ToastUtil.toast(context, "图片导入失败");
//            }
//            return;
//        }
//
//        if (name.equals("mask.png")) {
//            String maskPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/BFHY/Mask/mask.png";
//            String maskDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/BFHY/Mask";
//            FileUtils.deleteFilesInDir(maskDir);
//            boolean isSuccess = FileUtils.copy(file.getAbsolutePath(), maskPath);
//            if (isSuccess) {
//                ToastUtil.toast(context, "图片导入成功");
//            } else {
//                ToastUtil.toast(context, "图片导入失败");
//            }
//            return;
//        }
//
        ToastUtil.toast(getContext(), "请确保导入的文件文件名正确");
    }

    @OnClick(R.id.imgDismiss)
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.imgDismiss:
                dismiss();
                break;
        }
    }

    private List<File> convertList(File[] files) {
        List<File> list = new ArrayList<>();
        for (File file : files) {
            list.add(file);
        }
        return list;
    }


    private File[] myFile(File file) {

        String[] ss = file.list();
        if (ss == null) return null;
        int n = ss.length;
        File[] fs = new File[n];
        for (int i = 0; i < n; i++) {
            try {
                String str = new String(ss[i].getBytes("UTF8"));
                fs[i] = new File(str);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return fs;
    }
}
