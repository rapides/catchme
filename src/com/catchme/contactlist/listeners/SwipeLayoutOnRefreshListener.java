package com.catchme.contactlist.listeners;

import com.catchme.contactlist.CustomListAdapter;
import com.catchme.contactlist.asynctasks.GetContactsTask;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;

public class SwipeLayoutOnRefreshListener implements OnRefreshListener {
	SwipeRefreshLayout swipeLayout;
	CustomListAdapter adapter;

	public SwipeLayoutOnRefreshListener(SwipeRefreshLayout swipeLayout,
			CustomListAdapter adapter) {
		super();
		this.swipeLayout = swipeLayout;
		this.adapter = adapter;
	}

	@Override
	public void onRefresh() {
		new GetContactsTask(swipeLayout, adapter).execute("token");
	}

}
