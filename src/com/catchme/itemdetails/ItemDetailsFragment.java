package com.catchme.itemdetails;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import com.catchme.R;
import com.catchme.utils.GifMovieView;

public class ItemDetailsFragment extends Fragment implements OnClickListener,
		OnPageChangeListener, OnMenuItemClickListener {
	public static final String ARG_ITEM_ID = "contact_id";
	private ItemDetailsPagerAdapter pagerAdapter;
	private ViewPager viewPager;
	private View rootView;
	private Button btnMessagesTab;
	private Button btnProfileTab;
	private Button btnMapTab;
	private View tabUnderline;
	private GifMovieView loader;
	private RelativeLayout loaderContainer;

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
		loader = (GifMovieView) rootView.findViewById(R.id.gif_loader);
		loaderContainer = (RelativeLayout) rootView
				.findViewById(R.id.loader_container);

		btnMessagesTab.setOnClickListener(this);
		btnProfileTab.setOnClickListener(this);
		btnMapTab.setOnClickListener(this);
		viewPager.setOnPageChangeListener(this);
		viewPager.setOffscreenPageLimit(2);
		viewPager.setCurrentItem(0);
		setUnderlinePos(0, 0);
		loader.setMovieResource(R.drawable.loader);
		loader.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		new LoadDetailsTask(loader, loaderContainer).execute();
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
		if (sum < 100) {
			params.width = 180;// no idea why getWidth() returns 0
		} else {
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
	public void onPageSelected(int position) {
		if(position==1){
			Toast.makeText(getActivity(), "selected tab 1", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
		inflater.inflate(R.menu.item_details, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			getActivity().dispatchKeyEvent(
					new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
			getActivity().onBackPressed();
			return true;
		case R.id.details_action_overflow:
			openOverflowMenu();
			return true;
		case R.id.details_action_refresh:
			new LoadDetailsTask(loader, loaderContainer).execute();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void openOverflowMenu() {
		PopupMenu popup = new PopupMenu(getActivity(), getActivity()
				.findViewById(R.id.details_action_overflow));
		MenuInflater inflater = popup.getMenuInflater();
		inflater.inflate(R.menu.menu_overflow, popup.getMenu());
		popup.setOnMenuItemClickListener(this);
		popup.show();
	}
	
	@Override
	public boolean onMenuItemClick(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_overflow_item1:
			Toast.makeText(getActivity(), "Menu 1", Toast.LENGTH_SHORT).show();
			return true;
		case R.id.menu_overflow_item2:
			Toast.makeText(getActivity(), "Menu 2", Toast.LENGTH_SHORT).show();
			return true;
		case R.id.menu_overflow_item3:
			Toast.makeText(getActivity(), "Menu 3", Toast.LENGTH_SHORT).show();
			return true;
		case R.id.menu_overflow_item4:
			Toast.makeText(getActivity(), "Menu 4", Toast.LENGTH_SHORT).show();
			return true;
		}
		return false;
	}

	

}
