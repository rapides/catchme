package com.catchme.profile;

import java.util.HashMap;

public interface ImageUploadingListener {
	public void onPreUpdate();
	public void onProgressUpdate(long progress);
	public void onImageUploaded();
	public void onImageUploadError(HashMap<Integer, String> errors);
}
