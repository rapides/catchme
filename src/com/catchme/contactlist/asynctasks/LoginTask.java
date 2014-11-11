package com.catchme.contactlist.asynctasks;

import org.json.JSONObject;

import com.catchme.R;
import com.catchme.connections.ConnectionConst;
import com.catchme.connections.ServerConection;
import com.catchme.connections.ServerRequests;
import com.catchme.contactlist.CustomListAdapter;
import com.catchme.contactlist.DrawerMenuAdapter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class LoginTask extends AsyncTask<String, Void, JSONObject> {
	private Context context;
	private DrawerMenuAdapter drawerAdapter;
	private CustomListAdapter listAdapter;
	private SwipeRefreshLayout swipeLayout;

	public LoginTask(DrawerMenuAdapter drawerAdapter,
			CustomListAdapter listAdapter, SwipeRefreshLayout swipeLayout) {
		super();
		this.drawerAdapter = drawerAdapter;
		this.listAdapter = listAdapter;
		this.swipeLayout = swipeLayout;
		context = swipeLayout.getContext();
	}

	@Override
	protected void onPreExecute() {
		swipeLayout.setRefreshing(true);
	}

	@Override
	protected JSONObject doInBackground(String... params) {
		JSONObject result = null;
		if (ServerConection.isOnline(context)) {
			String login = params[0];
			String password = params[1];
			result = ServerConection.JsonPOST(ConnectionConst.URL_CONTACTS_ALL,
					ServerRequests.getTokenRequest(login, password));
		}else{
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
			/*
			 * try { System.out.println(result.get(ConnectionConst.TOKEN)); }
			 * catch (JSONException e) { Log.e("JSONParseError",
			 * e.getMessage()); }
			 */
			drawerAdapter.notifyDataSetChanged();
			listAdapter.notifyDataSetChanged();
		}
		swipeLayout.setRefreshing(false);
	}

}
