package com.catchme.contactlist.asynctasks;

import java.util.ArrayList;

import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.Toast;

import com.catchme.R;
import com.catchme.connections.ServerConnection;
import com.catchme.connections.ReadServerResponse;
import com.catchme.connections.ServerRequests;
import com.catchme.contactlist.CustomListAdapter;
import com.catchme.exampleObjects.ExampleContent;
import com.catchme.exampleObjects.ExampleContent.ExampleItem;

public class GetContactsTask extends AsyncTask<String, Void, JSONObject> {

	private SwipeRefreshLayout swipeLayout;
	private CustomListAdapter adapter;
	private Context context;

	@Override
	protected void onPreExecute() {
		swipeLayout.setRefreshing(true);
	}

	public GetContactsTask(SwipeRefreshLayout swipeLayout,
			CustomListAdapter listAdapter) {
		super();
		this.swipeLayout = swipeLayout;
		this.adapter = listAdapter;
		context = swipeLayout.getContext();
	}

	@Override
	protected JSONObject doInBackground(String... params) {
		String token = params[0];
		JSONObject result = new JSONObject();
		if (ServerConnection.isOnline(context)) {
			result = ServerRequests.getAcceptedContactsRequest(token);
			addItemsToDatabase(ReadServerResponse.getContactList(result));
		}
		return result;

	}

	@Override
	protected void onPostExecute(JSONObject result) {
		if (result == null) {
			Toast.makeText(context,
					context.getResources().getString(R.string.err_no_internet),
					Toast.LENGTH_SHORT).show();
		} else {
			adapter.notifyDataSetChanged();
		}
		swipeLayout.setRefreshing(false);
	}

	private void addItemsToDatabase(ArrayList<ExampleItem> itemList) {
		ExampleContent.updateItems(itemList);
	}
}