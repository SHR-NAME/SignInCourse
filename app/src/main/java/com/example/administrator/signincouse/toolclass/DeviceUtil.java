package com.example.administrator.signincouse.toolclass;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 * Created by bangzhu on 2016/8/24.
 */
public class DeviceUtil {
    private static final String DEVICE_ID = "DEVICE_ID";

    /**
     * 获取手机设备ID
     * @param context
     * @return 设备ID
     */
    public static String getDeviceId(Context context){
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = telephonyManager.getDeviceId();
        if(deviceId == null || TextUtils.isEmpty(deviceId.trim())){
            deviceId = Settings.System.getString(context.getContentResolver(), DEVICE_ID);
        }
        if(deviceId == null || TextUtils.isEmpty(deviceId.trim())){
            deviceId = "android_" + System.currentTimeMillis();
            Settings.System.putString(context.getContentResolver(), DEVICE_ID, deviceId);
        }
        return deviceId;
    }

    /**
     * 获取手机型号
     * @return
     */
    public static String getPhoneType(){
        return Build.MODEL;
    }

    /**
     * 获取当前系统版本
     * @return
     */
    public static String getSystemVersion(){
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取当前SDK版本号
     * @return
     */
    public static int getSDKVersion(){
        return Build.VERSION.SDK_INT;
    }
}
