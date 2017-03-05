package com.example.signincouse.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.signincouse.R;
import com.example.push.Constant;
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

//import com.example.push.util.XmppUtil;

public class FragmentNotice extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private DBManager dbManager;
    private boolean isTeacher;
    private String mUserName;

    public FragmentNotice() {
        // Required empty public constructor
    }

    public static FragmentNotice newInstance(int sectionNumber) {
        FragmentNotice fragment = new FragmentNotice();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbManager = new DBManager(getActivity());
        isTeacher = "teacher".equalsIgnoreCase(PreferencesUtils.getSharePreStr(getActivity(), "permission"));
        mUserName = PreferencesUtils.getSharePreStr(getActivity(), "user");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notice, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final EditText titleEt = (EditText) view.findViewById(R.id.title);
        final EditText contentEt = (EditText) view.findViewById(R.id.content);
        Button sendBtn = (Button) view.findViewById(R.id.send);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isTeacher) {
                    Toast.makeText(getActivity(), "您不是老师，不能群发消息", Toast.LENGTH_SHORT).show();
                    return;
                }
                String title = titleEt.getText().toString();
                String content = contentEt.getText().toString();
                if (TextUtils.isEmpty(title)) {
                    Toast.makeText(getContext(), "标题不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(getContext(), "内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                sendMsg(title, content);
            }
        });
    }

    private void sendMsg(final String title, final String content) {
        MessageBody body = new MessageBody();
        final String currentTime = getCurrentTime();
        body.setTime(currentTime);
        body.setTitle(title);
        body.setContent(content);
        body.setFrom(mUserName);
        body.setExtra("");
        final String msg = JSONUtil.toJSON(body);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    XmppUtil.sendGroupMessage(MyApplication.xmppConnection, msg, Constant.GROUP_JID);
                    dbManager.insert(DBHelper.TABLE_TEACHER, mUserName, title, content, currentTime, "");
                } catch (XMPPException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss ");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

}
