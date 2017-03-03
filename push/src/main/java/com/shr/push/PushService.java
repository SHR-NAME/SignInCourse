package com.shr.push;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.shr.push.util.PreferencesUtils;
import com.shr.push.util.XmppUtil;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.muc.HostedRoom;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.RoomInfo;

import java.util.Collection;
import java.util.List;

public class PushService extends Service {

    private XMPPConnection mXMPPConnection;
    private String mUserName, mPassword;
    private XMPPManager mXMPPManager;
    private boolean isTeacher;

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
                    //获取聊天室
                    List<String> col = XmppUtil.getConferenceServices(mXMPPConnection.getServiceName(), mXMPPConnection);
                    Log.i("xmpp", "聊天室个数" + col.size());
                    for (Object aCol : col) {
                        String service = (String) aCol;
                        //查询服务器上的聊天室
                        Collection<HostedRoom> rooms = MultiUserChat.getHostedRooms(mXMPPConnection, service);
                        for (HostedRoom room : rooms) {
                            //查看Room消息
                            Log.i("xmpp", room.getName() + " - " + room.getJid());
                            RoomInfo roomInfo = MultiUserChat.getRoomInfo(mXMPPConnection, room.getJid());
                            Log.i("xmpp", roomInfo.getOccupantsCount() + " : " + roomInfo.getSubject());
                        }
                    }
                    //获取身份，只有老师才能创建群
                    if (isTeacher) {
                        //创建聊天室
                        MultiUserChat multiUserChat = new MultiUserChat(mXMPPConnection, mUserName + "@conference." + Contants.XMPP_HOST);
//                        multiUserChat.join(mUserName);
                        multiUserChat.create(mUserName);
                        multiUserChat.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));
                        Log.i("xmpp", "创建聊天室成功");
                    } else {
                        //遍历已有聊天室，学生加入聊天室
                    }
                } catch (XMPPException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

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
