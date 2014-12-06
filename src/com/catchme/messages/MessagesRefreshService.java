package com.catchme.messages;

import java.util.List;

import android.app.IntentService;
import android.content.Intent;
import android.widget.Toast;

import com.catchme.exampleObjects.ExampleContent;
import com.catchme.exampleObjects.ExampleItem;
import com.catchme.exampleObjects.Message;

public class MessagesRefreshService extends IntentService {
	//private Handler handler = new Handler();// handler.post(new Runnable(){});

	public MessagesRefreshService() {
		super("C@tchme Message check");
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		ExampleItem item = ExampleContent.ITEM_MAP.get(intent.getExtras()
				.getLong("item_id"));
		List<Message> messages = item
				.getMessages(item.getFirstConversationId());
		if (messages != null) {
			System.out.println(""
					+ messages.get(messages.size() - 1).getMessageId());
		}
	}

	/*private Runnable sendUpdatesToUI = new Runnable() {
		public void run() {
			handler.post(this); 
		}
	};*/

	@Override
	public void onDestroy() {
		Toast.makeText(this, "Service Destroyed", Toast.LENGTH_SHORT).show();
		super.onDestroy();
	}
}
