package com.catchme.contactlist;

import java.util.ArrayList;

import com.catchme.R;
import com.catchme.exampleObjects.ExampleContent.ExampleItem;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

public class DrawerMenuAdapter extends BaseAdapter {
	private String title = "DRAWER TITLE";
	private String[] array = { "Refresh", "Settings", "Help", "Logout" };
	private LayoutInflater inflater;
	private Activity activity;

	public DrawerMenuAdapter(Activity activity) {
		this.activity = activity;
	}

	@Override
	public int getCount() {
		return array.length + 1;
	}

	@Override
	public Object getItem(int position) {
		if (position == 0) {
			return title;
		} else {
			return array[position-1];
		}
	}

	@Override
	public long getItemId(int position) {
		if (position == 0) {
			return title.hashCode();
		} else {
			return array[position-1].hashCode();
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(position==0){
			if (inflater == null) {
				inflater = (LayoutInflater) activity
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			}
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.drawer_list_title, null);
			}
			TextView item = (TextView) convertView
					.findViewById(R.id.drawer_titleitem);
			item.setText(title);
			convertView.setClickable(false);
		}else{
			if (inflater == null) {
				inflater = (LayoutInflater) activity
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			}
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.drawer_list_item, null);
			}
			TextView item = (TextView) convertView
					.findViewById(R.id.drawer_menuitem);
			item.setText(array[position-1]);
		}
		
		return convertView;
	}
}