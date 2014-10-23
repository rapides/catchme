package catchme.messages;

import com.nostra13.universalimageloader.core.ImageLoader;

import catchme.exampleObjects.ExampleContent.ExampleItem;
import cycki.catchme.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MessagesListAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private Activity activity;
	private ExampleItem item;

	public MessagesListAdapter(Activity activity, ExampleItem mItem) {
		this.activity = activity;
		this.item = mItem;
	}

	@Override
	public int getCount() {
		return item.getMessages().size();
	}

	@Override
	public Object getItem(int position) {
		return item.getMessages().get(position);
	}

	@Override
	public long getItemId(int position) {
		return item.getMessages().get(position).hashCode();
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (inflater == null) {
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.message, null);
		}
		TextView messageTime = (TextView) convertView
				.findViewById(R.id.single_message_time);
		TextView message = (TextView) convertView
				.findViewById(R.id.single_message_content);

		message.setText(item.getMessages().get(position).getContent());
		messageTime.setText(item.getMessages().get(position).getTime());

		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(
				60, 60);
		ImageView img = (ImageView) convertView
				.findViewById(R.id.single_message_image);
		if (item.getMessages().get(position).getSenderId() % 2 != 0) {
			imageParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			lp.addRule(RelativeLayout.LEFT_OF, R.id.single_message_image);
			message.setBackgroundResource(R.drawable.rounded_rectangle);

			img.setImageDrawable(convertView.getResources().getDrawable(
					R.drawable.o4));
		} else {
			imageParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			lp.addRule(RelativeLayout.RIGHT_OF, R.id.single_message_image);
			message.setBackgroundResource(R.drawable.rounded_rectangle_sender);
			if (item.getImageUrl() != null) {
				ImageLoader.getInstance().displayImage(item.getImageUrl(), img);
			} else {
				img.setImageResource(item.getImageResource());
			}
		}
		message.setMinimumWidth((int) (parent.getWidth() * 0.25));
		message.setMaxWidth((int) (parent.getWidth() * 0.7));
		message.setLayoutParams(lp);
		img.setLayoutParams(imageParams);
		return convertView;
	}

}
