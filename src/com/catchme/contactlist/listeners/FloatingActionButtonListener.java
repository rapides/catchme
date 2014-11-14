package com.catchme.contactlist.listeners;

import com.catchme.contactlist.NoticeDialogFragment;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.View.OnClickListener;

public class FloatingActionButtonListener implements OnClickListener {
	FragmentActivity activity;
	private SwipeRefreshLayout swipeLayout;

	public FloatingActionButtonListener(FragmentActivity activity,
			SwipeRefreshLayout swipeLayout) {
		this.activity = activity;
		this.swipeLayout = swipeLayout;
	}

	@Override
	public void onClick(View v) {
		showAddContactDialog();

	}

	public void showAddContactDialog() {
		DialogFragment dialog = new NoticeDialogFragment(swipeLayout);
		dialog.show(activity.getSupportFragmentManager(),
				"NoticeDialogFragment");
	}
}
