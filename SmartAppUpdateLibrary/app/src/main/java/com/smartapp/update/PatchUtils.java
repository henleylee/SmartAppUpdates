package com.smartapp.update;

/**
 * APK Patch工具类
 *
 * @author liyunlong
 * @date 2016/9/13 13:35
 */
public class PatchUtils {

    /**
     * native方法：使用路径为oldApkPath的apk与路径为patchPath的补丁包，合成新的apk，并存储于newApkPath
     *
     * @param oldApkPath 示例:/sdcard/old.apk
     * @param newApkPath 示例:/sdcard/new.apk
     * @param patchPath  示例:/sdcard/xx.patch
     * @return 0表示操作成功
     */
    public static native int patch(String oldApkPath, String newApkPath, String patchPath);

    static {
        System.loadLibrary("libSmartAppUpdates");// 之前在build.gradle里面设置的so名字，必须一致
    }

}
