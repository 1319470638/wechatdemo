package com.yts.wechatdemo.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yts.wechatdemo.R;
import com.yts.wechatdemo.model.GroupMemberModel;

import java.util.List;

public class GroupMemberListAdapter extends RecyclerView.Adapter {

    private List<GroupMemberModel> mGroupMemberModelList;
    private Context mContext;

    public GroupMemberListAdapter(List<GroupMemberModel> groupMemberModelList) {
        this.mGroupMemberModelList = groupMemberModelList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group_member, parent, false);
        mContext=parent.getContext();
        return new GroupMemberViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            GroupMemberModel groupMemberModel = mGroupMemberModelList.get(position);
            ((GroupMemberViewHolder) holder).tvName.setText(groupMemberModel.getName());
            Drawable drawable = mContext.getResources().getDrawable(groupMemberModel.getHeadImg());
            ((GroupMemberViewHolder) holder).ivLogo.setBackgroundDrawable(drawable);
            if (mOnItemClickListener != null) {
                final int finalPosition = position;
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onItemClick(holder.itemView, finalPosition);
                    }
                });

            }

    }

    @Override
    public int getItemCount() {
        return mGroupMemberModelList.size();
    }

    public static class GroupMemberViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivLogo;
        private TextView tvName;

        public GroupMemberViewHolder(View itemView) {
            super(itemView);
            //初始化子view
            ivLogo = itemView.findViewById(R.id.iv_logo);
            tvName =  itemView.findViewById(R.id.tv_title);
        }
    }


    //**********************itemClick************************
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
}


