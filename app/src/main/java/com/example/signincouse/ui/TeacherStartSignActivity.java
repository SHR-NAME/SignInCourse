package com.example.signincouse.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aigestudio.wheelpicker.WheelPicker;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.example.signincouse.MyApplication;
import com.example.signincouse.model.Course;
import com.example.administrator.signincouse.R;
import com.example.signincouse.toolclass.AsyncHttpUtil;
import com.example.signincouse.toolclass.JSONUtil;
import com.example.signincouse.toolclass.NetWorkUtil;
import com.example.signincouse.toolclass.Contants;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by bangzhu on 2016/8/24.
 */
public class TeacherStartSignActivity extends Activity implements View.OnClickListener {
    //百度地图相关
    private MapView mMapView;
    private BaiduMap mBaiduMap = null;
    private LocationClient mLocationClient;
    private GeoCoder mGeoCoder;
    private MyLocationListener myLocationListener;
    private SDKReceiver mReceiver;
    private double mLongitude;
    private double mLatitude;
    private String mDescription;
    private boolean isFirstLoc = true;
    //滚轮选择器 选中的位置
    private int mCoursePosition;

    private LinearLayout mNetworkError;
    //弹出对话框相关
    private AlertDialog mAlert;
    private AlertDialog.Builder mBuilder;
    private View mCustomView;
    private EditText mSignCourse, mSignClass;
    private WheelPicker mCourseChioce, mClassChioce, mWeekChioce;
    private List<String> mCourseList, mClassList, mWeekList;
    private List<Course> mCourseListSet;
    private Button mStartSign;
    private SharedPreferences mPreferences;
    private boolean isNeedToClass = false;
    private StaticHandler mHandler = new StaticHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_startsign);
        initView();
        bindView();
        initLocation();
        initMarker();
        setBaiduMapListener();
        setListener();
    }

    private void initView() {
        mMapView = (MapView) findViewById(R.id.bmap_teacher_sign);
        mBaiduMap = mMapView.getMap();

        mNetworkError = (LinearLayout) findViewById(R.id.ll_network_error);
        mSignCourse = (EditText) findViewById(R.id.et_sign_course);
        mSignClass = (EditText) findViewById(R.id.et_sign_class);

        mStartSign = (Button) findViewById(R.id.btn_startSign);
    }

    private void bindView() {
        mCourseList = new ArrayList<>();
        mClassList = new ArrayList<>();
        mWeekList = new ArrayList<>();
        mCourseListSet = new ArrayList<>();
        mPreferences = getSharedPreferences("login", MODE_PRIVATE);
    }

    private void initLocation() {
        //获取定位核心类
        mLocationClient = ((MyApplication) getApplication()).getBdLocationClient();
        //初始化定位监听接口
        myLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myLocationListener);
        //设置地图参数
        LocationClientOption option = new LocationClientOption();
        //设置坐标系
        option.setCoorType("bd09ll");
        //设置需要使用地址
        option.setIsNeedAddress(true);
        //设置需要使用位置人性化描述
        option.setIsNeedLocationDescribe(true);
        //设置定位模式
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //设置是否打开gps
        option.setOpenGps(true);
        //设置刷新间隔
        option.setScanSpan(3000);
        mLocationClient.setLocOption(option);
    }

    private void initMarker() {
        mGeoCoder = GeoCoder.newInstance();
        OnGetGeoCoderResultListener getGeoCoderResultListener = new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
                //获取地理编码结果
            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                //获取反向地理编码结果
                String str = reverseGeoCodeResult.getAddress();
                addOverlay(reverseGeoCodeResult);
                Toast.makeText(TeacherStartSignActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        };
        mGeoCoder.setOnGetGeoCodeResultListener(getGeoCoderResultListener);
    }

    private void setBaiduMapListener() {
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                //地图内 Poi 点击事件回调函数
                LatLng clickLoc = mapPoi.getPosition();
                ReverseGeoCodeOption reverseGeoCodeOption = new ReverseGeoCodeOption();
                reverseGeoCodeOption.location(clickLoc);
                mGeoCoder.reverseGeoCode(reverseGeoCodeOption);
                return false;
            }
        });

        //注册广播监听器
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
        iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
        mReceiver = new SDKReceiver();
        registerReceiver(mReceiver, iFilter);

        //设置地图加载完成监听
        mBaiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                if(NetWorkUtil.getInstance(TeacherStartSignActivity.this).isNetWorkConnected()){
                    mNetworkError.setVisibility(View.GONE);
                }else{
                    mNetworkError.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void setListener() {
        mStartSign.setOnClickListener(this);
        mSignCourse.setOnClickListener(this);
        mSignClass.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_startSign:
                if(NetWorkUtil.getInstance(TeacherStartSignActivity.this).isNetWorkConnected()){
                    mNetworkError.setVisibility(View.GONE);
                    if(TextUtils.isEmpty(mSignCourse.getText().toString()) || TextUtils.isEmpty(mSignClass.getText().toString())){
                        Toast.makeText(TeacherStartSignActivity.this, "课程或班级为空，请重新选择！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    teacherStartSign();
                }else{
                    mNetworkError.setVisibility(View.VISIBLE);
                    Toast.makeText(TeacherStartSignActivity.this, "网络连接失败！", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.et_sign_course:
            case R.id.et_sign_class:
                showChooseDialog();
                break;
        }
    }

    //添加覆盖物
    private void addOverlay(ReverseGeoCodeResult reverseGeoCodeResult){
        mBaiduMap.clear();
        LatLng latLng = reverseGeoCodeResult.getLocation();
        BitmapDescriptor iconMarker = BitmapDescriptorFactory.fromResource(R.drawable.loc_marker);
        OverlayOptions overlayOptions = new MarkerOptions().position(latLng)
                .icon(iconMarker).zIndex(5);
        Marker marker = (Marker) mBaiduMap.addOverlay(overlayOptions);
        MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.setMapStatus(update);
        TextView location = new TextView(getApplicationContext());
        location.setBackgroundResource(R.drawable.marker_tip);
        location.setTextColor(getResources().getColor(R.color.primary_white));
        location.setPadding(30, 40, 30, 50);
        location.setText(reverseGeoCodeResult.getAddress());
        Point p = mBaiduMap.getProjection().toScreenLocation(latLng);
        p.y -= 45;
        p.x -= 35;
        LatLng ll = mBaiduMap.getProjection().fromScreenLocation(p);
        InfoWindow infoWindow = new InfoWindow(location, ll, 10);
        mBaiduMap.showInfoWindow(infoWindow);
    }

    //地图监听接口
    class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation == null) {
                return;
            }
            // 构造定位数据
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(bdLocation.getRadius())
                    .direction(100).latitude(bdLocation.getLatitude())
                    .longitude(bdLocation.getLongitude()).build();
            // 设置定位数据
            mBaiduMap.setMyLocationData(locData);

            //更新经纬度
            mLongitude = bdLocation.getLongitude();
            mLatitude = bdLocation.getLatitude();

            mDescription = bdLocation.getLocationDescribe();

            if (isFirstLoc) {
                //获取坐标点
                LatLng ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
                MapStatusUpdate mapUpdate = MapStatusUpdateFactory.newLatLngZoom(ll, 16);
                mBaiduMap.animateMapStatus(mapUpdate);
                isFirstLoc = false;
            }
        }
    }

    //地图广播接收器
    class SDKReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
                //网络出错，相应处理
                mNetworkError.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     *  以下为其他工具方法
     */
    private void loadCourseAndClass() {
        int userID = mPreferences.getInt("userID", 0);
        String permission = mPreferences.getString("permission", "teacher");
        RequestParams params = new RequestParams();
        params.put("userID", userID);
        params.put("permission", permission);
        AsyncHttpUtil.get(Contants.GET_ALL_COURSE, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONObject msg = response.optJSONObject("msg");
                Log.i("HZWING", ">>>>>>>>>>" + response.toString());
                try {
                    int count = msg.getInt("count");
                    Message msg1 = new Message();
                    if (count > 0) {
                        JSONArray course = msg.optJSONArray("course");
                        for (int i = 0; i < course.length(); i++) {
                            Course c = new Course();
                            JSONUtil.setValue(c, (JSONObject) course.get(i));
                            mCourseListSet.add(c);
                            mCourseList.add(c.getCourseName());
                            mClassList.add(c.getClassName());
                            mWeekList.add(c.getCourseWeekday() + "");
                        }
                        mCourseChioce.setData(mCourseList);
                        mClassChioce.setData(mClassList);
                        mWeekChioce.setData(mWeekList);
                        msg1.what = 12138;
                    } else {
                        msg1.what = 1768;
                    }
                    mHandler.sendMessage(msg1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //List<Course> courseList = JSonUtil.getJsonList(course.toString(), Course.class);
//                for (int i = 0; i < course.length(); i++) {
//                    try {
//                        JSONObject obj = (JSONObject) course.get(i);
//                        mCourseList.add(obj.getString("courseName"));
//                        mClassList.add(obj.getString("className"));
//                        mWeekList.add(obj.getInt("courseWeekday") + "");
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//                mCourseChioce.setData(mCourseList);
//                mClassChioce.setData(mClassList);
//                mWeekChioce.setData(mWeekList);
            }
        });
    }

    //打开滚轮选择器对话框
    private void showChooseDialog() {
        mBuilder = new AlertDialog.Builder(this);
        mCustomView = getLayoutInflater().inflate(R.layout.wheel_picker_layout, null, false);
        mCourseChioce = (WheelPicker) mCustomView.findViewById(R.id.wp_course);
        mClassChioce = (WheelPicker) mCustomView.findViewById(R.id.wp_class);
        mWeekChioce = (WheelPicker) mCustomView.findViewById(R.id.wp_week);
        //设置滚轮选择器样式
        setWheelStyle();
        loadCourseAndClass();
//        if(isNeedToClass) {
            mBuilder.setView(mCustomView);
//        }else {
//            mBuilder.setMessage(getResources().getText(R.string.not_need_to_class));
//        }
        mBuilder.setCancelable(true);
        mBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mCoursePosition = mCourseChioce.getCurrentItemPosition();
                int classPosition = mClassChioce.getCurrentItemPosition();
                String _course, _class;
                if(mCourseList.size() == 0){
                    _course = "";
                }else{
                    _course = mCourseList.get(mCoursePosition);
                }
                if(mClassList.size() == 0) {
                    _class = "";
                }else {
                    _class = mClassList.get(classPosition);
                }
                mSignCourse.setText(_course);
                mSignClass.setText(_class);
            }
        });
        mBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAlert.dismiss();
            }
        });
        mAlert = mBuilder.create();
        mAlert.show();
    }

    //设置滚轮选择器样式
    private void setWheelStyle() {
        mCourseChioce.setCyclic(true);
        mCourseChioce.setIndicator(true);
        mCourseChioce.setIndicatorColor(R.color.indecator_color);
        mCourseChioce.setItemTextSize((int) getResources().getDimension(R.dimen.wheel_size));
        mCourseChioce.setCurtain(true);
        mCourseChioce.setCurtainColor(R.color.curtain_color);
        mCourseChioce.setAtmospheric(true);

        mClassChioce.setCyclic(true);
        mClassChioce.setIndicator(true);
        mClassChioce.setIndicatorColor(R.color.indecator_color);
        mClassChioce.setItemTextSize((int) getResources().getDimension(R.dimen.wheel_size));
        mClassChioce.setCurtain(true);
        mClassChioce.setCurtainColor(R.color.curtain_color);
        mClassChioce.setAtmospheric(true);

        mWeekChioce.setCyclic(true);
        mWeekChioce.setIndicator(true);
        mWeekChioce.setIndicatorColor(R.color.indecator_color);
        mWeekChioce.setItemTextSize((int) getResources().getDimension(R.dimen.wheel_size));
        mWeekChioce.setCurtain(true);
        mWeekChioce.setCurtainColor(R.color.curtain_color);
        mWeekChioce.setAtmospheric(true);
    }

    //教师发起考勤
    private void teacherStartSign() {
        int userId = mPreferences.getInt("userID", 0);
        int scheduleId = mCourseListSet.get(mCoursePosition).getScheduleId();
        double locationLatitude = mCourseListSet.get(mCoursePosition).getLocationLatitude();
        double locationLongitude = mCourseListSet.get(mCoursePosition).getLocationLongitude();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        long curTime = System.currentTimeMillis();
        String beginTime = df.format(curTime);
        String endTime = df.format(curTime + 10 * 60 * 1000);
        RequestParams params = new RequestParams();
        params.put("userID", userId);
        params.put("scheduleId", scheduleId);
        params.put("locationLatitude", mLatitude);
        params.put("locationLongitude", mLongitude);
        params.put("beginTime", beginTime);
        params.put("endTime", endTime);
        AsyncHttpUtil.get(Contants.CREATE_ATTENDANCE, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i("HZWING", "创建成功 " + response.toString() + "===========");

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("HZWING", ">>>>>>>>>>>>>" + responseString);
            }
        });
    }

    public void setIsNeedToClass(boolean flag) {
        isNeedToClass = flag;
    }

    @Override
    protected void onStart() {
        super.onStart();
        //开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        //开始定位
        mLocationClient.start();
        //开启室内定位
        mLocationClient.startIndoorMode();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mBaiduMap.setMyLocationEnabled(false);
        mLocationClient.stop();
        mLocationClient.stopIndoorMode();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        mGeoCoder.destroy();
        unregisterReceiver(mReceiver);
    }

    static class StaticHandler extends Handler {
        private WeakReference<TeacherStartSignActivity> activityWeakReference;

        public StaticHandler(TeacherStartSignActivity activity) {
            activityWeakReference = new WeakReference<TeacherStartSignActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            TeacherStartSignActivity activity = activityWeakReference.get();
            if(activity != null) {
                switch (msg.what) {
                    case 12138:
                        activity.setIsNeedToClass(true);
                        break;
                    case 1768:
                        activity.setIsNeedToClass(false);
                        break;
                }
            }
        }
    }
}

