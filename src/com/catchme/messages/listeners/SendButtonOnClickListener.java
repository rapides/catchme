package com.catchme.messages.listeners;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.catchme.exampleObjects.ExampleItem;
import com.catchme.exampleObjects.LoggedUser;
import com.catchme.messages.asynctask.SendMessageTask;

public class SendButtonOnClickListener implements OnClickListener {
	private Context context;
	private LoggedUser user;
	private ExampleItem item;
	private TextView textField;
	private OnMessageSent listener;

	
	public SendButtonOnClickListener(Context context, LoggedUser user,
			ExampleItem item, TextView textField, OnMessageSent listener) {
		this.context = context;
		this.user = user;
		this.item = item;
		this.textField = textField;
		this.listener = listener;
	}


	@Override
	public void onClick(View v) {
		/*
		 * int notifyID = 1; NotificationCompat.Builder mNotifyBuilder = new
		 * NotificationCompat.Builder(
		 * v.getContext()).setSmallIcon(R.drawable.o2)
		 * .setContentTitle("My notification") .setContentText("Hello World!");
		 * NotificationManager mNotificationManager = (NotificationManager) v
		 * .getContext().getSystemService(Context.NOTIFICATION_SERVICE);
		 * 
		 * Notification note = mNotifyBuilder.build(); note.defaults |=
		 * Notification.DEFAULT_VIBRATE; note.defaults |=
		 * Notification.DEFAULT_SOUND; mNotificationManager.notify(notifyID,
		 * note);
		 */
		textField.setEnabled(false);
		new SendMessageTask(context, item.getFirstConversationId(), listener)
				.execute(user.getToken(), textField.getText().toString());
	}

}
