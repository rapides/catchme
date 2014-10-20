package cycki.fragmenttest;

import java.util.ArrayList;

import cycki.fragmenttest.dummy.DummyContent;
import cycki.fragmenttest.dummy.DummyContent.DummyItem;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private Activity activity;
	private ArrayList<DummyItem> items;

	public CustomListAdapter(Activity activity, ArrayList<DummyItem> items) {
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (inflater == null)
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
			convertView = inflater.inflate(R.layout.list_row, null);
		DummyItem item = DummyContent.ITEMS.get(position);
		
		ImageView img = (ImageView) convertView.findViewById(R.id.thumbnail);
		TextView name = (TextView) convertView.findViewById(R.id.name);
		TextView city = (TextView) convertView.findViewById(R.id.city);
		
		img.setImageResource(item.getImageResource());
		name.setText(item.getName());
		city.setText(item.getCity());
		return convertView;
	}

}
