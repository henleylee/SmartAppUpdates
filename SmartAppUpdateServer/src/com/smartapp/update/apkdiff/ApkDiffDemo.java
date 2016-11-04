package com.smartapp.update.apkdiff;


import com.smartapp.update.DiffUtils;
import com.smartapp.update.common.Constants;

/**
 * 类说明：  新旧apk包，生成差分包实例
 * 
 * @author liyunlong
 * @date 2016/11/4 10:51
 */
public class ApkDiffDemo {

	public static void main(String[] args) {
		
		long start = System.currentTimeMillis();
		
		System.out.println("开始生成差分包，请等待...");

		int genDiff = DiffUtils.genDiff(Constants.OLD_APK, Constants.NEW_APK, Constants.PATCH_FILE);

		long end = System.currentTimeMillis();

		long interval = (end - start) / 1000;

		String result = genDiff == 0 ? "成功" : "失败";

		System.out.println("生成差分包成功：" + Constants.PATCH_FILE + result + "，耗时：" + interval + "秒");
	}

	static {
		System.loadLibrary("SmartAppUpdateServer");
	}
}
