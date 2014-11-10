package com.catchme.contactlist.asynctasks;

import java.util.HashMap;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.Toast;

import com.catchme.R;
import com.catchme.connections.ConnectionConst;
import com.catchme.connections.ServerConection;
import com.catchme.contactlist.CustomListAdapter;

public class GetContactsTask extends AsyncTask<String, Void, String> {

	private SwipeRefreshLayout swipeLayout;
	private CustomListAdapter adapter;
	private Context context;

	public GetContactsTask(SwipeRefreshLayout swipeLayout, CustomListAdapter listAdapter) {
		super();
		this.swipeLayout = swipeLayout;
		this.adapter = listAdapter;
		context = swipeLayout.getContext();
	}

	@Override
	protected String doInBackground(String... params) {
		String token = params[0];
		HashMap<String, HashMap<String, String>> data = new HashMap<String, HashMap<String, String>>();
		HashMap<String, String> dataContent = new HashMap<String, String>();
		dataContent.put(ConnectionConst.TOKEN, token);
		data.put(ConnectionConst.USER, dataContent);
		if(ServerConection.isOnline(context)){
			// TODO request structure
			// String result =
			// ServerConection.JsonPOST(ConnectionConst.URL_CONTACTS_ALL, data);
			String result = "test";
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				return e.getMessage();
			}
			//JSONObject json;
			// json = new JSONObject(result);
			// ((JSONObject) json.get("user")).get("email"));
			// TODO import contacts to database
			return result;
		}else{
			return null;
		}
		
	}

	@Override
	protected void onPostExecute(String result) {
		if(result == null){
			Toast.makeText(context, context.getResources().getString(R.string.err_no_internet), Toast.LENGTH_SHORT).show();
		}else{
			adapter.notifyDataSetChanged();
		}
		swipeLayout.setRefreshing(false);
	}
	
}