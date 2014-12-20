package com.catchme.itemdetails;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.catchme.database.CatchmeDatabaseAdapter;
import com.catchme.mapcontent.ItemMapFragment;
import com.catchme.messages.MessagesFragment;
import com.catchme.profile.ItemProfileFragment;

public class ItemDetailsPagerAdapter extends FragmentStatePagerAdapter {
	private long contactId;
	private CatchmeDatabaseAdapter dbAdapter;

	public ItemDetailsPagerAdapter(FragmentManager fm,
			CatchmeDatabaseAdapter dbAdapter, long contactId) {
		super(fm);
		this.contactId = contactId;
		this.dbAdapter = dbAdapter;
	}

	@Override
	public Fragment getItem(int i) {
		Fragment fragment = null;
		Bundle arguments = new Bundle();
		if (i == 0) {
			fragment = new MessagesFragment(dbAdapter);
		} else if (i == 1) {
			fragment = new ItemProfileFragment(dbAdapter);
		} else if (i == 2) {
			fragment = new ItemMapFragment(dbAdapter);
		}
		arguments.putLong(ItemDetailsFragment.ARG_ITEM_ID, contactId);
		fragment.setArguments(arguments);
		return fragment;
	}

	@Override
	public int getCount() {
		return 3;
	}
}
