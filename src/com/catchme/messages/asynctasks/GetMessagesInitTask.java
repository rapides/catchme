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
import com.catchme.messages.interfaces.GetMessagesListener;
import com.catchme.model.ExampleItem;
import com.catchme.model.LoggedUser;

public class GetMessagesInitTask extends AsyncTask<Long, Void, JSONObject> {
	private Context context;
	GetMessagesListener listener;
	private ExampleItem item;
	private long conversationId;
	private CatchmeDatabaseAdapter dbAdapter;

	public GetMessagesInitTask(Context context, ExampleItem item,
			CatchmeDatabaseAdapter dbAdapter, GetMessagesListener listener) {
		super();
		this.context = context;
		this.item = item;
		this.listener = listener;
		this.dbAdapter = dbAdapter;
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

			if(ReadServerResponse.isSuccess(result) && dbAdapter.isOpened()){
				dbAdapter.insertMessages(conversationId, ReadServerResponse
						.getMessagesList(result));
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
			listener.onGetMessagesCompleted(item.getId(), conversationId, ReadServerResponse
					.getMessagesList(result));
		} else {
			listener.onGetMessagesError(ReadServerResponse.getErrors(result));
		}
	}
}
