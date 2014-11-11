package com.catchme.contactlist;

import java.util.ArrayList;
import java.util.Locale;

import com.nostra13.universalimageloader.core.*;
import com.catchme.R;
import com.catchme.exampleObjects.ExampleContent;
import com.catchme.exampleObjects.Message;
import com.catchme.exampleObjects.ExampleContent.ExampleItem;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListAdapter extends BaseAdapter implements Filterable {
	private LayoutInflater inflater;
	private Activity activity;
	private ArrayList<ExampleItem> items;
	public static final String[] SEARCHTYPES = {"0","1"};
	public static final String SEARCHCHAR = ";";
			

	public CustomListAdapter(Activity activity, ArrayList<ExampleItem> items) {
		this.activity = activity;
		this.items = items;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		if (inflater == null) {
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_row, null);
		}
		ExampleItem item = items.get(position);

		ImageView img = (ImageView) convertView
				.findViewById(R.id.item_thumbnail);
		TextView name = (TextView) convertView.findViewById(R.id.item_name);
		// TextView city = (TextView) convertView.findViewById(R.id.item_city);

		TextView lastMsg = (TextView) convertView
				.findViewById(R.id.item_last_message);

		ImageButton btn = (ImageButton) convertView
				.findViewById(R.id.item_position_button);

		if (item.getImageUrl() != null) {
			ImageLoader.getInstance().displayImage(item.getImageUrl(), img);
		} else {
			img.setImageResource(item.getImageResource());
		}

		name.setText(item.getFullName());
		// city.setText(item.getCity());
		Message m = item.getMessages().get(item.getMessages().size() - 1);
		if (m.getSenderId() % 2 == 0) {
			lastMsg.setText("> " + m.getContent());
		} else {
			lastMsg.setText("Ty: " + m.getContent());
		}
		int maxLength = activity.getResources()
				.getInteger(R.integer.max_length);
		if (m.getContent().length() > maxLength) {

			lastMsg.setText(lastMsg.getText().subSequence(0, maxLength - 3)
					+ "...");
		}

		// btn.setOnClickListener(new PositonButtonListener(item.getId()));
		btn.setFocusable(false);
		btn.setFocusableInTouchMode(false);

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
					results.values = ExampleContent.ITEMS;
					results.count = ExampleContent.ITEMS.size();
				} else {
					String searchType = constraint.toString().substring(0,
							constraint.toString().indexOf(SEARCHCHAR));
					String searchQuery = constraint.toString().substring(
							constraint.toString().indexOf(SEARCHCHAR) + 1);
					ArrayList<ExampleItem> filteredArrayNames = new ArrayList<ExampleItem>();

					if (searchType.equals(SEARCHTYPES[0])) {
						for (int i = 0; i < ExampleContent.ITEMS.size(); i++) {
							ExampleItem dataItem = ExampleContent.ITEMS.get(i);

							if (searchQuery
									.startsWith("" + dataItem.getState())) {
								filteredArrayNames.add(dataItem);

							}
						}
					} else if (searchType.equals(SEARCHTYPES[1])) {
						for (int i = 0; i < ExampleContent.ITEMS.size(); i++) {
							ExampleItem dataItem = ExampleContent.ITEMS.get(i);
							if (dataItem
									.getFullName()
									.toLowerCase(Locale.getDefault())
									.contains(
											searchQuery.toLowerCase(Locale
													.getDefault()))) {
								filteredArrayNames.add(dataItem);
							}
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
}
