package com.yts.wechatdemo.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yts.wechatdemo.R;
import com.yts.wechatdemo.model.CommunicateInfo;

import java.util.List;


public class WechatFragmentAdapter extends RecyclerView.Adapter<WechatFragmentAdapter.WechatViewHolder> {

    private final List<CommunicateInfo> mData;
    private Context mContext;

    public WechatFragmentAdapter(List<CommunicateInfo> data) {
        this.mData = data;
    }

    //关联布局文件
    @Override
    public WechatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.communicate_item_view_layout, parent, false);
        this.mContext = parent.getContext();

        return new WechatViewHolder(itemView);
    }

    //设置数据
    @Override
    public void onBindViewHolder(final WechatViewHolder holder, int position) {
        CommunicateInfo info = mData.get(position);
        holder.ivLogo.setBackgroundDrawable(mContext.getResources().getDrawable(info.drawable_id));
        holder.tvTitle.setText(info.title);
        holder.tvDesc.setText(info.desc);
        holder.ivStatus.setVisibility(info.msg_status ? View.GONE : View.VISIBLE);
        if(mOnItemClickListener!=null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(v, holder.getAdapterPosition());
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mOnItemClickListener.onItemLongClick(view, holder.getAdapterPosition());
                    return false;
                }
            });
        }
    }

    //item条数
    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class WechatViewHolder extends RecyclerView.ViewHolder {

        private View ivStatus;
        private ImageView ivLogo;
        private TextView tvTitle;
        private TextView tvDesc;

        public WechatViewHolder(View itemView) {
            super(itemView);
            //初始化item的子view
            ivLogo = itemView.findViewById(R.id.iv_logo);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDesc = itemView.findViewById(R.id.tv_desc);
            ivStatus = itemView.findViewById(R.id.iv_status);

        }
    }

    //**********************itemClick************************
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
}
