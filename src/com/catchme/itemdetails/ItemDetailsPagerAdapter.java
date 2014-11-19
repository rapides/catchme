package com.catchme.itemdetails;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.catchme.mapcontent.ItemMapFragment;
import com.catchme.messages.MessagesFragment;
import com.catchme.profile.ItemProfileFragment;


public class ItemDetailsPagerAdapter extends FragmentStatePagerAdapter{
	long contactId;
	   public ItemDetailsPagerAdapter(FragmentManager fm, long contactId) {
	        super(fm);
	        this.contactId = contactId;
	    }

	@Override
	public Fragment getItem(int i) {
		Fragment fragment = null;
		Bundle arguments = new Bundle();
    	if(i ==0){
    		fragment = new MessagesFragment();
    	}else if(i==1){
    		fragment = new ItemProfileFragment();
    	}else if(i==2){
    		fragment = new ItemMapFragment();
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
