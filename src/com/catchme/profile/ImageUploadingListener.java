package com.catchme.profile;

import java.util.ArrayList;

public interface ImageUploadingListener {
	public void onPreUpdate();
	public void onProgressUpdate(int value);
	public void onImageUploaded();
	public void onImageUploadError(ArrayList<String> errors);
}
