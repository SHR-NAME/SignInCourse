package com.shr.push;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.shr.push.listener.CheckConnectionListener;
import com.shr.push.util.PreferencesUtils;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Presence;

public class PushService extends Service {

    private XMPPConnectionManager mXMPPConnectionManager;
    private XMPPConnection mXMPPConnection;

    private CheckConnectionListener mCheckConnectionListener;

    private String mUserName, mPassword;

    public PushService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mUserName = PreferencesUtils.getSharePreStr(this, "user");
        mPassword = PreferencesUtils.getSharePreStr(this, "password");

        mXMPPConnectionManager = XMPPConnectionManager.getInstance();
        initXMPPTask();
    }

    private void initXMPPTask() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    mXMPPConnection = mXMPPConnectionManager.init();						//初始化XMPPConnection
                    loginXMPP();															//登录XMPP
                    ChatManager chatmanager = mXMPPConnection.getChatManager();
                    chatmanager.addChatListener(new ChatManagerListener() {
                        @Override
                        public void chatCreated(Chat arg0, boolean arg1) {
//                            arg0.addMessageListener(new MsgListener(MsfService.this, mNotificationManager));
                        }
                    });
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 登录XMPP
     */
    void loginXMPP() {
        try {
            mXMPPConnection.connect();
            try{
                if(mCheckConnectionListener !=null){
                    mXMPPConnection.removeConnectionListener(mCheckConnectionListener);
                    mCheckConnectionListener =null;
                }
            }catch(Exception e){

            }
            mXMPPConnection.login(mUserName, mPassword);
            if(mXMPPConnection.isAuthenticated()){//登录成功
                System.out.println("PushService.loginXMPP success");
//                QQApplication.xmppConnection=mXMPPConnection;
//                sendLoginBroadcast(true);
//                //添加xmpp连接监听
//                mCheckConnectionListener =new CheckConnectionListener(this);
//                mXMPPConnection.addConnectionListener(mCheckConnectionListener);
//                // 注册好友状态更新监听
//                friendsPacketListener=new FriendsPacketListener(this);
//                PacketFilter filter = new AndFilter(new PacketTypeFilter(Presence.class));
//                mXMPPConnection.addPacketListener(friendsPacketListener, filter);
            }else{
//                sendLoginBroadcast(false);
                stopSelf();                                                                                        //如果登录失败，自动销毁Service
            }
        } catch (Exception e) {
            e.printStackTrace();
//            sendLoginBroadcast(false);
            stopSelf();
        }
    }
}
