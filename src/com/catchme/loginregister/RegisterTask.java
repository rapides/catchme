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

public class RegisterTask extends AsyncTask<String, Void, JSONObject> {
	private Context context;
	private OnTaskCompleted listener;
	private LoggedUser user;
	private ProgressBar register_loading;

	public RegisterTask(Context context, OnTaskCompleted listener,
			ProgressBar register_loading) {
		super();
		this.context = context;
		this.listener = listener;
		this.register_loading = register_loading;
	}

	@Override
	protected void onPreExecute() {
		register_loading.setVisibility(View.VISIBLE);
	}

	@Override
	protected JSONObject doInBackground(String... params) {
		JSONObject result = new JSONObject();
		if (ServerConnection.isOnline(context)) {
			String name = params[0];
			String surname = params[1];
			String email = params[2];
			String password = params[3];
			String confirmationPassword = params[4];
			result = ServerRequests.addUserRequest(name, surname, email,
					password, confirmationPassword);
			user = ReadServerResponse.getLoggedUser(result);
		} else {
			result = null;
		}

		return result;
	}

	@Override
	protected void onPostExecute(JSONObject result) {
		register_loading.setVisibility(View.GONE);
		if (result == null) {
			Toast.makeText(context,
					context.getResources().getString(R.string.err_no_internet),
					Toast.LENGTH_SHORT).show();
		} else {
			if (ReadServerResponse.isSuccess(result)) {
				Toast.makeText(context,
						"Success! Registered user: " + user.getFullName(),
						Toast.LENGTH_SHORT).show();
				ItemListActivity.setLoggedUser(context, user);
				listener.onTaskCompleted(null);
			} else {/*
					 * Toast.makeText(context, "Register fail! " +
					 * ReadServerResponse.getErrors(result),
					 * Toast.LENGTH_SHORT).show();
					 */
				listener.onTaskCompleted(ReadServerResponse.getErrors(result));
			}
		}
	}
}
