package com.yts.wechatdemo.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yts.wechatdemo.R;



public class ContactView extends RelativeLayout implements View.OnClickListener {

    private View view;
    private ImageView mIvLogo;
    private TextView mTvTitle,mTvTag;
    private View mDivider;
    private RelativeLayout rl;

    public ContactView(Context context) {
        this(context, null);
    }

    public ContactView(Context context, AttributeSet attrs) {
        super(context, attrs);

        view = LayoutInflater.from(context).inflate(R.layout.view_contact, this);
        view.setOnClickListener(this);
       // rl=view.findViewById(R.id.rl);
        mIvLogo = view.findViewById(R.id.iv_logo);
        mTvTitle = view.findViewById(R.id.tv_title);
        mDivider = view.findViewById(R.id.v_divider);
        mTvTag=view.findViewById(R.id.tv_tag);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ContactView);
        String name = a.getString(R.styleable.ContactView_cvName);
        Drawable drawable = a.getDrawable(R.styleable.ContactView_cvBackground);
        boolean dividerVisiable = a.getBoolean(R.styleable.ContactView_cvDividerVisiable, true);
        a.recycle();

        //设置view内容
        setContactDrawable(null == drawable ? context.getResources().getDrawable(R.drawable.friend2) : drawable);
        setContactName(name);
        mDivider.setVisibility(dividerVisiable ? View.VISIBLE : View.GONE);
    }

    public ContactView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setContactDrawable(Drawable drawable) {
        mIvLogo.setBackgroundDrawable(drawable);
    }

    public void setContactName(String name) {
        mTvTitle.setText(name);
    }

    public TextView getmTvTag() {
        return mTvTag;
    }
    public RelativeLayout getRl() {
        return rl;
    }
    public TextView getContactTextView() {
        return mTvTitle;
    }

    public ImageView getContactImageView() {
        return mIvLogo;
    }

    @Override
    public void onClick(View v) {

    }

//    public void setContactDrawable(int resId){
//
//    }
}
