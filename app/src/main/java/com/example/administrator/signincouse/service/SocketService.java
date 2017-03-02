package com.example.administrator.signincouse.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

/**
 * 开启service
 */
@Deprecated
public class SocketService extends Service {

    public static final String SOCKET_ACTION = "com.shr.socket.action";
    public static final String SOCKET_RECEIVE = "com.shr.socket.receive";
    private SocketReceiver mSocketReceiver;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("service", "socket service created");
        mSocketReceiver = new SocketReceiver(this);

        IntentFilter filter = new IntentFilter();
        filter.addAction(SOCKET_ACTION);
        registerReceiver(mSocketReceiver, filter);

        SocketAcceptThread socketAcceptThread = new SocketAcceptThread(this);
        // 开启 Socket 监听线程
        socketAcceptThread.start();
    }


    @Override
    public void onStart(Intent intent, int startId) {
        Log.d("service", "socket service start");

    }


    @Override
    public void onDestroy() {
        Log.d("service", "socket service destroy!");
        unregisterReceiver(mSocketReceiver);
    }

}
