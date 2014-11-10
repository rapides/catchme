package com.catchme.contactlist.asynctasks;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ListAdapter;
import android.widget.SearchView;

import com.catchme.connections.ConnectionConst;
import com.catchme.connections.ServerConection;
import com.catchme.contactlist.CustomListAdapter;

public class GetContactsTask extends AsyncTask<String, Void, String> {

	private SwipeRefreshLayout swipeLayout;
	private CustomListAdapter adapter;

	public GetContactsTask(SwipeRefreshLayout swipeLayout, CustomListAdapter listAdapter) {
		super();
		this.swipeLayout = swipeLayout;
		this.adapter = listAdapter;
	}

	@Override
	protected String doInBackground(String... params) {
		String token = params[0];
		HashMap<String, HashMap<String, String>> data = new HashMap<String, HashMap<String, String>>();
		HashMap<String, String> dataContent = new HashMap<String, String>();
		dataContent.put(ConnectionConst.TOKEN, token);
		data.put(ConnectionConst.USER, dataContent);
		// TODO request structure
		// String result =
		// ServerConection.JsonPOST(ConnectionConst.URL_CONTACTS_ALL, data);
		String result = "test";
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONObject json;
		// json = new JSONObject(result);
		// ((JSONObject) json.get("user")).get("email"));
		// TODO import contacts to database
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		adapter.notifyDataSetChanged();
		swipeLayout.setRefreshing(false);
	}
}