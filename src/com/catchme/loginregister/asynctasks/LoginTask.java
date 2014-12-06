package com.catchme.loginregister.asynctasks;

import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;

import com.catchme.connections.ReadServerResponse;
import com.catchme.connections.ServerConnection;
import com.catchme.connections.ServerRequests;
import com.catchme.contactlist.ItemListActivity;
import com.catchme.exampleObjects.LoggedUser;

public class LoginTask extends AsyncTask<String, Void, JSONObject> {
	private Context context;
	private LoginRegisterInterface listener;

	public LoginTask(Context context, LoginRegisterInterface listener) {
		super();
		this.context = context;
		this.listener = listener;
	}

	@Override
	protected void onPreExecute() {
		listener.onPreExecute();
	}

	@Override
	protected JSONObject doInBackground(String... params) {
		JSONObject result = new JSONObject();
		if (ServerConnection.isOnline(context)) {
			String login = params[0];
			String password = params[1];
			result = ServerRequests.getTokenRequest(login, password);
		} else {
			result = null;
		}

		return result;
	}

	@Override
	protected void onPostExecute(JSONObject result) {
		if (result == null) {
			listener.onError(null);
		} else {
			if (ReadServerResponse.isSuccess(result)) {
				LoggedUser user = ReadServerResponse.getLoggedUser(result);
				ItemListActivity.setLoggedUser(context, user);
				listener.onCompleted(user);
			} else {
				listener.onError(ReadServerResponse.getErrors(result));
			}
		}
	}
}
