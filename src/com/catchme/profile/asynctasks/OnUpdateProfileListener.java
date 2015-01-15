package com.catchme.profile.asynctasks;

import android.support.v4.util.LongSparseArray;

public interface OnUpdateProfileListener {
	public void onPreUpdate();
	public void onUpdateProfileCompleted();
	public void onUpdateProfileError(LongSparseArray<String> errors);
}
