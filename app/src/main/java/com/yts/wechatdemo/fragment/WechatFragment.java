package com.yts.wechatdemo.fragment;
import android.annotation.SuppressLint;
import android.content.Intent;
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
import android.widget.AdapterView;

import com.rance.chatui.ui.activity.IMActivity;
import com.yts.wechatdemo.R;
import com.yts.wechatdemo.adapter.WechatFragmentAdapter;
import com.yts.wechatdemo.model.CommunicateInfo;
import com.yts.wechatdemo.view.PopupWindowList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressLint("NewApi")
public class WechatFragment extends Fragment implements WechatFragmentAdapter.OnItemClickListener {

    private LinearLayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private WechatFragmentAdapter mAdapter;
    private List<CommunicateInfo> mDatas = new ArrayList<CommunicateInfo>();
    private String[] TITLES = new String[]{"张三", "Android开发交流群", "微信支付", "BB", "产品经理", "UI设计组群",
            "携程旅行网", "招商银行", "玮哥", "交互设计师", "UI设计-董师", "高三班-杨阬", "朗哥"};

    private String[] DESC = new String[]{"吃饭了没？", "需求确认了再告诉我要不要改", "微信支付凭证", "收到，谢谢啦！", "这是今天开会的资料", "要不你来？",
            "原型图地址发一下", "【链接】告诉你个发家致富的秘密！", "【红包】中秋节快乐", "这个效果挺好玩的哦！", "【连接】全球十大豪车品牌设计！","过年有没有空哦，出来聚聚", "【连接】面膜优惠促销！"};

    private int[] RES_IDS = new int[]{R.drawable.friend4, R.drawable.ui_designer_group,
            R.drawable.wechat_pay, R.drawable.friend2, R.drawable.friend1, R.drawable.group2,
            R.drawable.logo_ctrip, R.drawable.dianxin, R.drawable.friend3,
            R.drawable.friend4, R.drawable.friend5, R.drawable.friend6, R.drawable.friend7};
    private boolean[] MSG_STATUS = new boolean[]{false, false, true, false, false, true, true, true, true, true, true, true, true};
    private int[] TYPE=new int[]{0,1,2,0,0,1,2,2,0,0,0,0,0,0,0,0,0,0};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("WechatFragment","=====onCreateView");
        View view = inflater.inflate(R.layout.fragment_wechat, container, false);
        mRecyclerView = view.findViewById(R.id.rv_list);

        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        //item divider
        DividerItemDecoration divider = new DividerItemDecoration(getContext(), mLayoutManager.getOrientation());
        divider.setDrawable(getContext().getResources().getDrawable(R.drawable.common_line));
        mRecyclerView.addItemDecoration(divider);

        initData();

        return view;
    }

    private void initData() {
        mDatas.clear();
        for (int i = 0; i < TITLES.length; i++) {
            CommunicateInfo info = new CommunicateInfo();
            info.title = TITLES[i];
            info.desc = DESC[i];
            info.drawable_id = RES_IDS[i];
            info.msg_status = MSG_STATUS[i];
            info.type=TYPE[i];
            mDatas.add(info);
        }
        mAdapter = new WechatFragmentAdapter(mDatas);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(View view, int position) {
        mDatas.get(position).msg_status=true;
        mAdapter.notifyItemChanged(position);
        Intent intent =new Intent(getActivity(), IMActivity.class);
        intent.putExtra("headImg",mDatas.get(position).drawable_id);
        intent.putExtra("title",mDatas.get(position).title);
        intent.putExtra("type",mDatas.get(position).type);
        intent.putExtra("myHeadImg",R.drawable.friend7);
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(View view, int position) {
        Log.d("=======", "onItemLongClick: "+position);
        showPopWindows(view,position);
    }

    private PopupWindowList mPopupWindowList;
    private void showPopWindows(View view, final int item_position){
        List<String> dataList = new ArrayList<>();
        dataList.add("标为未读");
        dataList.add("置顶聊天");
        dataList.add("删除该聊天");
        if (mPopupWindowList == null){
            mPopupWindowList = new PopupWindowList(view.getContext());
        }
        mPopupWindowList.setAnchorView(view);
        mPopupWindowList.setItemData(dataList);
        mPopupWindowList.setModal(true);
        mPopupWindowList.show();
        mPopupWindowList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("======", "click position="+position);
                mPopupWindowList.hide();
                if(position==0){
                    mDatas.get(item_position).msg_status=false;
                    mAdapter.notifyItemChanged(item_position);
                }else if(position==1){
                    CommunicateInfo communicateInfo=mDatas.get(item_position);
                    mDatas.remove(item_position);
                    mDatas.add(0,communicateInfo);
                    mAdapter.notifyDataSetChanged();
                }else {
                    mDatas.remove(item_position);
                    mAdapter.notifyItemRangeRemoved(item_position,mAdapter.getItemCount());

                }
            }
        });
    }

}
