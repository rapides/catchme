package com.catchme.contactlist;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.catchme.R;
import com.catchme.exampleObjects.ExampleContent;
import com.catchme.utils.RoundedImageView;

public class DrawerMenuAdapter extends BaseAdapter {
	private String[] array = { "Refresh", "Settings", "Help", "TEST_Login",
			"Logout" };
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
			return ExampleContent.currentUser;
		} else {
			return array[position - 1];
		}
	}

	@Override
	public long getItemId(int position) {
		if (position == 0) {
			return ExampleContent.currentUser.hashCode();
		} else {
			return array[position - 1].hashCode();
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (position == 0) {
			if (inflater == null) {
				inflater = (LayoutInflater) activity
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			}
			if (convertView == null) {
				convertView = inflater
						.inflate(R.layout.drawer_list_title, null);
			}
			TextView name = (TextView) convertView
					.findViewById(R.id.drawer_title_user);

			TextView email = (TextView) convertView
					.findViewById(R.id.drawer_title_email);
			RoundedImageView avatar = (RoundedImageView) convertView
					.findViewById(R.id.drawer_title_avatar);
			if (ExampleContent.currentUser != null) {
				name.setText(ExampleContent.currentUser.getFullName());
				email.setText(ExampleContent.currentUser.getEmail());
				avatar.setImageResource(ExampleContent.currentUser
						.getImageResource());
			} else {
				name.setText("");
				email.setText("");
				avatar.setImageResource(-1);	
			}
			convertView.setClickable(false);
		} else {
			if (inflater == null) {
				inflater = (LayoutInflater) activity
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			}
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.drawer_list_item, null);
			}
			TextView item = (TextView) convertView
					.findViewById(R.id.drawer_menuitem);
			item.setText(array[position - 1]);
		}

		return convertView;
	}
}