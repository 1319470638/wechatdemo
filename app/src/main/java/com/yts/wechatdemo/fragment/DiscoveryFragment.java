package com.yts.wechatdemo.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.yts.wechatdemo.R;


@SuppressLint("NewApi")
public class DiscoveryFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("DiscoveryFragment","=====onCreateView");
        /*TextView tv = new TextView(getContext());
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(getResources().getColor(R.color.textNormal));
        tv.setText("发现");*/
        View view = inflater.inflate(R.layout.fragment_discovery, container, false);
        view.findViewById(R.id.ll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //    Toast.makeText(getActivity(),"这就是个样子货，你想看啥呢？",Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
