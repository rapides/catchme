package com.catchme.messages.asynctasks;

import java.util.ArrayList;

import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.catchme.connections.ReadServerResponse;
import com.catchme.connections.ServerConnection;
import com.catchme.connections.ServerRequests;
import com.catchme.contactlist.ItemListActivity;
import com.catchme.exampleObjects.ExampleItem;
import com.catchme.exampleObjects.LoggedUser;
import com.catchme.exampleObjects.Message;
import com.catchme.messages.interfaces.NewerMessagesListener;

public class GetNewerMessagesTask extends AsyncTask<Long, Void, JSONObject> {
	private Context context;
	private ExampleItem item;
	private NewerMessagesListener listener;
	private Long conversationId;

	public GetNewerMessagesTask(Context context, ExampleItem item,
			NewerMessagesListener listener) {
		super();
		this.item = item;
		this.context = context;
		this.listener = listener;
	}

	@Override
	protected JSONObject doInBackground(Long... params) {
		conversationId = params[0];
		long oldestMessageId = params[1];
		LoggedUser user = ItemListActivity.getLoggedUser(context);
		String token = user.getToken();
		JSONObject result = new JSONObject();
		if (ServerConnection.isOnline(context)) {
			result = ServerRequests.getMessagesNewer(token,conversationId, oldestMessageId);
		} else {
			result = null;
		}
		return result;
	}

	@Override
	protected void onPostExecute(JSONObject result) {
		if (result == null) {
			listener.onNewMessageError(null);
		} else if (ReadServerResponse.isSuccess(result)) {
			ArrayList<Message> newerMessages = ReadServerResponse
					.getMessagesList(result);
			item.addNewerMessages(conversationId, newerMessages);
			listener.onNewMessage(item.getId(), conversationId, newerMessages.size());
		} else {
			Toast.makeText(context, "Message get NEWER problem",
					Toast.LENGTH_SHORT).show();
			listener.onNewMessageError(ReadServerResponse.getErrors(result));
		}
	}

}
