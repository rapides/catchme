package com.catchme.contactlist.asynctasks;

import java.util.HashMap;

import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.Toast;

import com.catchme.R;
import com.catchme.connections.ConnectionConst;
import com.catchme.connections.ServerConection;
import com.catchme.connections.ServerRequests;
import com.catchme.contactlist.CustomListAdapter;

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
	protected JSONObject doInBackground(String... params) {
		String token = params[0];
		JSONObject result = new JSONObject();
		if (ServerConection.isOnline(context)) {
			result = ServerConection.JsonPOST(ConnectionConst.URL_CONTACTS_ALL,
					ServerRequests.getAllContactsRequest());

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
		} else {
			adapter.notifyDataSetChanged();
		}
		swipeLayout.setRefreshing(false);
	}

}