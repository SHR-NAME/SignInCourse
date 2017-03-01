package com.example.administrator.signincouse.ui;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.signincouse.R;
import com.example.administrator.signincouse.secondlayer.ChangeBackground;
import com.example.administrator.signincouse.secondlayer.CourseInfo;
import com.example.administrator.signincouse.secondlayer.DownloadCourse;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;

import cz.msebera.android.httpclient.Header;

public class FragmentCouse extends Fragment implements View.OnClickListener {
    private Button courseMenu_add;
    private HashMap<String, String> map;
    private ArrayList<HashMap<String, String>> list;
    private Button downloadCourse,changeBackground;
    private View myFragmentView;
    private PopupWindow WeekPopupWindow,AddPopupWindow;
    private TextView OrderOfWeek;
    private int isPopupWindow=0,isAddPopupWindow=0;
    private int courseNumHeight=50;
    private Calendar c;
    private int courseView_width,courseView_height;
    private static final String ARG_SECTION_NUMBER = "section_number";
    private int []courseWeedday={R.id.courseSun,
                                     R.id.courseMon,
                                     R.id.courseTue,
                                     R.id.courseWed,
                                     R.id.courseThu,
                                     R.id.courseFri,
                                     R.id.courseSat,
                                     };
    private static int weekDay;
    private Button courseMenuAdd;
    public FragmentCouse() {}
    public static FragmentCouse newInstance(int sectionNumber) {
        FragmentCouse fragment = new FragmentCouse();
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
        myFragmentView=inflater.inflate(R.layout.fragment_course, container, false);
        //动态设置自定义menu的高度
        courseMenuInstance();
        //设置当天是星期几
        setDayOfweek();
        //单节课程的宽高
        WidthAndHeight();
        //周数下拉框
        instancePopupWindows();
        instanceClick();
        list=new ArrayList<HashMap<String, String>>();
        downloadCourse();
        return myFragmentView;
    }
    public void downloadCourse(){//
        AsyncHttpClient client=new AsyncHttpClient();
        String myUrl="http://penderie.cn/penderie/test/getJson?url=getAllCourse";
        RequestParams params=new RequestParams("url","getAllCourse");
        client.get(myUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    JSONArray course = response.getJSONObject("msg").getJSONArray("course");
                    HashMap<String, String> map;
                    for (int i = 0; i < course.length(); i++) {
                        map = new HashMap<String, String>();
                        JSONObject oneCourse = course.getJSONObject(i);
                        System.out.println(list.size()+"<--------------");
                        map.put("name", oneCourse.getString("courseName"));
                        map.put("teacher", oneCourse.getString("courseTeachter"));
                        map.put("location", oneCourse.getString("courseLocation"));
                        map.put("weeks", oneCourse.getInt("startWeek") + "-" + oneCourse.getInt("endWeek"));
                        map.put("credit", oneCourse.getInt("courseCredit") + "");
                        map.put("courseWeekday", oneCourse.getInt("courseWeekday") + "");
                        map.put("order", oneCourse.getInt("order") + "");
                        map.put("number", oneCourse.getInt("number") + "");
                        list.add(map);
                    }
                    for (int cN = 0; cN < list.size(); cN++) {
                        addCourseView(Integer.parseInt(list.get(cN).get("courseWeekday")),
                                Integer.parseInt(list.get(cN).get("order")),
                                Integer.parseInt(list.get(cN).get("number")),
                                0x50c5F000,
                                list.get(cN).get("name"),
                                cN);//星期几，第几节开始上，一共上几节,颜色，课程名，标签
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void courseMenuInstance(){
        //动态设置自定义menu的高度
        courseMenuAdd= (Button) myFragmentView.findViewById(R.id.courseMenu_add);
        ViewGroup.LayoutParams layoutParams=courseMenuAdd.getLayoutParams();
        layoutParams.height=MainActivity.getScreenHeight()/21;
        layoutParams.width=MainActivity.getScreenWidth()/12;
        courseMenuAdd.setLayoutParams(layoutParams);
    }
    private void setDayOfweek(){
        c=Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        weekDay=c.get(Calendar.DAY_OF_WEEK);
        ColorDrawable drawable=new ColorDrawable(0x25F5F5F5);
        ColorDrawable drawable1=new ColorDrawable(0x10F5F5F5);
        for (int i=0;i<7;i++){
            if (i+1==weekDay){
                myFragmentView.findViewById(courseWeedday[i]).setBackground(drawable);
            }else {
                myFragmentView.findViewById(courseWeedday[i]).setBackground(drawable1);
            }

        }
    }
    private void WidthAndHeight(){
        courseView_width=MainActivity.getScreenWidth()*2/15;
        final float scale = getContext().getResources().getDisplayMetrics().density;
        courseView_height=(int) (courseNumHeight * scale + 0.5f);
    }
    private void addCourseView(int DayOfWeek,int OrderOfCourse,int courseNum,int viewColor,String courseName,int cN){//星期几，第几节开始上，一共上几节,颜色，课程名,标签
        Button button=new Button(getContext());
        button.setText(courseName);
        //利用课程匡的宽来设置字体大小
        button.setTextSize(courseView_width / 12);
        button.setTextColor(getContext().getResources().getColor(R.color.courseButtonText));
        button.setGravity(Gravity.CENTER);
        //利用课程匡的宽动态设置padding
        button.setPadding(courseView_width / 24, courseView_width / 20, courseView_width / 24, courseView_width / 20);
        ColorDrawable drawable1=new ColorDrawable(viewColor);
        button.setBackground(drawable1);
        RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(courseView_width,courseView_height*courseNum);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_START);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        button.setLayoutParams(layoutParams);
        layoutParams.leftMargin=courseView_width*(DayOfWeek-1);
        layoutParams.topMargin=courseView_height*(OrderOfCourse-1);
        System.out.println("宽" + courseView_width + "大宽" + courseView_height + "高" + courseView_height + "shishishi " + courseView_height * (OrderOfCourse - 1));
        RelativeLayout courseView= (RelativeLayout) myFragmentView.findViewById(R.id.courseView);
        button.setTag(cN);
        button.setOnClickListener(this);
        courseView.addView(button);
    }
    //周数弹框
    private void instancePopupWindows(){
        final View contentView=LayoutInflater.from(getContext()).inflate(R.layout.orderofweek_popupview,null);
        WeekPopupWindow=new PopupWindow(contentView);
        WeekPopupWindow.setWidth(MainActivity.getScreenWidth() / 3);
        WeekPopupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        WeekPopupWindow.setFocusable(true);
        //点击popupwindows之外返回
        contentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_UP){
                    if (isPopupWindow == 1) {
                        View view=contentView.findViewById(R.id.thePopup);
                        if ((int) event.getX()<view.getLeft()||(int) event.getX()>view.getRight()
                                ||(int) event.getY()<view.getTop()||(int) event.getY()>view.getBottom()){
                            isPopupWindow=0;
                            WeekPopupWindow.dismiss();
                        }
                    }
                }
                return true;
            }
        });
        //////////////////
        final View addContentView=LayoutInflater.from(getContext()).inflate(R.layout.courseadd_popupview,null);
        AddPopupWindow=new PopupWindow(addContentView);
        AddPopupWindow.setWidth(MainActivity.getScreenWidth() / 3);
        AddPopupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        AddPopupWindow.setFocusable(true);
        downloadCourse= (Button) addContentView.findViewById(R.id.downloadCourse);
        changeBackground= (Button) addContentView.findViewById(R.id.changeBackground);
        //点击popupwindows之外返回
        addContentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (isAddPopupWindow == 1) {
                        View view = addContentView.findViewById(R.id.theAddPopup);
                        if ((int) event.getX() < view.getLeft() || (int) event.getX() > view.getRight()
                                || (int) event.getY() < view.getTop() || (int) event.getY() > view.getBottom()) {
                            isAddPopupWindow = 0;
                            AddPopupWindow.dismiss();
                        }
                    }
                }
                return true;
            }
        });
    }
    //各种点击事件
    private void instanceClick(){
        OrderOfWeek= (TextView) myFragmentView.findViewById(R.id.OrderOfWeek);
        OrderOfWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (isPopupWindow == 0) {
                        isPopupWindow=1;
                        WeekPopupWindow.showAsDropDown(OrderOfWeek, 0, MainActivity.getScreenHeight() / 40);
                    }

            }
        });
        courseMenu_add= (Button) myFragmentView.findViewById(R.id.courseMenu_add);
        courseMenu_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAddPopupWindow==0){
                    isAddPopupWindow=1;
                    AddPopupWindow.showAsDropDown(courseMenu_add,0,MainActivity.getScreenHeight()/40);
                }
            }
        });
        downloadCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddPopupWindow.dismiss();
                isAddPopupWindow=0;
                startActivity(new Intent(getContext(), DownloadCourse.class));
            }
        });
        changeBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddPopupWindow.dismiss();
                isAddPopupWindow=0;
                startActivity(new Intent(getContext(), ChangeBackground.class));
            }
        });
    }
    //课程框点击回调
    @Override
    public void onClick(View v) {
        int cN= (int) v.getTag();
        HashMap<String, String> oneC=list.get(cN);
        //Todo:课程信息展示
        Bundle b=new Bundle();
        b.putString("name",oneC.get("name"));
        b.putString("teacher", oneC.get("teacher"));
        b.putString("location", oneC.get("location"));
        b.putString("weeks", oneC.get("weeds"));
        b.putString("credit", oneC.get("credit"));
        Intent intent=new Intent(getContext(), CourseInfo.class);
        intent.putExtras(b);
        startActivity(intent);
    }

}
