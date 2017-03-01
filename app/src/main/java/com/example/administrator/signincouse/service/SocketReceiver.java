package com.example.administrator.signincouse.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static com.example.administrator.signincouse.service.SocketService.SOCKET_RECEIVE;

/**
 *
 * Created by shi hao on 2017/2/25.
 */

public class SocketReceiver extends BroadcastReceiver {

    private PushManager manager;

    public SocketReceiver(Context context) {
        manager = new PushManager(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(SOCKET_RECEIVE)) {
            String url = intent.getExtras().getString("action");
            if (url.equals("ClientIP")) {
                String strIP = intent.getExtras().getString("content");
            } else if (url.equals("RcvStr")) {
                String strContent = intent.getExtras().getString("content");
                System.out.println("SocketReceiver.onReceive" + strContent);
                manager.buildMsg(strContent);
            } else if (url.equals("Disconnect")) {
                String strContent = intent.getExtras().getString("content");
            }
        }
    }
}

