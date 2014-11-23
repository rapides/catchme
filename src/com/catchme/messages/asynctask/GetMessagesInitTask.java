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
import com.catchme.exampleObjects.ExampleContent.ExampleItem;
import com.catchme.exampleObjects.Message;
import com.catchme.messages.MessagesListAdapter;

public class GetMessagesInitTask extends AsyncTask<String, Void, JSONObject> {
	private MessagesListAdapter adapter;
	private ListView listView;
	private SwipeRefreshLayout swipeLatout;
	private Context context;
	private ArrayList<Message> messages;
	private ExampleItem item;
	private long conversationId;

	public GetMessagesInitTask(ListView listView,
			SwipeRefreshLayout swipeLayout, ExampleItem item) {
		super();
		this.listView = listView;
		this.swipeLatout = swipeLayout;
		this.adapter = (MessagesListAdapter) listView.getAdapter();
		this.context = listView.getContext();
		this.item = item;
		messages = new ArrayList<Message>();
	}

	@Override
	protected void onPreExecute() {
		swipeLatout.setRefreshing(true);
	}

	@Override
	protected JSONObject doInBackground(String... params) {
		String token = params[0];
		conversationId = Long.parseLong(params[1]);
		JSONObject result = new JSONObject();
		if (ServerConnection.isOnline(context)) {
			result = ServerRequests.getMessagesInit(token, conversationId);
			messages = ReadServerResponse.getMessagesList(result);
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
			item.addOlderMessages(conversationId, messages);
			listView.setSelection(listView.getFirstVisiblePosition() +messages.size());
		} else {
			Toast.makeText(context, "Message INIT server error", Toast.LENGTH_SHORT)
					.show();
		}
		adapter.notifyDataSetChanged();
		swipeLatout.setRefreshing(false);
	}
}
