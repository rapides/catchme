package com.catchme.contactlist.listeners;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.View.OnClickListener;

import com.catchme.contactlist.NoticeDialogFragment;
import com.catchme.exampleObjects.*;

public class FloatingActionButtonListener implements OnClickListener {
	FragmentActivity activity;
	private SwipeRefreshLayout swipeLayout;
	private LoggedUser user;

	public FloatingActionButtonListener(FragmentActivity activity,
			SwipeRefreshLayout swipeLayout, LoggedUser user) {
		this.activity = activity;
		this.swipeLayout = swipeLayout;
		this.user  =user;
	}

	@Override
	public void onClick(View v) {
		showAddContactDialog();

	}

	public void showAddContactDialog() {
		DialogFragment dialog = new NoticeDialogFragment(swipeLayout, user);
		dialog.show(activity.getSupportFragmentManager(),
				"NoticeDialogFragment");
	}
}
