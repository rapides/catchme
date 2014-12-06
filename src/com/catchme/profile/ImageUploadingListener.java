package com.catchme.profile;

import android.support.v4.util.LongSparseArray;

public interface ImageUploadingListener {
	public void onPreUpdate();

	public void onProgressUpdate(long progress);

	public void onImageUploaded();

	public void onImageUploadError(LongSparseArray<String> errors);
}
