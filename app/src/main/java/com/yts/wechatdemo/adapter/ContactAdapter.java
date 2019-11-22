package com.yts.wechatdemo.adapter;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yts.wechatdemo.R;
import com.yts.wechatdemo.model.ContactModel;
import com.yts.wechatdemo.view.ContactHeaderView;
import com.yts.wechatdemo.view.ContactView;

import java.util.List;
import java.util.Random;

public class ContactAdapter extends RecyclerView.Adapter {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;

    private  List<ContactModel> mContactList;
    private Context mContext;


    public ContactAdapter(List<ContactModel> contactsList) {
        this.mContactList = contactsList;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else {
            return TYPE_NORMAL;
        }
//        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_contact, parent, false);
        mContext=parent.getContext();
        if (viewType == TYPE_HEADER) {
            ContactHeaderView headerView = new ContactHeaderView(mContext);
            return new HeaderViewHolder(headerView);
        } else {
            ContactView view = new ContactView(mContext);
            return new ContactViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);

        if (viewType==TYPE_HEADER) {
            return;
        } else {
            position--;
            int section = getSectionForPosition(position);

            if (position == getPositionForSection(section)) {
                ((ContactViewHolder)holder).tvTag.setVisibility(View.VISIBLE);
                ((ContactViewHolder)holder).tvTag.setText(mContactList.get(position).getFirstLetter());
            } else {
                ((ContactViewHolder)holder).tvTag.setVisibility(View.GONE);
                //绑定数据

            }
            ContactModel contact = mContactList.get(position);
            ((ContactViewHolder) holder).tvName.setText(contact.getName());
            Drawable drawable = mContext.getResources().getDrawable(contact.getHeadImg());
            ((ContactViewHolder) holder).ivLogo.setBackgroundDrawable(drawable);
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
    }

    @Override
    public int getItemCount() {
        return mContactList.size()+1;
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivLogo;
        private TextView tvName;
        private TextView tvTag;
      //  private RelativeLayout rl;

        public ContactViewHolder(View itemView) {
            super(itemView);
            //初始化子view
            ivLogo = ((ContactView) itemView).getContactImageView();
            tvName = ((ContactView) itemView).getContactTextView();
            tvTag=((ContactView) itemView).getmTvTag();
          //  rl=((ContactView) itemView).getRl();
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {

        public HeaderViewHolder(View itemView) {
            super(itemView);
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



    /**
     * 提供给Activity刷新数据
     * @param list
     */
    public void updateList(List<ContactModel> list){
        this.mContactList = list;
        notifyDataSetChanged();
    }


    /**
     * 根据ListView的当前位置获取分类的首字母的char ascii值
     */
    public int getSectionForPosition(int position) {

        return mContactList.get(position).getFirstLetter().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getItemCount()-1; i++) {
            String sortStr = mContactList.get(i).getFirstLetter();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

}


