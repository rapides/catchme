package com.catchme.messages;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.catchme.R;
import com.catchme.exampleObjects.ExampleContent.ExampleItem;
import com.catchme.exampleObjects.ExampleContent.LoggedUser;
import com.catchme.utils.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MessagesListAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private Activity activity;
	private ExampleItem item;
	private LoggedUser user;

	public MessagesListAdapter(Activity activity, ExampleItem mItem,
			LoggedUser user) {
		this.activity = activity;
		this.item = mItem;
		this.user = user;
	}

	@Override
	public int getCount() {
		if (item.getMessages(item.getFirstConversationId()) != null) {
			return item.getMessages(item.getFirstConversationId()).size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return item.getMessages(item.getFirstConversationId()).get(position);
	}

	@Override
	public long getItemId(int position) {
		return item.getMessages(item.getFirstConversationId()).get(position).getMessageId();
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

		message.setText(item.getMessages(item.getFirstConversationId())
				.get(position).getContent());
		messageTime.setText(item.getMessages(item.getFirstConversationId())
				.get(position).getTime());

		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		LayoutParams imageParams = new LayoutParams(60, 60);
		LayoutParams timeParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		timeParams.addRule(RelativeLayout.BELOW, R.id.single_message_content);
		RoundedImageView img = (RoundedImageView) convertView
				.findViewById(R.id.single_message_image);
		if (item.getMessages(item.getFirstConversationId()).get(position)
				.getSenderId() % 2 != 0) {
			imageParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			imageParams.addRule(RelativeLayout.ALIGN_TOP,
					R.id.single_message_content);
			lp.addRule(RelativeLayout.LEFT_OF, R.id.single_message_image);
			message.setBackgroundResource(R.drawable.bubble_right);

			img.setImageResource(user.getImageResource());
			timeParams.addRule(RelativeLayout.ALIGN_RIGHT,
					R.id.single_message_content);
		} else {
			imageParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			imageParams.addRule(RelativeLayout.ALIGN_BOTTOM,
					R.id.single_message_content);
			lp.addRule(RelativeLayout.RIGHT_OF, R.id.single_message_image);
			message.setBackgroundResource(R.drawable.bubble_left);
			if (item.getImageUrl() != null) {
				ImageLoader.getInstance().displayImage(item.getImageUrl(), img);
			} else {
				img.setImageResource(item.getImageResource());
			}
			timeParams.addRule(RelativeLayout.ALIGN_LEFT,
					R.id.single_message_content);
		}
		message.setMinimumWidth((int) (parent.getWidth() * 0.25));
		message.setMaxWidth((int) (parent.getWidth() * 0.7));
		message.setLayoutParams(lp);
		img.setLayoutParams(imageParams);
		messageTime.setLayoutParams(timeParams);
		return convertView;
	}

}
