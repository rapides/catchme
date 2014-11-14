package com.catchme.contactlist.listeners;

import com.catchme.contactlist.CustomListAdapter;
import com.catchme.contactlist.asynctasks.GetContactsTask;
import com.catchme.exampleObjects.ExampleContent;

import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class DrawerOnItemClickListener implements OnItemClickListener {
	private DrawerLayout drawerLayout;
	private ListView drawerList;
	private CustomListAdapter adapter;
	private SwipeRefreshLayout swipeLayout;

	public DrawerOnItemClickListener(DrawerLayout drawerLayout,
			ListView drawerList, CustomListAdapter adapter,
			SwipeRefreshLayout swipeLayout) {
		super();
		this.drawerLayout = drawerLayout;
		this.drawerList = drawerList;
		this.adapter = adapter;
		this.swipeLayout = swipeLayout;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (position > 1) {
			drawerLayout.closeDrawer(drawerList);
		} else if (position == 1) {
			drawerLayout.closeDrawer(drawerList);
			new GetContactsTask(swipeLayout, adapter)
					.execute(ExampleContent.currentUser.getToken());

		}
	}

}
