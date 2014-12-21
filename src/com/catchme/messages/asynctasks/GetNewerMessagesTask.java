package com.catchme.messages.asynctasks;

import java.util.LinkedList;

import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.catchme.connections.ReadServerResponse;
import com.catchme.connections.ServerConnection;
import com.catchme.connections.ServerRequests;
import com.catchme.contactlist.ItemListActivity;
import com.catchme.database.CatchmeDatabaseAdapter;
import com.catchme.messages.interfaces.NewerMessagesListener;
import com.catchme.model.ExampleItem;
import com.catchme.model.LoggedUser;
import com.catchme.model.Message;

public class GetNewerMessagesTask extends AsyncTask<Long, Void, JSONObject> {
	private Context context;
	private ExampleItem item;
	private NewerMessagesListener listener;
	private Long conversationId;
	private CatchmeDatabaseAdapter dbAdapter;

	public GetNewerMessagesTask(Context context, ExampleItem item,
			CatchmeDatabaseAdapter dbAdapter, NewerMessagesListener listener) {
		super();
		this.item = item;
		this.context = context;
		this.listener = listener;
		this.dbAdapter = dbAdapter;
	}

	@Override
	protected JSONObject doInBackground(Long... params) {
		conversationId = params[0];
		long oldestMessageId = params[1];
		LoggedUser user = ItemListActivity.getLoggedUser(context);
		String token = user.getToken();
		JSONObject result = new JSONObject();
		if (ServerConnection.isOnline(context)) {
			result = ServerRequests.getMessagesNewer(token, conversationId,
					oldestMessageId);
			if (ReadServerResponse.isSuccess(result) && dbAdapter.isOpened()) {
				dbAdapter.insertMessages(conversationId,
						ReadServerResponse.getMessagesList(result));
			}
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
			LinkedList<Message> newerMessages = ReadServerResponse
					.getMessagesList(result);
			listener.onNewMessage(item.getId(), conversationId,
					newerMessages);
		} else {
			Toast.makeText(context, "Message get NEWER problem",
					Toast.LENGTH_SHORT).show();
			listener.onNewMessageError(ReadServerResponse.getErrors(result));
		}
	}

}
