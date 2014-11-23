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
import com.catchme.exampleObjects.ExampleContent.ExampleItem.ContactStateType;

public class GetContactsTask extends AsyncTask<String, Void, JSONObject> {

	private SwipeRefreshLayout swipeLayout;
	private CustomListAdapter adapter;
	private Context context;
	private ContactStateType state;
	
	
	public GetContactsTask(SwipeRefreshLayout swipeLayout,
			CustomListAdapter listAdapter, ContactStateType state) {
		super();
		this.swipeLayout = swipeLayout;
		this.adapter = listAdapter;
		this.state = state;
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
			if(state == ContactStateType.ACCEPTED){
				result = ServerRequests.getAcceptedContactsRequest(token);
			}else if(state == ContactStateType.SENT){
				result = ServerRequests.getSentContactsRequest(token);
			}else if(state == ContactStateType.RECEIVED){
				result = ServerRequests.getReceivedContactsRequest(token);
			}
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