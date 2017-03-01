package com.shr.push.listener;

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
        // TODO Auto-generated method stub

    }

    @Override
    public void connectionClosedOnError(Exception e) {
        if (e.getMessage().equals("stream:error (conflict)")) {

        }
    }

    @Override
    public void reconnectingIn(int arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void reconnectionFailed(Exception arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void reconnectionSuccessful() {
        // TODO Auto-generated method stub

    }

}
