package com.example.push.listener;

import com.example.push.DBHelper;
import com.example.push.DBManager;
import com.example.push.PushManager;
import com.example.push.PushService;
import com.example.push.bean.MessageBody;
import com.example.signincouse.toolclass.JSONUtil;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

//import com.example.push.PushService;

/**
 * 消息监听
 * <p>
 * Created by shi hao on 2017/3/5.
 */

public class ChatMsgListener implements MessageListener {

    private PushManager pushManager;
    private DBManager dbManager;

    public ChatMsgListener(PushService context) {
        pushManager = new PushManager(context);
        dbManager = new DBManager(context);
    }

    @Override
    public void processMessage(Chat chat, Message message) {
        String body = message.getBody();
        MessageBody messageBody = JSONUtil.getObjFromJson(body, MessageBody.class);
        dbManager.insert(DBHelper.TABLE_MSG, messageBody);
        pushManager.buildAndNotifyMsg(messageBody.getTitle(), messageBody.getContent());
    }
}
