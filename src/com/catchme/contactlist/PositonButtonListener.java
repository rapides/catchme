package com.catchme.contactlist;

import com.catchme.R;
import com.catchme.mapcontent.ItemDetailFragment;
import com.catchme.messages.MessagesFragment;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;

public class PositonButtonListener implements OnClickListener {
	private long id;

	public PositonButtonListener(long contactId) {
		this.id = contactId;
	}

	@Override
	public void onClick(View v) {
		ViewPager viewPager = ((ViewPager) v.getRootView().findViewById(
				R.id.main_pager));
		viewPager.setCurrentItem(2);
		ItemListFragment.lastChoosedContactId = id;
		ItemDetailFragment mapFrag = (ItemDetailFragment) viewPager.getAdapter()
				.instantiateItem(viewPager, 2);
		mapFrag.updateView(id);
		MessagesFragment msgFrag = (MessagesFragment) viewPager
				.getAdapter().instantiateItem(viewPager, 0);
		msgFrag.updateView(id);
	}

}
