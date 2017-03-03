package com.example.administrator.signincouse.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.administrator.signincouse.R;
import com.example.administrator.signincouse.model.Course;
import com.example.administrator.signincouse.toolclass.AsyncHttpUtil;
import com.example.administrator.signincouse.toolclass.JSONUtil;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class FragmentNotice extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private View rootView;
    private SwipeRefreshLayout mSwipeLayout;
    private ListView mListView;
    private List<Course> mCourseList;
    private NoticeListAdapter mNoticeListAdapter;

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
                String msg = "";
                sendMsg(msg);
            }
        });
    }

    private void sendMsg(String content) {

    }

}
