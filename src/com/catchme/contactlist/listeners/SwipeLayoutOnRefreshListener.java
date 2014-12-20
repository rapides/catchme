package com.catchme.contactlist.listeners;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;

import com.catchme.contactlist.ItemListActivity;
import com.catchme.contactlist.ItemListFragment;
import com.catchme.contactlist.asynctasks.GetContactsTask;
import com.catchme.contactlist.interfaces.OnGetContactCompletedListener;
import com.catchme.database.CatchmeDatabaseAdapter;
import com.catchme.model.ExampleItem.ContactStateType;

public class SwipeLayoutOnRefreshListener implements OnRefreshListener {
	private OnGetContactCompletedListener listener;
	private Context context;
	private CatchmeDatabaseAdapter dbAdapter;

	public SwipeLayoutOnRefreshListener(Context context,
			OnGetContactCompletedListener listener,
			CatchmeDatabaseAdapter dbAdapter) {
		super();
		this.context = context;
		this.listener = listener;
		this.dbAdapter = dbAdapter;
	}

	@Override
	public void onRefresh() {
		SharedPreferences prefs = context.getSharedPreferences(
				ItemListActivity.PREFERENCES, Context.MODE_PRIVATE);
		int val = prefs.getInt(ItemListFragment.SELECTED_FILTER, 1);
		new GetContactsTask(context, listener, dbAdapter,
				ContactStateType.getStateType(val)).execute(ItemListActivity
				.getLoggedUser(context).getToken());
	}
}
