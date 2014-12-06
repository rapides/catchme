package com.catchme.messages.asynctasks;

import java.util.List;

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

public class GetOlderMessagesTask extends AsyncTask<Long, Void, JSONObject> {
	private ExampleItem item;
	private Context context;
	private GetMessagesListener listener;

	public GetOlderMessagesTask(Context context, ExampleItem item,
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
		long oldestMessageId = params[0];
		LoggedUser user = ItemListActivity.getLoggedUser(context);
		String token = user.getToken();
		JSONObject result = new JSONObject();
		if (ServerConnection.isOnline(context)) {
			result = ServerRequests.getMessagesOlder(token,
					item.getFirstConversationId(), oldestMessageId);
		} else {
			result = null;
		}
		return result;
	}

	@Override
	protected void onPostExecute(JSONObject result) {
		if (result == null) {
			Toast.makeText(context,
					context.getResources().getString(R.string.err_no_internet),
					Toast.LENGTH_SHORT).show();
			listener.onGetMessagesError(null);
		} else if (ReadServerResponse.isSuccess(result)) {
			List<Message> olderMessages = ReadServerResponse.getMessagesList(result);;
			item.addOlderMessages(item.getFirstConversationId(), olderMessages); 
			listener.onGetMessagesCompleted(olderMessages.size());
		} else {
			listener.onGetMessagesError(ReadServerResponse.getErrors(result));
		}
	}
}
