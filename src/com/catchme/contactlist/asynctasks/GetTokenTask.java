package com.catchme.contactlist.asynctasks;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.catchme.connections.ConnectionConst;
import com.catchme.connections.ServerConection;

import android.os.AsyncTask;

public class GetTokenTask extends AsyncTask<String, Void, String> {

	@Override
	protected String doInBackground(String... params) {
		String login = params[0];
		String password = params[1];
		HashMap<String, HashMap<String, String>> data = new HashMap<String, HashMap<String, String>>();
		HashMap<String, String> dataContent = new HashMap<String, String>();
		dataContent.put(ConnectionConst.USER_EMAIL, login);
		dataContent.put(ConnectionConst.USER_PASSWORD, password);
		data.put(ConnectionConst.USER, dataContent);
		String result = ServerConection.JsonPOST(
				ConnectionConst.URL_CONTACTS_ALL, data);
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO import token to database
		JSONObject json;
		try {
			json = new JSONObject(result);
			System.out.println(json.get(ConnectionConst.TOKEN));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
