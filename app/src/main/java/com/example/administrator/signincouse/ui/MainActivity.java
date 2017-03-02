package com.example.administrator.signincouse.ui;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.signincouse.R;
import com.example.administrator.signincouse.service.SocketReceiver;
import com.example.administrator.signincouse.service.SocketService;
import com.shr.push.PushService;

import static com.example.administrator.signincouse.service.SocketService.SOCKET_RECEIVE;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private TabLayout tabLayout;
    public static int screenWidth, screenHeight;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private TextView mActionBarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取屏幕高度宽度
        initScreenWH();
        initActionBar();
        initUI();
        //启动服务
//        SocketReceiver socketReceiver = new SocketReceiver(this);
//        IntentFilter socketIntentFilter = new IntentFilter();
//        socketIntentFilter.addAction(SOCKET_RECEIVE);
//        registerReceiver(socketReceiver, socketIntentFilter);
//
//        // 启动  Socket 服务
//        startService(new Intent(this, SocketService.class));
        //启动核心Service
        Intent intent=new Intent(this,PushService.class);
        startService(intent);
    }

    private void initActionBar() {
//        ActionBar actionBar = getActionBar();
//        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_bg));
//        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM, ActionBar.DISPLAY_SHOW_CUSTOM);
//        actionBar.setDisplayShowTitleEnabled(false);
//        actionBar.setDisplayShowHomeEnabled(false);
//        View view = LayoutInflater.from(this).inflate(R.layout.actionbar_layout, null);
//        mActionBarTitle = (TextView) view.findViewById(R.id.tv_actionbar_title);
//        mActionBarTitle.setText("首页");
//        actionBar.setCustomView(view, new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT));
    }

    private void initUI() {
        // 实例化一个viewpager的适配器
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), this);
        // 设置viewpager的适配器
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(mSectionsPagerAdapter.getTabView(i));
        }
        mViewPager.setOnPageChangeListener(this);
    }

    private void initScreenWH() {
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            ImageView imageView = (ImageView) tab.getCustomView();
            if (i == position) {
                imageView.setImageResource(mSectionsPagerAdapter.getTabImagepressId(i));
            } else {
                imageView.setImageResource(mSectionsPagerAdapter.getImageId(i));
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    public static int getScreenWidth() {
        return screenWidth;
    }

    public static int getScreenHeight() {
        return screenHeight;
    }
}
