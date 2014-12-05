package com.catchme.profile;

import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.catchme.R;
import com.catchme.connections.ReadServerResponse;
import com.catchme.connections.ServerConnection;
import com.catchme.connections.ServerRequests;
import com.catchme.contactlist.ItemListActivity;
import com.catchme.exampleObjects.LoggedUser;

public class UpdateAvatarTask extends AsyncTask<String, Long, JSONObject> {
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

		JSONObject result = null;
		if (ServerConnection.isOnline(context)) {
			result = ServerRequests.uploadAvatar(token, imagePath);
		} 
		
		return result;

	}

	@Override
	protected void onPostExecute(JSONObject result) {
		if (result == null) {
			Toast.makeText(context,
					context.getResources().getString(R.string.err_no_internet),
					Toast.LENGTH_SHORT).show();
		} else if (ReadServerResponse.isSuccess(result)) {
			
			LoggedUser user = ItemListActivity.getLoggedUser(context);
			user.setAvatars(ReadServerResponse.updateAvatars(result));
			ItemListActivity.setLoggedUser(context, user);

			listener.onImageUploaded();
		} else {
			listener.onImageUploadError(ReadServerResponse.getErrors(result));
		}
	}

	@Override
	protected void onProgressUpdate(Long... progress) {
		listener.onProgressUpdate(progress[0]);
	}

	
}
