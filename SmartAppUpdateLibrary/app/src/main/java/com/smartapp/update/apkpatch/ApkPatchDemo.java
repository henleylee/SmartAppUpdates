package com.smartapp.update.apkpatch;

import com.smartapp.update.PatchUtils;
import com.smartapp.update.common.Constants;

/**
 * 类说明：  使用旧版apk包+差分包，合并新包实例
 *
 * @author liyunlong
 * @date 2016/11/4 10:51
 */
public class ApkPatchDemo {

    public static void main(String[] args) {

        long start = System.currentTimeMillis();

        System.out.println("开始合成新包，请等待...");

        int patchResult = PatchUtils.patch(Constants.OLD_APK, Constants.OLD_2_NEW_APK, Constants.PATCH_FILE);

        long end = System.currentTimeMillis();

        long interval = (end - start) / 1000;

        String result = patchResult == 0 ? "成功" : "失败";

        System.out.println("合成新包成功：" + Constants.NEW_APK + result + "，耗时：" + interval + "秒");
    }

    static {
        System.loadLibrary("ApkPatchLibraryServer");
    }
}