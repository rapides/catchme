package com.catchme.messages;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.InboxStyle;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.util.LongSparseArray;
import android.util.Log;

import com.catchme.R;
import com.catchme.contactlist.ItemListActivity;
import com.catchme.exampleObjects.ExampleContent;
import com.catchme.exampleObjects.ExampleItem;
import com.catchme.exampleObjects.Message;
import com.catchme.messages.asynctasks.GetMessagesInitTask;
import com.catchme.messages.asynctasks.GetNewerMessagesTask;
import com.catchme.messages.interfaces.GetMessagesListener;
import com.catchme.messages.interfaces.NewerMessagesListener;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MessagesRefreshService extends IntentService implements
		GetMessagesListener, NewerMessagesListener {
	// private Handler handler = new Handler();// handler.post(new
	// Runnable(){});
	boolean isDestroyed;
	public static final String SERVICE_NAME = "C@tchme Message check";
	public static final String ITEM_ID = "itemId";
	public static final String CONVERSATION_ID = "conversationId";
	public static final String MESSAGES_COUNT = "messagesCount";
	public static final String BROADCAST_ERROR = "error message";
	public static final String BROADCAST_NEW_MESSAGE = "new message";
	public static final String ERROR_ARRAY = "errors";
	public static final String REFRESH_TIME = "refreshTime";
	public static final int MESSAGES_INTERVAL_SHORT = 5000;
	public static final int MESSAGES_INTERVAL_LONG = 300000;
	private LinkedList<Message> unreadedMessages = new LinkedList<Message>();
	private static int refreshTime = 5000;

	public MessagesRefreshService() {
		super(SERVICE_NAME);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		isDestroyed = false;
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		refreshTime = intent.getIntExtra(REFRESH_TIME, 49000);
		while (!isDestroyed) {
			Log.d("MessageService", "Working in bacground. Refresh time: "
					+ refreshTime);
			if (ExampleContent.ITEM_MAP != null) {
				for (int i = 0; i < ExampleContent.ITEM_MAP.size(); i++) {
					ExampleItem item = ExampleContent.ITEM_MAP.valueAt(i);
					List<Message> messages = item.getMessages(item
							.getFirstConversationId());
					if (messages == null) {
						new GetMessagesInitTask(getApplicationContext(), item,
								this).execute(item.getFirstConversationId());
					} else {
						long convId = item.getFirstConversationId();
						long newestMessageIt = item.getNewestMessageId(convId);
						new GetNewerMessagesTask(getApplicationContext(), item,
								this).execute(convId, newestMessageIt);
					}

				}
			}
			try {
				Thread.sleep(refreshTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * private Runnable sendUpdatesToUI = new Runnable() { public void run() {
	 * handler.post(this); } };
	 */

	@Override
	public void onDestroy() {
		isDestroyed = true;
		super.onDestroy();
	}

	@Override
	public void onPreGetMessages() {
	}

	@Override
	public void onGetMessagesCompleted(long itemId, long conversationId,
			int moreMessagesCount) {
		// sendNewMessageBroadcast(itemId, conversationId, moreMessagesCount);
	}

	@Override
	public void onGetMessagesError(LongSparseArray<String> errors) {
		sendErrorsBroadcast(errors);
	}

	@Override
	public void onNewMessageError(LongSparseArray<String> errors) {
		sendErrorsBroadcast(errors);
	}

	@Override
	public void onNewMessage(long itemId, long conversationId, int messagesCount) {
		if (messagesCount > 0) {
			if (refreshTime == MESSAGES_INTERVAL_SHORT) {
				sendNewMessageBroadcast(itemId, conversationId, messagesCount);
			} else {
				sendNotification(itemId, conversationId, messagesCount);
			}
		}
	}

	private void sendNewMessageBroadcast(long itemId, long conversationId,
			int messagesCount) {
		Intent resultIntent = new Intent();
		resultIntent.putExtra(ITEM_ID, itemId);
		resultIntent.putExtra(CONVERSATION_ID, conversationId);
		resultIntent.putExtra(MESSAGES_COUNT, messagesCount);
		resultIntent.setAction(BROADCAST_NEW_MESSAGE);
		LocalBroadcastManager.getInstance(this).sendBroadcast(resultIntent);
	}

	private void sendErrorsBroadcast(LongSparseArray<String> errors) {
		Intent resultIntent = new Intent();
		Gson gsonUser = new Gson();
		String json = gsonUser.toJson(errors);
		resultIntent.putExtra(ERROR_ARRAY, json);
		resultIntent.setAction(BROADCAST_ERROR);
		LocalBroadcastManager.getInstance(this).sendBroadcast(resultIntent);
	}

	private String getNotificationTitle(long itemId) {
		return "New message from "
				+ ExampleContent.ITEM_MAP.get(itemId).getFullName();
	}

	private InboxStyle getInboxStyle(long itemId, long conversationId,
			int messagesCount) {
		NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
		LinkedList<Message> messages = ExampleContent.ITEM_MAP.get(itemId)
				.getLastMessages(conversationId, messagesCount);
		inboxStyle.setBigContentTitle(getNotificationTitle(itemId));
		Collections.reverse(unreadedMessages);
		Collections.reverse(messages);
		for (int i = 0; i < messages.size(); i++) {
			unreadedMessages.add(messages.get(i));
		}
		Collections.reverse(unreadedMessages);
		for (int i = unreadedMessages.size() - 1; i >= 0; i--) {
			inboxStyle.addLine(unreadedMessages.get(i).getContent());
		}

		return inboxStyle;
	}

	private Intent prepareIntent(long itemId, long conversationId,
			int messagesCount) {
		Intent resultIntent = new Intent(this, ItemListActivity.class);
		resultIntent.putExtra(ITEM_ID, itemId);
		resultIntent.putExtra(CONVERSATION_ID, conversationId);
		resultIntent.putExtra(MESSAGES_COUNT, messagesCount);
		return resultIntent;
	}

	private void sendNotification(long itemId, long conversationId,
			int messagesCount) {
		String title = getNotificationTitle(itemId);
		Message lastMessage = ExampleContent.ITEM_MAP.get(itemId)
				.getNewestMessage(conversationId);
		Bitmap bmp = ImageLoader.getInstance().loadImageSync(
				ExampleContent.ITEM_MAP.get(itemId).getMediumImage());
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this).setContentTitle(title)
				.setContentText(lastMessage.getContent())
				.setSmallIcon(R.drawable.catch_me)
				.setNumber(unreadedMessages.size() + messagesCount)
				.setLargeIcon(bmp);

		NotificationCompat.InboxStyle inboxStyle = getInboxStyle(itemId,
				conversationId, messagesCount);
		Intent resultIntent = prepareIntent(itemId, conversationId,
				messagesCount);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		stackBuilder.addParentStack(ItemListActivity.class);
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setStyle(inboxStyle);
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(ItemListActivity.NOTIFICATION_ID,
				mBuilder.build());
	}
}
