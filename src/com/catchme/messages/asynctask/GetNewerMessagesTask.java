package com.catchme.messages.asynctask;

import java.util.ArrayList;

import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.Toast;

import com.catchme.R;
import com.catchme.connections.ReadServerResponse;
import com.catchme.connections.ServerConnection;
import com.catchme.connections.ServerRequests;
import com.catchme.contactlist.ItemListActivity;
import com.catchme.exampleObjects.ExampleItem;
import com.catchme.exampleObjects.LoggedUser;
import com.catchme.exampleObjects.Message;
import com.catchme.messages.MessagesListAdapter;

public class GetNewerMessagesTask extends AsyncTask<Long, Void, JSONObject> {

	private long conversationId;
	private Context context;
	private ArrayList<Message> newerMessages;
	private MessagesListAdapter adapter;
	private ListView listView;
	private ExampleItem item;

	public GetNewerMessagesTask(ListView listView,
			ExampleItem item) {
		super();
		this.item = item;
		this.listView = listView;
		this.conversationId = item.getFirstConversationId();
		this.adapter = (MessagesListAdapter) listView.getAdapter();
		this.context = listView.getContext();
		newerMessages = new ArrayList<Message>();
	}

	@Override
	protected JSONObject doInBackground(Long... params) {
		long oldestMessageId = params[0];
		LoggedUser user = ItemListActivity.getLoggedUser(context);
		String token = user.getToken();
		JSONObject result = new JSONObject();
		if (ServerConnection.isOnline(context)) {
			result = ServerRequests.getMessagesNewer(token, conversationId,
					oldestMessageId);
			newerMessages = ReadServerResponse.getMessagesList(result);
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
		} else if (ReadServerResponse.isSuccess(result)) {
			item.addNewerMessages(conversationId, newerMessages);
			adapter.notifyDataSetChanged();
			listView.setSelection(listView.getCount()-1);
		} else {
			Toast.makeText(context, "Message get NEWER problem",
					Toast.LENGTH_SHORT).show();
		}
	}

}
