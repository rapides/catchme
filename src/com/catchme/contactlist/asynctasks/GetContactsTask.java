package com.catchme.contactlist.asynctasks;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

import com.catchme.connections.ConnectionConst;
import com.catchme.connections.ServerConection;

public class GetContactsTask extends AsyncTask<String, Void, String> {

	@Override
	protected String doInBackground(String... params) {
		String token = params[0];
		HashMap<String, HashMap<String, String>> data = new HashMap<String, HashMap<String, String>>();
		HashMap<String, String> dataContent = new HashMap<String, String>();
		dataContent.put(ConnectionConst.TOKEN, token);
		data.put(ConnectionConst.USER, dataContent);
		//TODO request structure
		String result = ServerConection.JsonPOST(ConnectionConst.URL_CONTACTS_ALL, data);
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		//TODO import contacts to database
		JSONObject json;
		try {
			json = new JSONObject(result);
			System.out
					.println("Popacz sam, jak gumisie skacz¹ tam i siam: "
							+ ((JSONObject) json.get("user")).get("email"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}