package com.catchme.contactlist.asynctasks;

import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.widget.Toast;

import com.catchme.R;
import com.catchme.connections.ReadServerResponse;
import com.catchme.connections.ServerConnection;
import com.catchme.connections.ServerRequests;
import com.catchme.contactlist.ItemListActivity;
import com.catchme.exampleObjects.ExampleContent;
import com.catchme.exampleObjects.ExampleContent.LoggedUser;

public class LoginTask extends AsyncTask<String, Void, JSONObject> {
	private Context context;
	private OnTaskCompleted listener;
	
	public LoginTask(Context context, OnTaskCompleted listener) {
		super();
		this.context = context;
		this.listener = listener;
	}

	@Override
	protected void onPreExecute() {
	}

	@Override
	protected JSONObject doInBackground(String... params) {
		JSONObject result = new JSONObject();
		if (ServerConnection.isOnline(context)) {
			String login = params[0];
			String password = params[1];
			result = ServerRequests.getTokenRequest(login, password);
			setCurrentLoggedUser(ReadServerResponse.getLoggedUser(result));
		} else {
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
			if (ReadServerResponse.isSuccess(result)) {
				Toast.makeText(
						context,
						"Success! Logged user: "
								+ ExampleContent.currentUser.getFullName(),
						Toast.LENGTH_SHORT).show();
				SharedPreferences preferences = context.getSharedPreferences(
						ItemListActivity.PREFERENCES, Context.MODE_PRIVATE);
				Editor e = preferences.edit();
				e.putString(ItemListActivity.USER_TOKEN,
						ExampleContent.currentUser.getToken());
				e.commit();

			} else {
				Toast.makeText(context,
						"Fail! " + ReadServerResponse.getErrors(result),
						Toast.LENGTH_SHORT).show();
			}
		}
		listener.onTaskCompleted();
	}
}
