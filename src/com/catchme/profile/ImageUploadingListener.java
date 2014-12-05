package com.catchme.profile;

import java.util.HashMap;

public interface ImageUploadingListener {
	public void onPreUpdate();
	public void onProgressUpdate(int value);
	public void onImageUploaded();
	public void onImageUploadError(HashMap<Integer, String> errors);
}
