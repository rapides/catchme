package com.catchme.contactlist;

import android.app.ActionBar;
import android.support.v4.view.ViewPager.OnPageChangeListener;

public class ViewPagerListener implements OnPageChangeListener {
	ActionBar actionBar;
	public ViewPagerListener(ActionBar actionBar) {
		this.actionBar = actionBar;
	}

	@Override
	public void onPageScrollStateChanged(int state) {
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		
	}

	@Override
	public void onPageSelected(int n) {
		actionBar.setSelectedNavigationItem(n);
	}

}
