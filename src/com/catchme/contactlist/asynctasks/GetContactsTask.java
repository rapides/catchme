package com.catchme.contactlist.asynctasks;

import java.util.ArrayList;

import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.Toast;

import com.catchme.R;
import com.catchme.connections.ReadServerResponse;
import com.catchme.connections.ServerConnection;
import com.catchme.connections.ServerRequests;
import com.catchme.contactlist.CustomListAdapter;
import com.catchme.exampleObjects.ExampleContent;
import com.catchme.exampleObjects.ExampleContent.ExampleItem;

public class GetContactsTask extends AsyncTask<String, Void, JSONObject> {

	private SwipeRefreshLayout swipeLayout;
	private CustomListAdapter adapter;
	private Context context;

	public GetContactsTask(SwipeRefreshLayout swipeLayout,
			CustomListAdapter listAdapter) {
		super();
		this.swipeLayout = swipeLayout;
		this.adapter = listAdapter;
		context = swipeLayout.getContext();
	}

	@Override
	protected void onPreExecute() {
		swipeLayout.setRefreshing(true);
	}

	@Override
	protected JSONObject doInBackground(String... params) {
		String token = params[0];
		JSONObject result = null;
		if (ServerConnection.isOnline(context)) {
			result = ServerRequests.getSentContactsRequest(token);
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
			if (ReadServerResponse.isSuccess(result)) {
				Toast.makeText(
						context,
						"Refresh succeded. Contact count: "
								+ ReadServerResponse.getContactList(result)
										.size(), Toast.LENGTH_SHORT).show();
				addItemsToDatabase(ReadServerResponse.getContactList(result));
			} else {
				Toast.makeText(
						context,
						"Refresh Failed, server error\n"
								+ ReadServerResponse.getErrors(result),
						Toast.LENGTH_SHORT).show();
			}
		}
		swipeLayout.setRefreshing(false);
	}

	private void addItemsToDatabase(ArrayList<ExampleItem> itemList) {
		ExampleContent.updateItems(itemList);
		adapter.swapItems(itemList);
	}
}