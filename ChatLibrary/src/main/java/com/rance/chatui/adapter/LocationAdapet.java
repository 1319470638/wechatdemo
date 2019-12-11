package com.rance.chatui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rance.chatui.R;

import java.util.List;
import java.util.Map;

public class LocationAdapet extends BaseAdapter {
	List<Map<Object, Object>> list;
	Context context;

	public LocationAdapet(Context context, List<Map<Object, Object>> list) {
		this.context = context;
		this.list = list;
	}

	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Houder houder;
		if (convertView == null) {
			houder = new Houder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.location_item, null);

			houder.textView1 = (TextView) convertView
					.findViewById(R.id.textView1);
			houder.textView2 = (TextView) convertView
					.findViewById(R.id.textView2);
			houder.imageView1 = (ImageView) convertView
					.findViewById(R.id.imageView1);
			convertView.setTag(houder);
		} else {
			houder = (Houder) convertView.getTag();
		}
		Map<Object, Object> map = list.get(position);
		houder.textView1.setText(map.get("title").toString());
		houder.textView2.setText(map.get("lr").toString());
		// houder.imageView1.setBackgroundResource();
		return convertView;
	}

	class Houder {
		TextView textView1;
		TextView textView2;
		ImageView imageView1;
	}
}
