package com.catchme.profile.asynctasks;

import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;

import com.catchme.connections.ReadServerResponse;
import com.catchme.connections.ServerConnection;
import com.catchme.connections.ServerRequests;

public class UpdateProfileTask extends AsyncTask<String, Long, JSONObject> {
	private Context context;
	private OnUpdateProfileListener listener;

	public UpdateProfileTask(Context context, OnUpdateProfileListener listener) {
		super();
		this.context = context;
		this.listener = listener;
	}

	@Override
	protected void onPreExecute() {
		listener.onPreUpdate();
	}

	@Override
	protected JSONObject doInBackground(String... params) {
		String token = params[0];
		String email = params[1];
		String password = null;
		String passwordConfirmation = null;
		if (params.length > 2) {
			if (params.length != 4) {
				throw new IllegalArgumentException(
						"Task needs token, email, password, confirmation password");
			}
			password = params[2];
			passwordConfirmation = params[3];
		}
		JSONObject result = null;
		if (ServerConnection.isOnline(context)) {
			result = ServerRequests.updateUserEmail(token, email, password,
					passwordConfirmation);
		}
		return result;
	}

	@Override
	protected void onPostExecute(JSONObject result) {
		if (result == null) {
			listener.onUpdateProfileError(null);
		} else if (ReadServerResponse.isSuccess(result)) {
			listener.onUpdateProfileCompleted();
		} else {
			listener.onUpdateProfileError(ReadServerResponse.getErrors(result));
		}
	}
}
