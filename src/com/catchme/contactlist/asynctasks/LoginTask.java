package com.catchme.contactlist.asynctasks;

import java.util.Currency;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.Toast;

import com.catchme.R;
import com.catchme.connections.ReadServerResponse;
import com.catchme.connections.ServerConnection;
import com.catchme.connections.ServerRequests;
import com.catchme.contactlist.DrawerMenuAdapter;
import com.catchme.contactlist.ItemListActivity;
import com.catchme.exampleObjects.ExampleContent;
import com.catchme.exampleObjects.ExampleContent.LoggedUser;

public class LoginTask extends AsyncTask<String, Void, JSONObject> {
	private Context context;
	private DrawerMenuAdapter drawerAdapter;
	private SwipeRefreshLayout swipeLayout;

	public LoginTask(DrawerMenuAdapter drawerAdapter,
			SwipeRefreshLayout swipeLayout) {
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
			setCurrentLoggedUser(ReadServerResponse.getLoggedUser(result), password);
		} else {
			result = null;
		}

		return result;
	}

	private void setCurrentLoggedUser(LoggedUser loggedUser, String password) {
		loggedUser.setPassword(password);
		ExampleContent.currentUser = loggedUser;
		
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
						"Success! Logged user: "
								+ ExampleContent.currentUser.getFullName(),
						Toast.LENGTH_SHORT).show();
				SharedPreferences preferences = context.getSharedPreferences(ItemListActivity.PREFERENCES, Context.MODE_PRIVATE);
				Editor e = preferences.edit();
				e.putString(ItemListActivity.USER_TOKEN, ExampleContent.currentUser.getToken());
				e.putString(ItemListActivity.USER_EMAIL, ExampleContent.currentUser.getEmail());
				e.putString(ItemListActivity.USER_PASSWORD, ExampleContent.currentUser.getPassword());
				e.commit();
				drawerAdapter.notifyDataSetChanged();
			} else {
				Toast.makeText(context,
						"Fail! " + ReadServerResponse.getErrors(result),
						Toast.LENGTH_SHORT).show();

			}
		}
		swipeLayout.setRefreshing(false);
	}
}
