package catchme.contactlist;

import java.util.ArrayList;

import com.nostra13.universalimageloader.core.*;

import catchme.exampleObjects.ExampleContent;
import catchme.exampleObjects.ExampleContent.ExampleItem;
import catchme.exampleObjects.Message;
import cycki.catchme.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private Activity activity;
	private ArrayList<ExampleItem> items;

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

	@SuppressLint("InflateParams") @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (inflater == null) {
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_row, null);
		}
		ExampleItem item = ExampleContent.ITEMS.get(position);

		ImageView img = (ImageView) convertView
				.findViewById(R.id.item_thumbnail);
		TextView name = (TextView) convertView.findViewById(R.id.item_name);
		TextView city = (TextView) convertView.findViewById(R.id.item_city);
		TextView lastMsg = (TextView) convertView
				.findViewById(R.id.item_last_message);

		if (item.getImageUrl() != null) {
			ImageLoader.getInstance().displayImage(item.getImageUrl(), img);
		} else {
			img.setImageResource(item.getImageResource());
		}

		name.setText(item.getName());
		city.setText(item.getCity());
		Message m = item.getMessages().get(item.getMessages().size() - 1);
		if(m.getSenderId()%2==0){
			lastMsg.setText("> "+m.getContent());
		}else{
			lastMsg.setText("Ty: "+m.getContent());
		}
		if(m.getContent().length()>R.integer.max_length){
			lastMsg.setText(lastMsg.getText().subSequence(0, lastMsg.length()-3)+"...");
		}
		

		ImageButton btn = (ImageButton) convertView
				.findViewById(R.id.item_position_button);
		btn.setOnClickListener(new PositonButtonListener(item.getId()));
		btn.setFocusable(false);
		btn.setFocusableInTouchMode(false);
		return convertView;
	}

}
