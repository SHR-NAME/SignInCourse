package com.example.administrator.signincouse.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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

public class FragmentNotice extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
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
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_notice, container, false);
        initView();
        bindView();
        setContent();
        setListener();
        return rootView;
    }

    private void initView() {
        mSwipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeLayout);
        mListView = (ListView) rootView.findViewById(R.id.lv_notice_list);
    }

    private void bindView() {
        //设置 刷新圈的颜色
        mSwipeLayout.setColorSchemeResources(R.color.swipe_scheme_color);
        //设置 刷新圈的大小
        mSwipeLayout.setSize(SwipeRefreshLayout.LARGE);
        mSwipeLayout.setProgressBackgroundColorSchemeResource(R.color.swipe_bg_color);
        mSwipeLayout.setProgressViewOffset(false, 0, 24);
        mSwipeLayout.setProgressViewEndTarget(true, 200);

        mCourseList = new ArrayList<>();
        mNoticeListAdapter = new NoticeListAdapter(getActivity(), mCourseList);
        mListView.setAdapter(mNoticeListAdapter);
    }

    private void setContent() {
        getNoticeList();
    }

    private void setListener(){
        mSwipeLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        getNoticeList();
        mSwipeLayout.setRefreshing(false);
    }

    private void getNoticeList(){
        AsyncHttpUtil.get("getAllCourse", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONObject msg = response.optJSONObject("msg");
                    JSONArray course = msg.optJSONArray("course");
                    Log.i("HZWING", ">>>>>>>>>>>>>>" + course.toString());
                    setNoticeList(course);
                    mNoticeListAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setNoticeList(JSONArray course){
         for (int i = 0; i < course.length(); i++) {
             JSONObject obj = null;
             try {
                 obj = (JSONObject) course.get(i);
                 Course c = new Course();
                 c.setCourseID(obj.getInt("courseID"));
                 c.setCourseName(obj.getString("courseName"));
                 c.setCourseTeachter(obj.getString("courseTeachter"));
                 c.setCourseLocation(obj.getString("courseLocation"));
                 c.setCourseWeekday(obj.getInt("courseWeekday"));
                 c.setBeginTime(obj.getString("beginTime"));
                 c.setEndTime(obj.getString("endTime"));
                 c.setStartWeek(obj.getInt("startWeek"));
                 c.setEndWeek(obj.getInt("endWeek"));
                 c.setLocationLongitude(obj.getDouble("locationLongitude"));
                 c.setLocationLatitude(obj.getDouble("locationLatitude"));
                 c.setCourseCredit(obj.getInt("courseCredit"));
                 c.setClassName(obj.getString("className"));
                 mCourseList.add(c);
             } catch (JSONException e) {
                 e.printStackTrace();
             }
         }

    }
}
