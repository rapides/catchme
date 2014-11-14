package com.catchme.contactlist.asynctasks;

import org.json.JSONObject;

import com.catchme.R;
import com.catchme.connections.ReadServerResponse;
import com.catchme.connections.ServerConnection;
import com.catchme.connections.ServerRequests;
import com.catchme.contactlist.DrawerMenuAdapter;
import com.catchme.exampleObjects.ExampleContent;
import com.catchme.exampleObjects.ExampleContent.LoggedUser;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.Toast;

public class LoginTask extends AsyncTask<String, Void, JSONObject> {
	private Context context;
	private DrawerMenuAdapter drawerAdapter;
	private SwipeRefreshLayout swipeLayout;

	public LoginTask(DrawerMenuAdapter drawerAdapter, SwipeRefreshLayout swipeLayout) {
		super();
		this.drawerAdapter = drawerAdapter;
		this.swipeLayout = swipeLayout;
		context = swipeLayout.getContext();
	}

	@Override
	protected void onPreExecute() {
		swipeLayout.setRefreshing(true);
	}

	@Override
	protected JSONObject doInBackground(String... params) {
		JSONObject result = new JSONObject();
		if (ServerConnection.isOnline(context)) {
			String login = params[0];
			String password = params[1];
			result = ServerRequests.getTokenRequest(login, password);
			setCurrentLoggedUser(ReadServerResponse.getLoggedUser(result));
		}else{
			result = null;
		}

		return result;
	}

	private void setCurrentLoggedUser(LoggedUser loggedUser) {
		ExampleContent.currentUser = loggedUser;
	}

	@Override
	protected void onPostExecute(JSONObject result) {
		if (result == null) {
			Toast.makeText(context,
					context.getResources().getString(R.string.err_no_internet),
					Toast.LENGTH_SHORT).show();
		} else {
			drawerAdapter.notifyDataSetChanged();
		}
		swipeLayout.setRefreshing(false);
	}

}
