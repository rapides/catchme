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
import com.catchme.exampleObjects.ExampleContent;
import com.catchme.exampleObjects.ExampleContent.ExampleItem;
import com.catchme.utils.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class CustomListAdapter extends BaseAdapter implements Filterable {
	private LayoutInflater inflater;
	private Activity activity;
	private ArrayList<ExampleItem> items;/*
										 * public static final String[]
										 * SEARCHTYPES = { "0", "1" }; public
										 * static final String SEARCHCHAR = ";";
										 */

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
		// TextView city = (TextView)
		// convertView.findViewById(R.id.item_city);

		/*TextView lastMsg = (TextView) convertView
				.findViewById(R.id.item_last_message);*/

		ImageLoader.getInstance().displayImage(item.getSmallImageUrl(), img);

		name.setText(item.getFullName());
		// city.setText(item.getCity());
		/*
		 * Message m = item.getMessages(item.getFirstConversationId()).get(
		 * item.getMessages(item.getFirstConversationId()).size() - 1); if
		 * (m.getSenderId() % 2 == 0) { lastMsg.setText("> " + m.getContent());
		 * } else { lastMsg.setText("You: " + m.getContent()); }
		 */
		/*int maxLength = activity.getResources()
				.getInteger(R.integer.max_length);*/
		/*
		 * if (m.getContent().length() > maxLength) {
		 * 
		 * lastMsg.setText(lastMsg.getText().subSequence(0, maxLength - 3) +
		 * "..."); }
		 */
		img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//ListView parentListView = (ListView) v.getParent().getParent();
				// parentListView.setItemChecked(position, true);
				// todo image checking
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
					results.values = ExampleContent.ITEMS;
					results.count = ExampleContent.ITEMS.size();
				} else {

					ArrayList<ExampleItem> filteredArrayNames = new ArrayList<ExampleItem>();
					/*
					 * String searchType = constraint.toString().substring(0,
					 * constraint.toString().indexOf(SEARCHCHAR)); String
					 * searchQuery = constraint.toString().substring(
					 * constraint.toString().indexOf(SEARCHCHAR) + 1);
					 * 
					 * if (searchType.equals(SEARCHTYPES[0])) { for (int i = 0;
					 * i < ExampleContent.ITEMS.size(); i++) { ExampleItem
					 * dataItem = ExampleContent.ITEMS.get(i);
					 * 
					 * if (searchQuery .startsWith("" + dataItem.getState())) {
					 * filteredArrayNames.add(dataItem); } } } else if
					 * (searchType.equals(SEARCHTYPES[1])) {
					 */
					for (int i = 0; i < ExampleContent.ITEMS.size(); i++) {
						ExampleItem dataItem = ExampleContent.ITEMS.get(i);
						if (dataItem
								.getFullName()
								.toLowerCase(Locale.getDefault())
								.contains(
										constraint.toString().toLowerCase(
												Locale.getDefault()))) {
							filteredArrayNames.add(dataItem);
						}
					}
					// }
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
