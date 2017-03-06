package com.example.signincouse.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.example.administrator.signincouse.R;
import com.example.push.DBHelper;
import com.example.push.DBManager;
import com.example.push.bean.MessageBody;
import com.example.push.util.PreferencesUtils;
import com.example.push.util.XmppUtil;
import com.example.signincouse.MyApplication;
import com.example.signincouse.toolclass.JSONUtil;

import org.jivesoftware.smack.XMPPException;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AskForLeaveActivity extends AppCompatActivity {

    private MessageBody messageBody;
    private String msg;
    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_for_leave);
        dbManager = new DBManager(this);
        EditText contentEt = (EditText) findViewById(R.id.leave_content);
        String username = PreferencesUtils.getSharePreStr(this, "user");
        String content = contentEt.getText().toString();
        messageBody = new MessageBody();
        messageBody.setTime(getCurrentTime());
        messageBody.setTitle("请假条");
        messageBody.setContent(content);
        messageBody.setExtra("");
        messageBody.setOrigin(username);
        messageBody.setToUser("teacher");
        msg = JSONUtil.toJSON(messageBody);

        findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            XmppUtil.sendMessage(MyApplication.xmppConnection, msg, "teacher");
                            dbManager.insert(DBHelper.TABLE_MSG, messageBody);
                        } catch (XMPPException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }

    private String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        return formatter.format(curDate);
    }
}
