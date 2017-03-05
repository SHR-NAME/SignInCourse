package com.example.push;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.push.listener.ChatMsgListener;
import com.example.push.util.PreferencesUtils;
import com.example.signincouse.MyApplication;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.HostedRoom;
import org.jivesoftware.smackx.muc.MultiUserChat;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;


public class PushService extends Service {

    private XMPPConnection mXMPPConnection;
    private String mUserName, mPassword;
    private XMPPManager mXMPPManager;

    private boolean isGroupExist;

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

        Log.i("xmpp", "初始化IM");
        if (MyApplication.xmppConnection == null) {
            mXMPPManager = XMPPManager.getInstance();
            mXMPPConnection = mXMPPManager.getConnection();
        }
        initXMPP();
    }

    private void initXMPP() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mXMPPConnection.connect();
                    Log.i("xmpp", "建立连接是否成功" + mXMPPConnection.isConnected());
                    MyApplication.xmppConnection = mXMPPConnection;
                    mXMPPConnection.login(mUserName, mPassword);
                    Log.i("xmpp", "登陆IM成功！");
                    // 设置登录状态：在线
                    Presence presence = new Presence(Presence.Type.available);
                    mXMPPConnection.sendPacket(presence);
                    Log.i("xmpp", "service name:" + mXMPPConnection.getServiceName());

                    Collection<HostedRoom> ServiceCollection = MultiUserChat
                            .getHostedRooms(mXMPPConnection, "conference." + Constant.XMPP_HOST);

                    for (HostedRoom s : ServiceCollection) {
                        if (s.getJid().equals(Constant.GROUP_JID)) {
                            isGroupExist = true;
                            break;
                        }
                    }

                    createGroup();
                    joinGroup(mUserName);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    /**
     * 加入组
     */
    private void joinGroup(String user) {
        try {
            // 使用XMPPConnection创建一个MultiUserChat窗口
            MultiUserChat muc = new MultiUserChat(mXMPPConnection, Constant.GROUP_JID);
            // 聊天室服务将会决定要接受的历史记录数量
            DiscussionHistory history = new DiscussionHistory();
            history.setMaxChars(0);
            history.setSince(new Date());
            // 用户加入聊天室
            muc.join(user, null, history, SmackConfiguration.getPacketReplyTimeout());
            Log.i("xmpp", "会议室【teacher】加入成功........");

            ChatManager chatManager = mXMPPConnection.getChatManager();
            chatManager.addChatListener(new ChatManagerListener() {
                @Override
                public void chatCreated(Chat chat, boolean b) {
                    chat.addMessageListener(new ChatMsgListener(PushService.this));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("xmpp", "会议室【teacher】加入失败........");
        }

    }

    /**
     * 初始化组
     *
     * @throws Exception
     */
    private void createGroup() throws Exception {
        if (isGroupExist) {
            return;
        }
        //创建聊天室
        MultiUserChat multiUserChat = new MultiUserChat(mXMPPConnection, "teacher@conference." + Constant.XMPP_HOST);
        multiUserChat.create("teacher");
        Log.i("xmpp", "创建聊天室成功");
        // 获得聊天室的配置表单
        Form form = multiUserChat.getConfigurationForm();
        // 根据原始表单创建一个要提交的新表单。
        Form submitForm = form.createAnswerForm();
        // 向要提交的表单添加默认答复
        for (Iterator fields = form.getFields(); fields.hasNext(); ) {
            FormField field = (FormField) fields.next();
            if (!FormField.TYPE_HIDDEN.equals(field.getType())
                    && field.getVariable() != null) {
                // 设置默认值作为答复
                submitForm.setDefaultAnswer(field.getVariable());
            }
        }
        // 设置聊天室是持久聊天室，即将要被保存下来
        submitForm.setAnswer("muc#roomconfig_persistentroom", true);
        // 房间仅对成员开放
        submitForm.setAnswer("muc#roomconfig_membersonly", false);
        // 允许占有者邀请其他人
        submitForm.setAnswer("muc#roomconfig_allowinvites", true);
        // 设置描述
        submitForm.setAnswer("muc#roomconfig_roomdesc", "teacher");
        // 登录房间对话
        submitForm.setAnswer("muc#roomconfig_enablelogging", true);
        // 仅允许注册的昵称登录
        submitForm.setAnswer("x-muc#roomconfig_reservednick", false);
        // 允许使用者修改昵称
        submitForm.setAnswer("x-muc#roomconfig_canchangenick", true);
        // 允许用户注册房间
        submitForm.setAnswer("x-muc#roomconfig_registration", true);
        // 发送已完成的表单（有默认值）到服务器来配置聊天室
        multiUserChat.sendConfigurationForm(submitForm);

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
