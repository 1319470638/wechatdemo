package com.yts.wechatdemo.fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.rance.chatui.ui.activity.IMActivity;
import com.yts.wechatdemo.R;
import com.yts.wechatdemo.adapter.ContactAdapter;
import com.yts.wechatdemo.model.ContactModel;
import com.yts.wechatdemo.util.PinyinComparator;
import com.yts.wechatdemo.util.PinyinUtils;
import com.yts.wechatdemo.view.SideBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

@SuppressLint("NewApi")
public class ContactFragment extends Fragment implements ContactAdapter.OnItemClickListener {

    private RecyclerView mRecyclerView;
    private TextView mDialog;
    private SideBar mSideBar;
    private ContactAdapter mAdapter;
    private List<ContactModel> mContactsList = new ArrayList<ContactModel>();
    /**
     * 根据拼音来排列RecyclerView里面的数据类
     */
    private PinyinComparator pinyinComparator;
    private final int[] RES_ID = new int[]{R.drawable.friend1, R.drawable.friend2, R.drawable.friend3, R.drawable.friend4, R.drawable.friend5, R.drawable.friend6,
            R.drawable.friend7, R.drawable.friend8, R.drawable.friend9, R.drawable.friend10, R.drawable.friend11, R.drawable.friend12};


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("ContactFragment","=====onCreateView");
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        pinyinComparator=new PinyinComparator();
        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        //item divider
       /* DividerItemDecoration divider = new DividerItemDecoration(getContext(), layoutManager.getOrientation());
        divider.setDrawable(getContext().getResources().getDrawable(R.drawable.common_line));
        mRecyclerView.addItemDecoration(divider);*/
        mDialog = (TextView) view.findViewById(R.id.dialog);
        mSideBar = (SideBar) view.findViewById(R.id.sideBar);
        mSideBar.setTextView(mDialog);
        mSideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = mAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    layoutManager.scrollToPositionWithOffset(position+1, 0);
                }
            }
        });

    }

    private void initData() {
        String[] names = getContext().getResources().getStringArray(R.array.names);
        mContactsList.clear();
        int i=0;
        for (;i<names.length;i++) {
            ContactModel contact = new ContactModel();
            contact.setName(names[i]);
            int rdm = new Random().nextInt(RES_ID.length);
           // Drawable drawable = getActivity().getResources().getDrawable(RES_ID[i % RES_ID.length]);
            contact.setHeadImg(RES_ID[rdm % RES_ID.length]);
            String pingYin = PinyinUtils.getPingYin(names[i]);
          //  contact.firstLetter = pingYin.substring(0, 1).toUpperCase();
            String sortString = pingYin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                contact.setFirstLetter(sortString.toUpperCase());
            } else {
                contact.setFirstLetter("#");
            }
            mContactsList.add(contact);
        }
        // 根据a-z进行排序源数据
        Collections.sort(mContactsList, pinyinComparator);
        mAdapter = new ContactAdapter(mContactsList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(View view, int position) {
        if(position==0){
            Toast.makeText(getActivity(),"看看就行了哈，不要乱摸",Toast.LENGTH_SHORT).show();
        }else {
           // Toast.makeText(getActivity(),"看看就行了哈，不要乱摸"+mContactsList.get(position).getName(),Toast.LENGTH_SHORT).show();
        }

        Intent intent =new Intent(getActivity(), IMActivity.class);
        intent.putExtra("headImg",mContactsList.get(position).getHeadImg());
        intent.putExtra("title",mContactsList.get(position).getName());
        intent.putExtra("myHeadImg",R.drawable.friend7);

        startActivity(intent);
    }
}
