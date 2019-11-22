package com.yts.wechatdemo;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rance.chatui.adapter.ChatAdapter;
import com.rance.chatui.enity.MessageInfo;
import com.rance.chatui.widget.EmotionInputDetector;
import com.yts.wechatdemo.adapter.ContactAdapter;
import com.yts.wechatdemo.adapter.GroupMemberListAdapter;
import com.yts.wechatdemo.model.ContactModel;
import com.yts.wechatdemo.model.GroupMemberModel;
import com.yts.wechatdemo.util.PinyinUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class GroupMemberListActivity extends AppCompatActivity implements GroupMemberListAdapter.OnItemClickListener {
    private static final String TAG = "GroupMemberListActivity";
    RecyclerView mRecyclerView;

    private TextView tv_title;
    private ImageView img_back;

    private EmotionInputDetector mDetector;


    private GroupMemberListAdapter mAdapter;
    private LinearLayoutManager layoutManager;
    private List<GroupMemberModel> mGroupMemberModelList;

    private String title="";
    private Context mContext;
    private final int[] RES_ID = new int[]{R.drawable.friend1, R.drawable.friend2, R.drawable.friend3, R.drawable.friend4, R.drawable.friend5, R.drawable.friend6,
            R.drawable.friend7, R.drawable.friend8, R.drawable.friend9, R.drawable.friend10, R.drawable.friend11, R.drawable.friend12};
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_member_list);
        mContext=this;
        Intent intent=getIntent();
        title=intent.getStringExtra("title");
        count=intent.getIntExtra("count",0);
        initView();
        initData();
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        //item divider
        DividerItemDecoration divider = new DividerItemDecoration(this, layoutManager.getOrientation());
        divider.setDrawable(getResources().getDrawable(R.drawable.common_line));
        mRecyclerView.addItemDecoration(divider);
        img_back= (ImageView) findViewById(R.id.img_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tv_title.setText("选择提醒的人");


    }


    private void initData() {
        String[] names = getResources().getStringArray(R.array.names);
        mGroupMemberModelList=new ArrayList<>();
        int i=0;
        for (;i<count;i++) {
            GroupMemberModel groupMemberModel = new GroupMemberModel();
            groupMemberModel.setName(names[i]);

            // Drawable drawable = getActivity().getResources().getDrawable(RES_ID[i % RES_ID.length]);
            groupMemberModel.setHeadImg(RES_ID[i]);

            mGroupMemberModelList.add(groupMemberModel);
        }

        mAdapter = new GroupMemberListAdapter(mGroupMemberModelList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onItemClick(View view, int position) {
        Log.d(TAG, "onItemClick: ");
        Intent intent =new Intent();
        intent.putExtra("name",mGroupMemberModelList.get(position).getName());
        intent.putExtra("image",mGroupMemberModelList.get(position).getHeadImg());
        Log.d(TAG, "onItemClick: image = "+mGroupMemberModelList.get(position).getHeadImg());
        setResult(2,intent);
        finish();
    }
}
