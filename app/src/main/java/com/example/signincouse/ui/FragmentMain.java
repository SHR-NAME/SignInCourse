package com.example.signincouse.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.administrator.signincouse.R;
import com.shr.filehelper.mvp.FileMainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FragmentMain extends Fragment implements AdapterView.OnItemClickListener {
    //四个按钮
    private RelativeLayout qianDao, qingJia, lianXi, ziYuan;
    private ArrayList<HashMap<String, String>> list = null;
    private ListtodayCourseAdapter listtodayCourseAdapter;
    private ListView list_todayCourse;
    private HashMap<String, String> map = null;
    //轮播时间间隔
    private final static int TIME_LunBo = 3;
    //自动轮播启动的使能
    private static boolean isAutoPlay = true;
    //当前的轮播页
    //定时任务
    private ScheduledExecutorService scheduledExecutorService;
    private int currentItem = 0;
    private static final String ARG_SECTION_NUMBER = "section_number";
    private ImageView[] points;
    private int[] ids = {R.id.select_point1, R.id.select_point2, R.id.select_point3, R.id.select_point4};
    private int[] lunBoPhoto;
    private ViewPager viewPager;
    private View myView;
    private List<View> views;

    public FragmentMain() {
    }

    public static FragmentMain newInstance(int sectionNumber) {
        FragmentMain fragment = new FragmentMain();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopPlay();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_main, container, false);
        //轮播
        viewPager = (ViewPager) myView.findViewById(R.id.MainviewPager);
        views = new ArrayList<View>();
        initPhoto();
        initPoints();//要在views加载完后调用
        viewPager.setAdapter(new FraMainPhotoPagerAdapter(views, getActivity()));
        viewPager.setFocusable(true);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentItem = position;
                for (int i = 0; i < ids.length; i++) {
                    if (position == i) points[i].setImageResource(R.drawable.pointlunbopress);
                    else points[i].setImageResource(R.drawable.pointlunbo);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case 0://重新回到什么都不做状态
                        if (viewPager.getCurrentItem() == viewPager.getAdapter().getCount() - 1 && !isAutoPlay) {
                            viewPager.setCurrentItem(0);
                        } else if (viewPager.getCurrentItem() == 0 && !isAutoPlay) {
                            viewPager.setCurrentItem(viewPager.getAdapter().getCount() - 1);
                        }
                        break;
                    case 1://当状态变成滑动
                        isAutoPlay = false;
                        break;
                    case 2://切换中
                        isAutoPlay = true;
                        break;
                }
            }
        });
        if (isAutoPlay) {
            startPlay();
        }
        //按钮qianDao,qingJia,lianXi,ziYuan
        initButton();
        /////////////////////////////////////////////////////今日课程
        list = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < 3; i++) {
            map = new HashMap<String, String>();       //为避免产生空指针异常，有几列就创建几个map对象
            map.put("textTime", "08:30-10:00");
            map.put("textName", "计算机导论和毛概结合");
            map.put("textAdd", "南201");
            list.add(map);
        }
        listtodayCourseAdapter = new ListtodayCourseAdapter(getActivity(), list);
        list_todayCourse = (ListView) myView.findViewById(R.id.list_todayCourse);
        list_todayCourse.setAdapter(listtodayCourseAdapter);
        list_todayCourse.setOnItemClickListener(this);
        return myView;
    }

    private void initButton() {

        qianDao = (RelativeLayout) myView.findViewById(R.id.qianDao);
        qianDao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到签到页面
                Intent intent = new Intent(getActivity(), TeacherSignActivity.class);
                startActivity(intent);
            }
        });
        qingJia = (RelativeLayout) myView.findViewById(R.id.qingJia);
        qingJia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AskForLeaveActivity.class);
                startActivity(intent);
            }
        });
        lianXi = (RelativeLayout) myView.findViewById(R.id.lianXi);
        lianXi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MessageListActivity.class);
                startActivity(intent);
            }
        });
        ziYuan = (RelativeLayout) myView.findViewById(R.id.ziYuan);
        ziYuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FileMainActivity.class);
                startActivity(intent);
            }
        });
    }

    /*
    * 轮播图片数据*/
    private void initPhoto() {
        //TODO:异步加载图片
        lunBoPhoto = new int[]{
                R.drawable.newsphoto1,
                R.drawable.newsphoto2,
                R.drawable.newsphoto3,
                R.drawable.newsphoto4
        };
        for (int i : lunBoPhoto) {
            ImageView imageView = new ImageView(getActivity());
            imageView.setImageResource(i);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            views.add(imageView);
        }
    }

    /*
    * 分别实例化轮播时用于显示当前图片位置的Imageview(小圆点)*/
    private void initPoints() {
        points = new ImageView[views.size()];
        for (int i = 0; i < views.size(); i++) {
            points[i] = (ImageView) myView.findViewById(ids[i]);
        }
    }

    //listview的点击事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getActivity(), "点击了" + (position + 1) + "项", Toast.LENGTH_SHORT).show();
    }

    private class AutoLunBoTask implements Runnable {
        @Override
        public void run() {
            synchronized (viewPager) {//一次只能一个线程进入本对象
                currentItem = (currentItem + 1) % views.size();
                handler.obtainMessage().sendToTarget();//发送处理请求
            }
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {//处理请求
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            viewPager.setCurrentItem(currentItem);
        }

    };

    /**
     * 开始轮播图切换
     */
    private void startPlay() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        //初始延迟3秒，两次之间3秒
        scheduledExecutorService.scheduleAtFixedRate(new AutoLunBoTask(), 3, 3, TimeUnit.SECONDS);
    }

    /**
     * 停止轮播图切换
     */
    private void stopPlay() {
        scheduledExecutorService.shutdown();
    }

}
