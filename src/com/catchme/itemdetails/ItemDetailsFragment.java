package com.catchme.itemdetails;

import com.catchme.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout.LayoutParams;

public class ItemDetailsFragment extends Fragment implements OnClickListener,
		OnPageChangeListener {
	public static final String ARG_ITEM_ID = "contact_id";
	private ItemDetailsPagerAdapter pagerAdapter;
	private ViewPager viewPager;
	private View rootView;
	private Button btnMessagesTab;
	private Button btnProfileTab;
	private Button btnMapTab;
	private View tabUnderline;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		rootView = inflater.inflate(R.layout.fragment_item_details, container,
				false);

		long itemId = getArguments().getLong(ARG_ITEM_ID);
		pagerAdapter = new ItemDetailsPagerAdapter(getFragmentManager(), itemId);

		viewPager = (ViewPager) rootView.findViewById(R.id.item_pager);
		viewPager.setAdapter(pagerAdapter);

		btnMessagesTab = (Button) rootView
				.findViewById(R.id.detail_messages_button);
		btnProfileTab = (Button) rootView
				.findViewById(R.id.detail_profile_button);
		btnMapTab = (Button) rootView.findViewById(R.id.detail_map_button);

		tabUnderline = rootView.findViewById(R.id.detail_underline);

		btnMessagesTab.setOnClickListener(this);
		btnProfileTab.setOnClickListener(this);
		btnMapTab.setOnClickListener(this);
		viewPager.setOnPageChangeListener(this);
		viewPager.setOffscreenPageLimit(2);
		viewPager.setCurrentItem(0);
		setUnderlinePos(0,0);
		return rootView;
	}

	@Override
	public void onClick(View v) {
		if (v == btnMessagesTab) {
			viewPager.setCurrentItem(0);
		} else if (v == btnProfileTab) {
			viewPager.setCurrentItem(1);
		} else if (v == btnMapTab) {
			viewPager.setCurrentItem(2);
		}
	}

	private void setUnderlinePos(int position, float positionOffset) {
		LayoutParams params = (LayoutParams) tabUnderline.getLayoutParams();

		int tabCount = viewPager.getAdapter().getCount();
		int sum = btnProfileTab.getWidth() + btnMessagesTab.getWidth()
				+ btnMapTab.getWidth() + 2 * tabCount;
		params.leftMargin = (int) (position * sum / tabCount + positionOffset
				/ tabCount);
		if(sum <100){
			params.width = 180;//no idea why getWidth() returns 0
		}else{
			params.width = sum / tabCount;
		}
		tabUnderline.setLayoutParams(params);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		if (positionOffset == 0) {

		} else {
			setUnderlinePos(position, positionOffsetPixels);
		}
	}

	@Override
	public void onPageSelected(int arg0) {

	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setHasOptionsMenu(true);
	}
	
	@Override
	public void  onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
		inflater.inflate(R.menu.item_details, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_settings:
			// openSettings();
			return true;
		case R.id.action_overflow:
			// openSettings();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
