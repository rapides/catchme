package com.catchme.contactlist.listeners;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;

import com.catchme.contactlist.NoticeDialogFragment;
import com.catchme.contactlist.interfaces.OnAddContactCompletedListener;

public class FloatingActionButtonListener implements OnClickListener {
	FragmentActivity activity;
	OnAddContactCompletedListener listener;

	public FloatingActionButtonListener(FragmentActivity activity,
			OnAddContactCompletedListener listener) {
		this.activity = activity;
		this.listener = listener;
	}

	@Override
	public void onClick(View v) {
		showAddContactDialog();

	}

	public void showAddContactDialog() {
		DialogFragment dialog = new NoticeDialogFragment(
				activity.getApplicationContext(), listener);
		dialog.show(activity.getSupportFragmentManager(),
				"NoticeDialogFragment");
	}
}
