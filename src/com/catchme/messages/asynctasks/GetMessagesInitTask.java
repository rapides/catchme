package com.catchme.messages.asynctasks;

import java.util.ArrayList;

import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.catchme.R;
import com.catchme.connections.ReadServerResponse;
import com.catchme.connections.ServerConnection;
import com.catchme.connections.ServerRequests;
import com.catchme.contactlist.ItemListActivity;
import com.catchme.exampleObjects.ExampleItem;
import com.catchme.exampleObjects.LoggedUser;
import com.catchme.exampleObjects.Message;
import com.catchme.messages.interfaces.GetMessagesListener;

public class GetMessagesInitTask extends AsyncTask<Long, Void, JSONObject> {
	private Context context;
	private ExampleItem item;
	GetMessagesListener listener;
	private long conversationId;

	public GetMessagesInitTask(Context context, ExampleItem item,
			GetMessagesListener listener) {
		super();
		this.item = item;
		this.context = context;
		this.listener = listener;
	}

	@Override
	protected void onPreExecute() {
		listener.onPreGetMessages();
	}

	@Override
	protected JSONObject doInBackground(Long... params) {
		LoggedUser user = ItemListActivity.getLoggedUser(context);
		String token = user.getToken();
		conversationId = params[0];
		JSONObject result = new JSONObject();
		if (ServerConnection.isOnline(context)) {
			result = ServerRequests.getMessagesInit(token, conversationId);
		} else {
			result = null;
		}
		return result;
	}

	@Override
	protected void onPostExecute(JSONObject result) {

		System.out.println("Done");
		if (result == null) {
			Toast.makeText(context,
					context.getResources().getString(R.string.err_no_internet),
					Toast.LENGTH_SHORT).show();
			listener.onGetMessagesError(null);
		} else if (ReadServerResponse.isSuccess(result)) {

			ArrayList<Message> messages = ReadServerResponse.getMessagesList(result);
			System.out.println("Succes: "+messages.size());
			item.addOlderMessages(conversationId, messages);
			listener.onGetMessagesCompleted(item.getId(), conversationId, messages.size());
		} else {
			listener.onGetMessagesError(ReadServerResponse.getErrors(result));
		}
	}
}
