package com.catchme.contactlist;

import java.util.ArrayList;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.catchme.R;
import com.catchme.exampleObjects.*;
import com.catchme.utils.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class CustomListAdapter extends BaseAdapter implements Filterable {
	private LayoutInflater inflater;
	private Activity activity;
	private ArrayList<ExampleItem> items;

	public CustomListAdapter(Activity activity, ArrayList<ExampleItem> items) {
		this.items = items;
		this.activity = activity;
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return items.get(position).getId();
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (inflater == null) {
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_row, null);

		}
		ExampleItem item = items.get(position);

		RoundedImageView img = (RoundedImageView) convertView
				.findViewById(R.id.item_thumbnail);
		TextView name = (TextView) convertView.findViewById(R.id.item_name);
		

		ImageLoader.getInstance().displayImage(item.getSmallImageUrl(), img);

		name.setText(item.getFullName());
		
		img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// ListView parentListView = (ListView)
				// v.getParent().getParent();
				// parentListView.setItemChecked(position, true);
				//TODO image selecting
			}
		});
		return convertView;
	}

	@Override
	public Filter getFilter() {
		Filter filter = new Filter() {
			;
			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {

				items = (ArrayList<ExampleItem>) results.values;
				notifyDataSetChanged();
			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults results = new FilterResults();

				if (constraint == null || constraint.length() == 0) {
					results.values = new ArrayList<ExampleItem>(ExampleContent.ITEM_MAP.values());
					results.count = ExampleContent.ITEM_MAP.size();
				} else {

					ArrayList<ExampleItem> filteredArrayNames = new ArrayList<ExampleItem>();
					
					for(Long key:ExampleContent.ITEM_MAP.keySet()){
						ExampleItem dataItem = ExampleContent.ITEM_MAP.get(key);
						if (dataItem
								.getFullName()
								.toLowerCase(Locale.getDefault())
								.contains(
										constraint.toString().toLowerCase(
												Locale.getDefault()))) {
							filteredArrayNames.add(dataItem);
						}
					}
					results.count = filteredArrayNames.size();
					results.values = filteredArrayNames;
				}
				return results;

			}
		};
		return filter;
	}

	public void swapItems(ArrayList<ExampleItem> items) {
		this.items = items;
		notifyDataSetChanged();
	}
}
