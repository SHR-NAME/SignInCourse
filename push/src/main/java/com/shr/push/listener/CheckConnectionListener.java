package com.shr.push.listener;

import android.util.Log;

import com.shr.push.PushService;

import org.jivesoftware.smack.ConnectionListener;

/**
 * 检测链接状态
 *
 * Created by shi hao on 2017/3/1.
 */

public class CheckConnectionListener implements ConnectionListener {

    private PushService context;

    public CheckConnectionListener(PushService context){
        this.context=context;
    }

    @Override
    public void connectionClosed() {
        Log.i("connect", "connectionClosed");
    }

    @Override
    public void connectionClosedOnError(Exception e) {
        Log.i("connect", "connectionClosedOnError");
        e.printStackTrace();
        if (e.getMessage().equals("stream:error (conflict)")) {

        }
    }

    @Override
    public void reconnectingIn(int arg0) {
        Log.i("connect", "reconnectingIn");

    }

    @Override
    public void reconnectionFailed(Exception arg0) {
        Log.i("connect", "reconnectionFailed");
    }

    @Override
    public void reconnectionSuccessful() {
        Log.i("connect", "reconnectionSuccessful");
    }

}
