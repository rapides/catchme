package com.catchme.contactlist.listeners;

import android.support.v4.app.ActionBarDrawerToggle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.catchme.contactlist.ItemListFragment.Callbacks;
import com.catchme.model.*;

@SuppressWarnings("deprecation")
public class ItemListOnItemClickListener implements OnItemClickListener {

	ActionBarDrawerToggle drawerToggle;
	Callbacks mCallbacks;

	public ItemListOnItemClickListener(ActionBarDrawerToggle drawerToggle,
			Callbacks mCallbacks) {
		this.drawerToggle = drawerToggle;
		this.mCallbacks = mCallbacks;
	}

	@Override
	public void onItemClick(AdapterView<?> a, View v, int position, long id) {
		drawerToggle.setDrawerIndicatorEnabled(false);
		mCallbacks.onItemSelected(((ExampleItem) a.getItemAtPosition(position))
				.getId());

	}

}
