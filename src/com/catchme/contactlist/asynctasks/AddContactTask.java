package com.catchme.contactlist.asynctasks;

import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;

import com.catchme.connections.ReadServerResponse;
import com.catchme.connections.ServerConnection;
import com.catchme.connections.ServerRequests;
import com.catchme.contactlist.interfaces.OnAddContactCompletedListener;

public class AddContactTask extends AsyncTask<String, Void, JSONObject> {
	private Context context;
	private OnAddContactCompletedListener listener;
	public AddContactTask(Context context, OnAddContactCompletedListener listener) {
		super();
		this.context = context;
		this.listener = listener;
	}

	@Override
	protected void onPreExecute() {
		listener.onPreAddContact();
	}

	@Override
	protected JSONObject doInBackground(String... params) {
		String token = params[0];
		String email = params[1];
		JSONObject result = null;
		if (ServerConnection.isOnline(context)) {
			result = ServerRequests.addContactRequest(token, email);
		}
		return result;
	}

	@Override
	protected void onPostExecute(JSONObject result) {
		if (result == null) {
			listener.onAddContactError(null);
		} else if (ReadServerResponse.isSuccess(result)) {
			listener.onAddContactSucceded();
		} else {
			listener.onAddContactError(ReadServerResponse.getErrors(result));
		}
	}

}
