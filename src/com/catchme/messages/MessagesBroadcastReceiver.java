package com.catchme.messages;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.util.LongSparseArray;
import android.widget.Toast;

import com.catchme.messages.interfaces.NewerMessagesListener;
import com.google.gson.Gson;

public class MessagesBroadcastReceiver extends BroadcastReceiver {

	private NewerMessagesListener messageListener;

	
	public MessagesBroadcastReceiver(NewerMessagesListener listener) {
		super();
		this.messageListener = listener;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle data = intent.getExtras();
		if (intent.getAction().equalsIgnoreCase(
				MessagesRefreshService.BROADCAST_NEW_MESSAGE)) {

			long itemId = data.getLong(MessagesRefreshService.ITEM_ID);
			long convId = data.getLong(MessagesRefreshService.CONVERSATION_ID);
			/*int messagesCount = data
					.getInt(MessagesRefreshService.MESSAGES_COUNT);*/
			
			messageListener.onNewMessage(itemId, convId, null);
			Vibrator vibrator = (Vibrator) context
					.getSystemService(Context.VIBRATOR_SERVICE);
			vibrator.vibrate(100);
			Toast.makeText(context, "Vibrate!", Toast.LENGTH_SHORT).show();
			
		} else if (intent.getAction().equalsIgnoreCase(
				MessagesRefreshService.BROADCAST_ERROR)) {
			String json = data.getString(MessagesRefreshService.ERROR_ARRAY);
			@SuppressWarnings("unchecked")
			LongSparseArray<String> errors = new Gson().fromJson(json,
					LongSparseArray.class);
			messageListener.onNewMessageError(errors);
		}
	}
}