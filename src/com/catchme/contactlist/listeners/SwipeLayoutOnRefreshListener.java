package com.catchme.contactlist.listeners;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.widget.ListView;

import com.catchme.contactlist.CustomListAdapter;
import com.catchme.contactlist.ItemListActivity;
import com.catchme.contactlist.ItemListFragment;
import com.catchme.contactlist.asynctasks.GetContactsTask;
import com.catchme.exampleObjects.ExampleItem.ContactStateType;
import com.catchme.exampleObjects.LoggedUser;

;

public class SwipeLayoutOnRefreshListener implements OnRefreshListener {
	SwipeRefreshLayout swipeLayout;
	ListView listView;
	private Context context;

	public SwipeLayoutOnRefreshListener(SwipeRefreshLayout swipeLayout,
			ListView listView) {
		super();
		this.swipeLayout = swipeLayout;
		this.listView = listView;
		this.context = listView.getContext();
	}

	@Override
	public void onRefresh() {
		SharedPreferences prefs = context.getSharedPreferences(
				ItemListActivity.PREFERENCES, Context.MODE_PRIVATE);
		int val = prefs.getInt(ItemListFragment.SELECTED_FILTER, 1);
		new GetContactsTask(swipeLayout,
				(CustomListAdapter) listView.getAdapter(),
				ContactStateType.getStateType(val)).execute(ItemListActivity
				.getLoggedUser(context).getToken());
	}
}
