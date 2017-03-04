package com.shr.push;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.shr.push.util.PreferencesUtils;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.muc.MultiUserChat;


public class PushService extends Service {

    private XMPPConnection mXMPPConnection;
    private String mUserName, mPassword;
    private XMPPManager mXMPPManager;
    private boolean isTeacher;
    private MultiUserChat multiUserChat;

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
        isTeacher = "teacher".equalsIgnoreCase(PreferencesUtils.getSharePreStr(this, "permission"));

        Log.i("xmpp", "初始化IM");
        mXMPPManager = XMPPManager.getInstance();
        mXMPPConnection = mXMPPManager.init();
        initXMPP();
    }

    private void initXMPP() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mXMPPConnection.connect();
                    Log.i("xmpp", "建立连接是否成功" + mXMPPConnection.isConnected());
                    mXMPPConnection.login(mUserName, mPassword);
                    Log.i("xmpp", "登陆IM成功！");
                    // 设置登录状态：在线
                    Presence presence = new Presence(Presence.Type.available);
                    mXMPPConnection.sendPacket(presence);
                    Log.i("xmpp", "service name:" + mXMPPConnection.getServiceName());

                    initGroup();
                    if (!isTeacher) {
                        multiUserChat.join(mUserName);
                    }

                } catch (XMPPException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    /**
     * 初始化组
     *
     * @throws Exception
     */
    private void initGroup() throws Exception {
        //创建聊天室
        multiUserChat = new MultiUserChat(mXMPPConnection, "teacher@conference." + Contants.XMPP_HOST);
        multiUserChat.create(mUserName);
        Log.i("xmpp", "创建聊天室成功");
        multiUserChat.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));
        //监听消息
        multiUserChat.addMessageListener(new PacketListener() {
            @Override
            public void processPacket(Packet packet) {
                Message message = (Message) packet;
                Log.i("xmpp", "收到的消息：" + message.getFrom() + " : " + message.getBody());
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (mXMPPConnection != null) {
                mXMPPConnection.disconnect();
                mXMPPConnection = null;
            }
            if (mXMPPManager != null) {
                mXMPPManager = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
