package com.catchme.loginregister;

import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.catchme.R;
import com.catchme.connections.ReadServerResponse;
import com.catchme.connections.ServerConnection;
import com.catchme.connections.ServerRequests;
import com.catchme.contactlist.ItemListActivity;
import com.catchme.exampleObjects.LoggedUser;

public class LoginTask extends AsyncTask<String, Void, JSONObject> {
	private Context context;
	private OnTaskCompleted listener;
	private LoggedUser user;
	private ProgressBar login_loading;

	public LoginTask(Context context, OnTaskCompleted listener,
			ProgressBar login_loading) {
		super();
		this.context = context;
		this.login_loading = login_loading;
		this.listener = listener;
	}

	@Override
	protected void onPreExecute() {
		login_loading.setVisibility(View.VISIBLE);
		// animaton.setVisible(VIw.Visible);
	}

	@Override
	protected JSONObject doInBackground(String... params) {
		JSONObject result = new JSONObject();
		if (ServerConnection.isOnline(context)) {
			String login = params[0];
			String password = params[1];
			result = ServerRequests.getTokenRequest(login, password);
			user = ReadServerResponse.getLoggedUser(result);
		} else {
			result = null;
		}

		return result;
	}

	@Override
	protected void onPostExecute(JSONObject result) {
		login_loading.setVisibility(View.GONE);
		if (result == null) {
			Toast.makeText(context,
					context.getResources().getString(R.string.err_no_internet),
					Toast.LENGTH_SHORT).show();
		} else {
			if (ReadServerResponse.isSuccess(result)) {
				Toast.makeText(context,
						"Success! Logged user: " + user.getFullName(),
						Toast.LENGTH_SHORT).show();
				ItemListActivity.setLoggedUser(context, user);
				listener.onTaskCompleted(null);
			} else {
				/*
				 * Toast.makeText(context, "Fail! " +
				 * ReadServerResponse.getErrors(result),
				 * Toast.LENGTH_SHORT).show();
				 */
				listener.onTaskCompleted(ReadServerResponse.getErrors(result));
			}
		}
	}
}
