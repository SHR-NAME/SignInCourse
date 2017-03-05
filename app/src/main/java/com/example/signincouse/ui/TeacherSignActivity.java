package com.example.signincouse.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import com.example.administrator.signincouse.R;
import com.example.signincouse.model.StudentSign;
import com.example.signincouse.toolclass.AsyncHttpUtil;
import com.example.signincouse.toolclass.JSONUtil;
import com.example.signincouse.toolclass.Contants;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.uuzuche.lib_zxing.activity.CaptureActivity;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by bangzhu on 2016/8/24.
 */
public class TeacherSignActivity extends Activity implements View.OnClickListener,MaterialSpinner.OnItemSelectedListener, SwipeRefreshLayout.OnRefreshListener{
    private ImageView iv_locationSign, iv_codeSign, iv_startSign;

    //联动下拉框
    private MaterialSpinner s_signTime, s_signCourse, s_signCondition;
    private List<String> signTimeList, signCourseList, signConditionList;
    private PieChart mPieChart;

    private SwipeRefreshLayout swipeRefreshLayout;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_sign);
        initView();
        bindView();
        setContents();
        setListeners();
    }

    private void initView() {
        iv_locationSign  = (ImageView) findViewById(R.id.iv_locationSign);
        iv_codeSign = (ImageView) findViewById(R.id.iv_codeSign);
        iv_startSign = (ImageView) findViewById(R.id.iv_startSign);
        s_signTime = (MaterialSpinner) findViewById(R.id.s_signTime);
        s_signCourse = (MaterialSpinner) findViewById(R.id.s_signCourse);
        s_signCondition = (MaterialSpinner) findViewById(R.id.s_signCondition);
        mPieChart = (PieChart) findViewById(R.id.piechart);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
    }

    private void bindView(){
        signTimeList = new ArrayList<String>();
        signCourseList = new ArrayList<String>();
        signConditionList = new ArrayList<String>();
        //饼状图
        mPieChart.addPieSlice(new PieModel("Freetime", 15, Color.parseColor("#FE6DA8")));
        mPieChart.addPieSlice(new PieModel("Sleep", 25, Color.parseColor("#56B7F1")));
        mPieChart.addPieSlice(new PieModel("Work", 35, Color.parseColor("#CDA67F")));
        mPieChart.addPieSlice(new PieModel("Eating", 9, Color.parseColor("#FED70E")));
        mPieChart.startAnimation();

        // 第一次进入页面的时候显示加载进度条
        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        swipeRefreshLayout.setColorSchemeResources(R.color.swipe_scheme_color);
        swipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE);
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.swipe_bg_color);
        swipeRefreshLayout.setProgressViewEndTarget(true, 200);

        preferences = getSharedPreferences("login", MODE_PRIVATE);
    }

    private void setContents() {
        String[] strSignTimeList = getResources().getStringArray(R.array.signTime);
        String[] strSignConditionList = getResources().getStringArray(R.array.signCondition);
        for(int i=0;i<strSignConditionList.length;i++){
            signTimeList.add(strSignTimeList[i]);
            signConditionList.add(strSignConditionList[i]);
        }
        s_signTime.setItems(signTimeList);
        s_signCondition.setItems(signConditionList);
        getTodayCourse();
        getAllStudentSign();
    }

    private void setListeners() {
        swipeRefreshLayout.setOnRefreshListener(this);
        iv_locationSign.setOnClickListener(this);
        iv_codeSign.setOnClickListener(this);
        iv_startSign.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.iv_locationSign:
                Intent intent = new Intent(TeacherSignActivity.this, StudentSignActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_codeSign:
                Intent it = new Intent(TeacherSignActivity.this, CaptureActivity.class);
                startActivityForResult(it, 0);
                break;
            case R.id.iv_startSign:
                Intent i = new Intent(TeacherSignActivity.this, TeacherStartSignActivity.class);
                startActivity(i);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            Bundle bundle = data.getExtras();
            String url = bundle.getString("result");
            Intent intent = new Intent(TeacherSignActivity.this, ShowZxingUrlActivity.class);
            intent.putExtra("url", url);
            startActivity(intent);
        }
    }

    @Override
    public void onRefresh() {
        //请求数据，刷新列表

        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {

    }

    /**
     *  工具方法
     */
    private void getTodayCourse(){
        int userID = preferences.getInt("userID", 0);
        String permission = preferences.getString("permission", "teacher");
        RequestParams params = new RequestParams();
        params.put("userID", userID);
        params.put("permission", permission);
        AsyncHttpUtil.get(Contants.GET_TODAY_COURSE, params,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            Log.i("HZWING", response.toString()+">>>>>>>>>>>");
//                            JSONObject msg = response.optJSONObject("msg");
//                            JSONArray jsonArray = msg.optJSONArray("course");
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject obj = (JSONObject) jsonArray.get(i);
//                                signCourseList.add(obj.getString("courseName"));
//                            }
//                            s_signCourse.setItems(signCourseList);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void getAllStudentSign(){
        AsyncHttpUtil.get("getAllStudentSign", null,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            JSONObject msg = response.optJSONObject("msg");
                            JSONArray jsonArray = msg.optJSONArray("studentSign");
                            List<StudentSign> studentSignList = JSONUtil.getJsonList(jsonArray.toString(), StudentSign.class);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
