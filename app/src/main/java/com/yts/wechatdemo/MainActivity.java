package com.yts.wechatdemo;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yts.wechatdemo.fragment.ContactFragment;
import com.yts.wechatdemo.fragment.DiscoveryFragment;
import com.yts.wechatdemo.fragment.MineFragment;
import com.yts.wechatdemo.fragment.WechatFragment;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private LinearLayout mLlWechat;
    private LinearLayout mLlContact;
    private LinearLayout mLlDiscovery;
    private LinearLayout mLlMine;
    private ViewPager mViewPager;
    private WechatAdapter mAdapter;
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    /**
     * 初始化控件
     */
    @SuppressLint({"NewApi"})
    private void initView() {
        mLlWechat = (LinearLayout) findViewById(R.id.ll_wechat);
        mLlContact = (LinearLayout) findViewById(R.id.ll_contact);
        mLlDiscovery = (LinearLayout) findViewById(R.id.ll_discovery);
        mLlMine = (LinearLayout) findViewById(R.id.ll_mine);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mToolbar = (Toolbar)findViewById(R.id.toolbar);

//        mToolbar.setNavigationIcon(R.drawable.add);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);//避免toolbar默认标题占位
        setListener();
    }

    private void initData() {
        WechatFragment wechatFragment = new WechatFragment();
        ContactFragment contactFragment = new ContactFragment();
        DiscoveryFragment discoveryFragment = new DiscoveryFragment();
        MineFragment mineFragment = new MineFragment();

        mFragmentList.add(wechatFragment);
        mFragmentList.add(contactFragment);
        mFragmentList.add(discoveryFragment);
        mFragmentList.add(mineFragment);

        mAdapter = new WechatAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
    }

    /**
     * 点击监听
     */
    private void setListener() {
        mLlWechat.setOnClickListener(MainActivity.this);
        mLlContact.setOnClickListener(MainActivity.this);
        mLlDiscovery.setOnClickListener(MainActivity.this);
        mLlMine.setOnClickListener(MainActivity.this);
        mViewPager.setOnPageChangeListener(MainActivity.this);
    }

    @Override
    public void onClick(View v) {
        resetTab();
        if (v == mLlWechat) {
            showWechatTab();
            mViewPager.setCurrentItem(0);
        } else if (v == mLlContact) {
            showContactTab();
            mViewPager.setCurrentItem(1);
        } else if (v == mLlDiscovery) {
            showDiscoveryTab();
            mViewPager.setCurrentItem(2);
        } else if (v == mLlMine) {
            showMineTab();
            mViewPager.setCurrentItem(3);
        }
    }

    private void showMineTab() {
        ((TextView) mLlMine.getChildAt(1)).setTextColor(getResources().getColor(R.color.textSelected));
        ((ImageView) mLlMine.getChildAt(0)).setBackgroundResource(R.drawable.mine_selected);
    }

    private void showDiscoveryTab() {
        ((TextView) mLlDiscovery.getChildAt(1)).setTextColor(getResources().getColor(R.color.textSelected));
        ((ImageView) mLlDiscovery.getChildAt(0)).setBackgroundResource(R.drawable.dis_selected);
    }

    private void showContactTab() {
        ((TextView) mLlContact.getChildAt(1)).setTextColor(getResources().getColor(R.color.textSelected));
        ((ImageView) mLlContact.getChildAt(0)).setBackgroundResource(R.drawable.contact_selected);
    }

    private void showWechatTab() {
        ImageView ivWechat = (ImageView) mLlWechat.findViewById(R.id.iv_wechat);
        TextView tvWechat = (TextView) mLlWechat.getChildAt(1);
        tvWechat.setTextColor(getResources().getColor(R.color.textSelected));
        ivWechat.setBackgroundResource(R.drawable.weichat_selected);
    }

    /**
     * 重置底部导航样式
     */
    private void resetTab() {
        ((TextView) mLlWechat.getChildAt(1)).setTextColor(getResources().getColor(R.color.textNormal));
        mLlWechat.findViewById(R.id.iv_wechat).setBackgroundResource(R.drawable.weichat_normal);

        ((TextView) mLlContact.getChildAt(1)).setTextColor(getResources().getColor(R.color.textNormal));
        ((ImageView) mLlContact.getChildAt(0)).setBackgroundResource(R.drawable.contact_normal);
        ((TextView) mLlDiscovery.getChildAt(1)).setTextColor(getResources().getColor(R.color.textNormal));
        ((ImageView) mLlDiscovery.getChildAt(0)).setBackgroundResource(R.drawable.dis_normal);
        ((TextView) mLlMine.getChildAt(1)).setTextColor(getResources().getColor(R.color.textNormal));
        ((ImageView) mLlMine.getChildAt(0)).setBackgroundResource(R.drawable.mine_normal);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        resetTab();
        switch (position) {
            case 0:
                showWechatTab();
                break;
            case 1:
                showContactTab();
                break;
            case 2:
                showDiscoveryTab();
                break;
            case 3:
                showMineTab();
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class WechatAdapter extends FragmentPagerAdapter {

        public WechatAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("RestrictedApi")
    @Override
    //解决menu icon不显示问题
    protected boolean onPrepareOptionsPanel(View view, Menu menu) {
        if (menu != null) {
            if (menu.getClass() == MenuBuilder.class) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
//                    e.print(getClass().getSimpleName() + "onMenuOpened...unable to set icons for overflow menu" + e);
                }
            }
        }
        return super.onPrepareOptionsPanel(view, menu);
    };
}
