package com.example.signincouse;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.shr.filehelper.MyApp;

import org.jivesoftware.smack.XMPPConnection;

/**
 * Created by bangzhu on 2016/8/24.
 */
public class MyApplication extends MyApp {
    private LocationClient bdLocationClient;
    public static XMPPConnection xmppConnection;
    @Override
    public void onCreate() {
        super.onCreate();
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        bdLocationClient = new LocationClient(getApplicationContext());
    }

    public LocationClient getBdLocationClient(){
        return bdLocationClient;
    }
}
