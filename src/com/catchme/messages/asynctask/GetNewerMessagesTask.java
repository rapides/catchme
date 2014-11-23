package com.catchme.messages.asynctask;

import java.util.ArrayList;

import org.json.JSONObject;

import com.catchme.R;
import com.catchme.connections.ReadServerResponse;
import com.catchme.connections.ServerConnection;
import com.catchme.connections.ServerRequests;
import com.catchme.contactlist.ItemListActivity;
import com.catchme.exampleObjects.ExampleContent.ExampleItem;
import com.catchme.exampleObjects.ExampleContent.LoggedUser;
import com.catchme.exampleObjects.Message;
import com.catchme.messages.MessagesListAdapter;
import com.google.gson.Gson;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.Toast;

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
		SharedPreferences preferences = context.getSharedPreferences(
				ItemListActivity.PREFERENCES, Context.MODE_PRIVATE);
		Gson gson = new Gson();
		String json = preferences.getString(ItemListActivity.USER, "");
		LoggedUser user = gson.fromJson(json, LoggedUser.class);
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
