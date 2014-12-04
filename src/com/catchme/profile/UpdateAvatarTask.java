package com.catchme.profile;

import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.catchme.R;
import com.catchme.connections.ReadServerResponse;
import com.catchme.connections.ServerRequests;
import com.catchme.contactlist.ItemListActivity;
import com.catchme.exampleObjects.ExampleContent.LoggedUser;
import com.google.gson.Gson;

public class UpdateAvatarTask extends AsyncTask<String, Integer, JSONObject> {
	private Context context;
	ImageUploadingListener listener;

	public UpdateAvatarTask(Context context, ImageUploadingListener listener) {
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
		String imagePath = params[1];
		// publishProgress(3);
		return ServerRequests.uploadAvatar(token, imagePath);

	}

	@Override
	protected void onPostExecute(JSONObject result) {
		if (result == null) {
			Toast.makeText(context,
					context.getResources().getString(R.string.err_no_internet),
					Toast.LENGTH_SHORT).show();
		} else if (ReadServerResponse.isSuccess(result)) {
			SharedPreferences preferences = context.getSharedPreferences(
					ItemListActivity.PREFERENCES, Context.MODE_PRIVATE);
			Gson gson = new Gson();
			String json = preferences.getString(ItemListActivity.USER, "");
			LoggedUser user = gson.fromJson(json, LoggedUser.class);
			user.setAvatars(ReadServerResponse.updateAvatars(result));
			preferences.edit()
					.putString(ItemListActivity.USER, new Gson().toJson(user))
					.commit();

			listener.onImageUploaded();
		} else {
			listener.onImageUploadError(ReadServerResponse.getErrors(result));
		}
	}

	@Override
	protected void onProgressUpdate(Integer... progress) {
		// TODO Auto-generated method stub

	}
}
