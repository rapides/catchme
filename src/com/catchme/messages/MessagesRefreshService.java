package com.catchme.messages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.app.NotificationCompat.InboxStyle;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.util.LongSparseArray;
import android.util.Log;

import com.catchme.R;
import com.catchme.contactlist.ItemListActivity;
import com.catchme.database.CatchmeDatabaseAdapter;
import com.catchme.database.model.ExampleItem;
import com.catchme.database.model.Message;
import com.catchme.database.model.ExampleItem.ContactStateType;
import com.catchme.messages.asynctasks.GetNewerMessagesTask;
import com.catchme.messages.interfaces.NewerMessagesListener;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MessagesRefreshService extends IntentService implements
		NewerMessagesListener {
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
	public static final int MESSAGES_INTERVAL_LONG = 15000;
	private LongSparseArray<LinkedList<Message>> unreadedMessages = new LongSparseArray<LinkedList<Message>>();

	private CatchmeDatabaseAdapter dbAdapter;
	private static int refreshTime = 5000;

	public MessagesRefreshService() {
		super(SERVICE_NAME);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		dbAdapter = new CatchmeDatabaseAdapter(this);
		dbAdapter.open();
		isDestroyed = false;
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		refreshTime = intent.getIntExtra(REFRESH_TIME, 49000);
		while (!isDestroyed) {
			Log.d("MessageService", "Working in background. Refresh time: "
					+ refreshTime);
			ArrayList<ExampleItem> items = dbAdapter
					.getItems(ContactStateType.ACCEPTED);
			for (ExampleItem item : items) {
				long convId = item.getFirstConversationId();
				Message lastMessage = dbAdapter.getLastMessage(convId);
				if(lastMessage!=null){
					new GetNewerMessagesTask(getApplicationContext(), item,
							dbAdapter, this).execute(convId,
							lastMessage.getMessageId());
				}else{
					//TODO getmessageInit if first run
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
		dbAdapter.close();
		super.onDestroy();
	}

	@Override
	public void onNewMessageError(LongSparseArray<String> errors) {
		sendErrorsBroadcast(errors);
	}

	@Override
	public void onNewMessage(long itemId, long conversationId, LinkedList<Message> newerMessages) {
		if (newerMessages.size() > 0) {
			if (refreshTime == MESSAGES_INTERVAL_SHORT) {
				sendNewMessageBroadcast(itemId, conversationId, newerMessages.size());
			} else {
				sendNotification(itemId, conversationId, newerMessages);
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

	private InboxStyle getInboxStyle(long itemId, long conversationId, String title) {
		InboxStyle inboxStyle = new InboxStyle();

		inboxStyle.setBigContentTitle(title);

		for (int i = unreadedMessages.get(itemId).size() - 1; i >= 0; i--) {
			inboxStyle
					.addLine(unreadedMessages.get(itemId).get(i).getContent());
		}

		return inboxStyle;
	}

	private InboxStyle getMultipleItemsInboxStyle(String title) {
		InboxStyle inboxStyle = new InboxStyle();
		inboxStyle.setBigContentTitle(title);
		for (int i = 0; i < unreadedMessages.size(); i++) {
			LinkedList<Message> m = unreadedMessages.get(unreadedMessages
					.keyAt(i));
			ExampleItem item = dbAdapter.getItem(unreadedMessages.keyAt(i));
			inboxStyle.addLine(item.getName() + ": "
					+ m.get(m.size() - 1).getContent());
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
			LinkedList<Message> newerMessages) {
		updateMessages(itemId, conversationId, newerMessages);

		Message lastMessage = dbAdapter.getLastMessage(conversationId);
		Intent resultIntent;
		InboxStyle inboxStyle;
		Builder mBuilder;
		if (!isInNotification()) {
			String title = dbAdapter.getItem(itemId).getFullName();
			Bitmap bmp = ImageLoader.getInstance().loadImageSync(
					dbAdapter.getItem(itemId).getMediumImageUrl());
			mBuilder = new Builder(this).setContentTitle(title)
					.setContentText(lastMessage.getContent())
					.setSmallIcon(R.drawable.catch_me)
					.setNumber(unreadedMessages.get(itemId).size())
					.setLargeIcon(bmp);
			inboxStyle = getInboxStyle(itemId, conversationId,
					title);
			resultIntent = prepareIntent(itemId, conversationId, newerMessages.size());

		} else {
			String title = unreadedMessages.size() + " "
					+ getResources().getString(R.string.new_messages);
			String content = dbAdapter.getItem(unreadedMessages.keyAt(0))
					.getName();
			for (int i = 1; i < unreadedMessages.size(); i++) {
				content += ", "
						+ dbAdapter.getItem(unreadedMessages.keyAt(i))
								.getName();
			}
			mBuilder = new Builder(this).setContentTitle(title)
					.setContentText(content).setSmallIcon(R.drawable.catch_me);
			inboxStyle = getMultipleItemsInboxStyle(title);
			resultIntent = new Intent(this, ItemListActivity.class);
		}
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

	private void updateMessages(long itemId, long conversationId,
			LinkedList<Message> newerMessages) {
		LinkedList<Message> olderUnreaded = unreadedMessages.get(itemId);
		Collections.reverse(newerMessages);
		if(olderUnreaded!=null){
			newerMessages.addAll(olderUnreaded);
		}
		unreadedMessages.put(itemId, newerMessages);
	}

	private boolean isInNotification() {
		return unreadedMessages.size() > 1;
	}

}
