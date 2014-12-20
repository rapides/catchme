package com.catchme.messages;

import java.util.List;

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
import com.catchme.database.CatchmeDatabaseAdapter;
import com.catchme.model.ExampleItem;
import com.catchme.model.LoggedUser;
import com.catchme.model.Message;
import com.catchme.utils.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MessagesListAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private Activity activity;
	private List<Message> messagesList;
	private LoggedUser user;
	private ExampleItem item;
	private CatchmeDatabaseAdapter dbAdapter;
	private long conversationId;

	public MessagesListAdapter(Activity activity, LoggedUser user,
			ExampleItem item, CatchmeDatabaseAdapter dbAdapter,
			long conversationId) {
		this.activity = activity;
		this.user = user;
		this.item = item;
		this.conversationId = conversationId;
		this.dbAdapter = dbAdapter;
		this.messagesList = dbAdapter.getMessages(conversationId);
	}

	@Override
	public int getCount() {
		if (messagesList != null) {
			return messagesList.size();
		} else {
			return 0;
		}
	}

	@Override
	public Message getItem(int position) {
		return messagesList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return messagesList.get(position).getMessageId();
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
		RelativeLayout containter = (RelativeLayout) convertView
				.findViewById(R.id.single_message_containter);
		TextView message = (TextView) convertView
				.findViewById(R.id.single_message_content);
		RoundedImageView img = (RoundedImageView) convertView
				.findViewById(R.id.single_message_image);

		message.setText(messagesList.get(position).getContent());
		messageTime.setText(messagesList.get(position).getTime());

		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		LayoutParams imageParams = new LayoutParams(55, 55);
		LayoutParams timeParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		timeParams.addRule(RelativeLayout.BELOW, R.id.single_message_content);
		timeParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		// img.setPadding(10, 10, 10, 10);
		imageParams.setMargins(0, 5, 0, 5);
		if (messagesList.get(position).getSenderId() == user.getId()) {
			imageParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			imageParams.addRule(RelativeLayout.ALIGN_BOTTOM,
					R.id.single_message_containter);
			lp.addRule(RelativeLayout.LEFT_OF, R.id.single_message_image);
			containter.setBackgroundResource(R.drawable.bubble_right_2);
			ImageLoader.getInstance()
					.displayImage(user.getSmallImageUrl(), img);

			timeParams.addRule(RelativeLayout.ALIGN_RIGHT,
					R.id.single_message_content);
			timeParams.setMargins(0, 0, 10, 0);
		} else {
			imageParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			imageParams.addRule(RelativeLayout.ALIGN_TOP,
					R.id.single_message_containter);
			lp.addRule(RelativeLayout.RIGHT_OF, R.id.single_message_image);
			containter.setBackgroundResource(R.drawable.bubble_left_2);
			ImageLoader.getInstance()
					.displayImage(item.getSmallImageUrl(), img);
			timeParams.addRule(RelativeLayout.ALIGN_LEFT,
					R.id.single_message_content);
			timeParams.setMargins(17, 0, 0, 0);
		}
		img.setLayoutParams(imageParams);
		message.setMinimumWidth((int) (parent.getWidth() * 0.25));
		message.setMaxWidth((int) (parent.getWidth() * 0.7));
		containter.setLayoutParams(lp);
		messageTime.setLayoutParams(timeParams);
		return convertView;
	}

	@Override
	public void notifyDataSetChanged() {
		messagesList = dbAdapter.getMessages(conversationId);
		super.notifyDataSetChanged();
	}
}
