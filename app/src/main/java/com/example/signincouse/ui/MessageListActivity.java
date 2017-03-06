package com.example.signincouse.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.administrator.signincouse.R;
import com.example.push.DBHelper;
import com.example.push.DBManager;
import com.example.push.bean.MessageBody;
import com.example.push.util.PreferencesUtils;

import java.util.List;

public class MessageListActivity extends AppCompatActivity {

    private boolean isTeacher;
    private MsgAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.msg_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        isTeacher = "teacher".equalsIgnoreCase(PreferencesUtils.getSharePreStr(this, "permission"));

        final DBManager manager = new DBManager(this);
        adapter = new MsgAdapter();

        new AsyncTask<String, Void, List<MessageBody>>(){
            @Override
            protected List<MessageBody> doInBackground(String... params) {
                return manager.query(params[0], null);
            }

            @Override
            protected void onPostExecute(List<MessageBody> messageBodies) {
                super.onPostExecute(messageBodies);
                adapter.addData(messageBodies);
            }
        }.execute(DBHelper.TABLE_MSG);

        recyclerView.setAdapter(adapter);

    }
}
