package com.catchme.messages.asynctasks;

import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.catchme.R;
import com.catchme.connections.ReadServerResponse;
import com.catchme.connections.ServerConnection;
import com.catchme.connections.ServerRequests;
import com.catchme.contactlist.ItemListActivity;
import com.catchme.database.CatchmeDatabaseAdapter;
import com.catchme.database.model.ExampleItem;
import com.catchme.database.model.LoggedUser;
import com.catchme.messages.interfaces.GetMessagesListener;

public class GetOlderMessagesTask extends AsyncTask<Long, Void, JSONObject> {
	private ExampleItem item;
	private Context context;
	private GetMessagesListener listener;
	private long conversationId;

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
		conversationId = params[0];
		long oldestMessageId = params[1];
		LoggedUser user = ItemListActivity.getLoggedUser(context);
		String token = user.getToken();
		JSONObject result = new JSONObject();
		if (ServerConnection.isOnline(context)) {
			result = ServerRequests.getMessagesOlder(token, conversationId,
					oldestMessageId);
			if (ReadServerResponse.isSuccess(result)) {
				CatchmeDatabaseAdapter.getInstance(context).insertMessages(
						conversationId,
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
			Toast.makeText(context,
					context.getResources().getString(R.string.err_no_internet),
					Toast.LENGTH_SHORT).show();
			listener.onGetMessagesError(null);
		} else if (ReadServerResponse.isSuccess(result)) {
			listener.onGetMessagesCompleted(item.getId(), conversationId,
					ReadServerResponse.getMessagesList(result));
		} else {
			listener.onGetMessagesError(ReadServerResponse.getErrors(result));
		}
	}
}
