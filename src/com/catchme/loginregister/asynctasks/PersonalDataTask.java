package com.catchme.loginregister.asynctasks;

import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;

import com.catchme.connections.ReadServerResponse;
import com.catchme.connections.ServerConnection;
import com.catchme.connections.ServerRequests;
import com.catchme.contactlist.ItemListActivity;
import com.catchme.database.model.LoggedUser;
import com.catchme.database.model.ExampleItem.UserSex;

public class PersonalDataTask extends AsyncTask<String, Void, JSONObject> {
	private Context context;
	private LoginRegisterInterface listener;
	private LoggedUser user;

	public PersonalDataTask(Context context, LoginRegisterInterface listener) {
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
			String token = params[0];
			String name = params[1];
			String surname = params[2];
			UserSex sex = UserSex.getSexByString(params[3]);
			String date = null;
			if(params.length>=3){
				date = params[4];
			}
			result = ServerRequests.addPersonalDataRequest(token, name, surname, sex, date);
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
				user = ReadServerResponse.getLoggedUser(result);
				ItemListActivity.setLoggedUser(context, user);
				listener.onCompleted(user);
			} else {
				listener.onError(ReadServerResponse.getErrors(result));
			}
		}
	}
}
