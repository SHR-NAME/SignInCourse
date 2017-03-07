package com.example.signincouse.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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

    private DBManager dbManager;

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Toast.makeText(AskForLeaveActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                    break;
                case -1:
                    Toast.makeText(AskForLeaveActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    private String username;
    private EditText contentEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_for_leave);
        dbManager = new DBManager(this);
        contentEt = (EditText) findViewById(R.id.leave_content);
        username = PreferencesUtils.getSharePreStr(this, "user");

        findViewById(R.id.for_leave_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String content = contentEt.getText().toString();

                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(AskForLeaveActivity.this, "内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                sendMsg(content);

            }
        });
    }

    private void sendMsg(String content) {
        final MessageBody messageBody = new MessageBody();
        messageBody.setTime(getCurrentTime());
        messageBody.setTitle("请假条");
        messageBody.setContent(content);
        messageBody.setExtra("");
        messageBody.setOrigin(username);
        messageBody.setToUser("teacher");
        final String msg = JSONUtil.toJSON(messageBody);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    XmppUtil.sendMessage(MyApplication.xmppConnection, msg, "teacher");
                    dbManager.insert(DBHelper.TABLE_MSG, messageBody);
                    handler.sendEmptyMessage(1);
                } catch (XMPPException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(-1);
                }
            }
        }).start();
    }

    private String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        return formatter.format(curDate);
    }
}
