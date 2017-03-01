package com.shr.push;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.XMPPConnection;

/**
 * 连接管理器
 * <p>
 * Created by shi hao on 2017/3/1.
 */

public class XMPPConnectionManager {

    private static XMPPConnectionManager XMPPConnectionManager;

    static {
        try {
            Class.forName("org.jivesoftware.smack.ReconnectionManager");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private XMPPConnectionManager() {

    }

    public static XMPPConnectionManager getInstance() {
        if (XMPPConnectionManager == null) {
            XMPPConnectionManager = new XMPPConnectionManager();
        }
        return XMPPConnectionManager;
    }

    /**
     * 初始化
     *
     * @return 返回一个XmppConnection
     */
    public XMPPConnection init() {

        ConnectionConfiguration connectionConfig = new ConnectionConfiguration(Contants.XMPP_HOST, Contants.XMPP_PORT);
        // connectionConfig.setSASLAuthenticationEnabled(false);//
        // 不使用SASL验证，设置为false
        // connectionConfig
        // .setSecurityMode(ConnectionConfiguration.SecurityMode.enabled);
        // 允许自动连接
        connectionConfig.setReconnectionAllowed(true);
        // 允许登陆成功后更新在线状态
        connectionConfig.setSendPresence(true);

        // 收到好友邀请后manual表示需要经过同意,accept_all表示不经同意自动为好友
        Roster.setDefaultSubscriptionMode(Roster.SubscriptionMode.accept_all);
        XMPPConnection connection = new XMPPConnection(connectionConfig);
        return connection;
    }

}
