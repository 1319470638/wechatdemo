package com.yts.wechatdemo.view;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.yts.wechatdemo.R;



public class ContactHeaderView extends LinearLayout  implements View.OnClickListener {
    private Context mcontext;
    private ContactView contactView1,contactView2,contactView3,contactView4;
    public ContactHeaderView(Context context) {
        this(context, null, 0);
        mcontext=context;
    }

    public ContactHeaderView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
        mcontext=context;
    }

    @SuppressLint("NewApi")
    public ContactHeaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view=  LayoutInflater.from(context).inflate(R.layout.view_contact_header, this);
        contactView1= view.findViewById(R.id.tv1);
        contactView1.setOnClickListener(this);
        contactView2= view.findViewById(R.id.tv2);
        contactView2.setOnClickListener(this);
        contactView3= view.findViewById(R.id.tv3);
        contactView3.setOnClickListener(this);
        contactView4= view.findViewById(R.id.tv4);
        contactView4.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv1:
                Toast.makeText(mcontext,""+contactView1.getContactTextView().getText().toString()+"功能正在开发中...",Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv2:
                Toast.makeText(mcontext,""+contactView2.getContactTextView().getText().toString()+"功能正在开发中...",Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv3:
                Toast.makeText(mcontext,""+contactView3.getContactTextView().getText().toString()+"功能正在开发中...",Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv4:
                Toast.makeText(mcontext,""+contactView4.getContactTextView().getText().toString()+"功能正在开发中...",Toast.LENGTH_SHORT).show();
                break;
        }

    }
}
