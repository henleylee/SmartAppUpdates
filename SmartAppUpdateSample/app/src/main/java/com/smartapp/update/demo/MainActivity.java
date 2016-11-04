package com.smartapp.update.demo;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.smartapp.update.DiffUtils;
import com.smartapp.update.PatchUtils;
import com.smartapp.update.utils.SignUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    /**
     * 旧版本APK
     */
    private String oldApkPath;
    /**
     * 新版本APK
     */
    private String newApkPath;
    /**
     * patch包存储路径
     */
    private String patchFilePath;
    /**
     * 使用旧版本APK+patch包，合成的新版本APK
     */
    private String old2NewApkPath;
    private TextView tvLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.action_diff).setOnClickListener(this);
        findViewById(R.id.action_patch).setOnClickListener(this);
        tvLog = (TextView) findViewById(R.id.action_log);

        String filePath = Environment.getExternalStoragePublicDirectory("AppSmartUpdates").getAbsolutePath();
        oldApkPath = filePath + File.separator + "oldVersion.apk";
        patchFilePath = filePath + File.separator + "diffPatch.patch";
        newApkPath = filePath + File.separator + "newVersion.apk";
        old2NewApkPath = filePath + File.separator + "old2NewVersion.apk";
        tvLog.setText("开始复制文件...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean copyOld = copy(getApplicationContext(), "oldVersionApk.apk", oldApkPath);
                boolean copyNew = copy(getApplicationContext(), "newVersionApk.apk", newApkPath);
                if (copyOld && copyNew) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvLog.setText("文件复制完毕！！！");
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        if (!new File(oldApkPath).exists() || !new File(newApkPath).exists()) {
            Toast.makeText(this, "正在复制文件，请稍后再试...", Toast.LENGTH_SHORT).show();
            return;
        }
        switch (v.getId()) {
            case R.id.action_diff: // 生成差分包
                apkDiff();
                break;
            case R.id.action_patch: // 合并新包
                apkPatch();
                break;
        }
    }

    /**
     * 由新旧apk包生成差分包
     */
    private void apkDiff() {
        tvLog.setText("开始生成差分包，请等待...");
        Log.i(TAG, "开始生成差分包，请等待...");
        long start = System.currentTimeMillis();
        int genDiff = DiffUtils.genDiff(oldApkPath, newApkPath, patchFilePath);
        long end = System.currentTimeMillis();
        long interval = (end - start) / 1000;
        String result = genDiff == 0 ? "成功" : "失败";
        Log.i(TAG, "生成差分包结果：" + result + "\n路径：" + patchFilePath + "\n耗时：" + interval + "秒");
        tvLog.setText("生成差分包结果：" + result + "\n路径：" + patchFilePath + "\n耗时：" + interval + "秒");
    }

    /**
     * 使用旧版apk包和差分包合并成新包
     */
    private void apkPatch() {
        tvLog.setText("开始合成新包，请等待...");
        Log.i(TAG, "开始合成新包，请等待...");
        long start = System.currentTimeMillis();
        int patchResult = PatchUtils.patch(oldApkPath, old2NewApkPath, patchFilePath);
        long end = System.currentTimeMillis();
        long interval = (end - start) / 1000;
        String result = patchResult == 0 ? "成功" : "失败";
        String md5ByFile = SignUtils.getMd5ByFile(new File(newApkPath));
        boolean checkMd5 = SignUtils.checkMd5(new File(old2NewApkPath), md5ByFile);
        Log.i(TAG, "合成新包结果：" + result + "\n路径：" + old2NewApkPath + "\n耗时：" + interval + "秒" + "\nMd5值比对：" + checkMd5);
        tvLog.setText("合成新包结果：" + result + "\n路径：" + old2NewApkPath + "\n耗时：" + interval + "秒" + "\nMd5值比对：" + checkMd5);
    }


    /**
     * @param context
     * @param assetsName 要复制的文件名
     * @param fileName    要保存的路径
     */
    public static boolean copy(Context context, String assetsName, String fileName) {
        File file = new File(fileName);
        File dir = new File(file.getParent());
        if (!dir.exists())// 如果目录不中存在，创建这个目录
            dir.mkdir();
        try {
            if (!(new File(fileName)).exists()) {
                InputStream is = context.getResources().getAssets().open(assetsName);
                FileOutputStream fos = new FileOutputStream(fileName);
                byte[] buffer = new byte[7168];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    static {
        System.loadLibrary("SmartAppUpdates");// 库名为libSmartAppUpdates.so
    }
}
