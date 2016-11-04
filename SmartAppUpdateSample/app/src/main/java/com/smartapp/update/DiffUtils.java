package com.smartapp.update;

/**
 * APK Diff工具类
 *
 * @author liyunlong
 * @date 2016/9/13 13:35
 */
public class DiffUtils {

    /**
     * native方法 比较路径为oldPath的apk与newPath的apk之间差异，并生成patch包，存储于patchPath
     *
     * @param oldApkPath 示例:/sdcard/old.apk
     * @param newApkPath 示例:/sdcard/new.apk
     * @param patchPath  示例:/sdcard/xx.patch
     * @return 0表示操作成功
     */
    public static native int genDiff(String oldApkPath, String newApkPath, String patchPath);

}