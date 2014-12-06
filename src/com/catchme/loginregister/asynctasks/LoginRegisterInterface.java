package com.catchme.loginregister.asynctasks;

import com.catchme.exampleObjects.LoggedUser;

import android.support.v4.util.LongSparseArray;

public interface LoginRegisterInterface {
	void onPreExecute();
	void onCompleted(LoggedUser user);
	void onError(LongSparseArray<String> errors);
}