package com.catchme.contactlist.listeners;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.catchme.R;
import com.catchme.contactlist.CustomListAdapter;
import com.catchme.contactlist.ItemListActivity;
import com.catchme.contactlist.LoginFragment;
import com.catchme.contactlist.asynctasks.GetContactsTask;
import com.catchme.exampleObjects.ExampleContent;

public class DrawerOnItemClickListener implements OnItemClickListener {
	private DrawerLayout drawerLayout;
	private ListView drawerList;
	private ListView listView;
	private SwipeRefreshLayout swipeLayout;
	private Context context;

	public DrawerOnItemClickListener(Context context,
			DrawerLayout drawerLayout, ListView drawerList, ListView listView,
			SwipeRefreshLayout swipeLayout) {
		super();
		this.context = context;
		this.drawerLayout = drawerLayout;
		this.drawerList = drawerList;
		this.listView = listView;
		this.swipeLayout = swipeLayout;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (position == 1) {
			new GetContactsTask(swipeLayout,
					(CustomListAdapter) listView.getAdapter())
					.execute(ExampleContent.currentUser.getToken());

		} else if (position == 5) {
			SharedPreferences preferences = context.getSharedPreferences(
					ItemListActivity.PREFERENCES, Context.MODE_PRIVATE);
			Editor e = preferences.edit();
			e.remove(ItemListActivity.USER_TOKEN);
			e.commit();
			ExampleContent.clear();
			LoginFragment loginFragment = new LoginFragment();
			((FragmentActivity) context).getSupportFragmentManager()
					.beginTransaction()
					.replace(R.id.main_fragment_container, loginFragment)
					.commit();
		}
		if (position != 0) {
			drawerLayout.closeDrawer(drawerList);
		}
	}

}
