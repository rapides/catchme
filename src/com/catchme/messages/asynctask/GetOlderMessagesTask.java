package com.catchme.messages.asynctask;

import java.util.ArrayList;

import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
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

public class GetOlderMessagesTask extends AsyncTask<Long, Void, JSONObject> {
	private MessagesListAdapter adapter;
	private ListView listView;
	private ExampleItem item;
	private SwipeRefreshLayout swipeLatout;
	private Context context;
	private ArrayList<Message> olderMessages;
	private long conversationId;
	private GetOlderMessagesTaskCompleted listener;

	public GetOlderMessagesTask(ListView listView, ExampleItem item,
			SwipeRefreshLayout swipeLayout, long conversationId,
			GetOlderMessagesTaskCompleted listener) {
		super();
		this.listView = listView;
		this.item = item;
		this.swipeLatout = swipeLayout;
		this.adapter = (MessagesListAdapter) listView.getAdapter();
		this.context = listView.getContext();
		this.conversationId = conversationId;
		this.listener = listener;
		olderMessages = new ArrayList<Message>();
	}

	@Override
	protected void onPreExecute() {
		swipeLatout.setRefreshing(true);
	}

	@Override
	protected JSONObject doInBackground(Long... params) {
		long oldestMessageId = params[0];
		LoggedUser user = ItemListActivity.getLoggedUser(context);
		String token = user.getToken();
		JSONObject result = new JSONObject();
		if (ServerConnection.isOnline(context)) {
			result = ServerRequests.getMessagesOlder(token, conversationId,
					oldestMessageId);
			olderMessages = ReadServerResponse.getMessagesList(result);
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
			item.addOlderMessages(conversationId, olderMessages);
			adapter.notifyDataSetChanged();
			listView.setSelection(listView.getFirstVisiblePosition()
					+ olderMessages.size());
			listener.setIsMoreMessagesAvailable(olderMessages.size() != 0);
		} else {
			Toast.makeText(context, "Message get OLDER server problem",
					Toast.LENGTH_SHORT).show();
		}
		swipeLatout.setRefreshing(false);
	}
}
