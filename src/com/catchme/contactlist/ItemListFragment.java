package com.catchme.contactlist;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import com.catchme.R;
import com.catchme.contactlist.asynctasks.GetContactsTask;
import com.catchme.contactlist.listeners.DrawerOnItemClickListener;
import com.catchme.contactlist.listeners.FloatingActionButtonListener;
import com.catchme.contactlist.listeners.ItemListOnItemClickListener;
import com.catchme.contactlist.listeners.SwipeLayoutOnRefreshListener;
import com.catchme.exampleObjects.ExampleContent.ExampleItem;
import com.catchme.exampleObjects.ExampleContent.ExampleItem.ContactStateType;
import com.catchme.exampleObjects.ExampleContent.LoggedUser;
import com.catchme.utils.FloatingActionButton;
import com.google.gson.Gson;

@SuppressWarnings("deprecation")
public class ItemListFragment extends Fragment implements OnQueryTextListener,
		OnMenuItemClickListener {

	private static final String STATE_ACTIVATED_POSITION = "activated_position";
	public static final String SELECTED_FILTER = "filter";
	public ListView listView;
	private SearchView searchView;
	private SwipeRefreshLayout swipeLayout;
	private DrawerLayout drawerLayout;
	private ListView drawerList;
	private ActionBarDrawerToggle drawerToggle;
	private SharedPreferences sharedpreferences;
	private FloatingActionButton fab;
	private LoggedUser user;
	/**
	 * The fragment's current callback object, which is notified of list item
	 * clicks.
	 */
	private Callbacks mCallbacks = sDummyCallbacks;

	/**
	 * The current activated item position. Only used on tablets.
	 */
	private int mActivatedPosition = ListView.INVALID_POSITION;
	private PopupMenu popup;

	public interface Callbacks {
		public void onItemSelected(long id);
	}

	private static Callbacks sDummyCallbacks = new Callbacks() {
		@Override
		public void onItemSelected(long id) {
		}
	};

	public ItemListFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_item_list,
				container, false);
		sharedpreferences = getActivity().getSharedPreferences(
				ItemListActivity.PREFERENCES, Context.MODE_PRIVATE);
		String json = sharedpreferences.getString(ItemListActivity.USER, "");
		user = new Gson().fromJson(json, LoggedUser.class);

		listView = (ListView) rootView.findViewById(R.id.list_item);

		swipeLayout = (SwipeRefreshLayout) rootView
				.findViewById(R.id.swipe_container);
		drawerLayout = (DrawerLayout) rootView.findViewById(R.id.drawer_layout);
		drawerList = (ListView) rootView.findViewById(R.id.left_drawer);
		fab = (FloatingActionButton) rootView
				.findViewById(R.id.list_floating_action_button);

		listView.setAdapter(new CustomListAdapter(getActivity(),
				new ArrayList<ExampleItem>()));

		swipeLayout.setOnRefreshListener(new SwipeLayoutOnRefreshListener(
				swipeLayout, listView, user));
		swipeLayout.setColorSchemeResources(R.color.swipelayout_bar,
				R.color.swipelayout_color1, R.color.swipelayout_color2,
				R.color.swipelayout_color3);

		drawerList.setAdapter(new DrawerMenuAdapter(getActivity(), user));
		drawerList.setOnItemClickListener(new DrawerOnItemClickListener(
				getActivity(), drawerLayout, drawerList, listView, swipeLayout,
				user));

		((DrawerMenuAdapter) drawerList.getAdapter()).notifyDataSetChanged();
		drawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				((DrawerMenuAdapter) drawerList.getAdapter())
						.notifyDataSetChanged();
			}

		};
		drawerLayout.setDrawerListener(drawerToggle);
		drawerToggle.setDrawerIndicatorEnabled(true);
		listView.setOnItemClickListener(new ItemListOnItemClickListener(
				drawerToggle, mCallbacks));
		fab.setOnClickListener(new FloatingActionButtonListener(getActivity(),
				swipeLayout, user));

		// filterList(sharedpreferences.getInt(SELECTED_FILTER, 0) - 1);

		new GetContactsTask(swipeLayout,
				(CustomListAdapter) listView.getAdapter(), ContactStateType.ACCEPTED).execute(user
				.getToken());
		return rootView;
	}

	private void setListItemChecked(int position) {
		boolean isChecked = listView.getCheckedItemPositions().get(
				listView.getCheckedItemPositions().keyAt(position));
		listView.setItemChecked(position, !isChecked);
		// View itemView = getViewByPosition(position);

		/*
		 * if (isChecked) {
		 * itemView.setBackgroundColor(getActivity().getResources().getColor(
		 * R.color.list_item_background)); } else {
		 * itemView.setBackgroundColor(getActivity().getResources().getColor(
		 * R.color.list_item_selected)); }
		 */
		String out = "";
		for (int i = 0; i < listView.getCheckedItemPositions().size(); i++) {
			out += ""
					+ listView.getCheckedItemPositions().keyAt(i)
					+ ":"
					+ listView.getCheckedItemPositions().get(
							listView.getCheckedItemPositions().keyAt(i)) + ", ";
		}
		System.out.println(out);
	}

	private View getViewByPosition(int pos) {
		final int firstListItemPosition = listView.getFirstVisiblePosition();
		final int lastListItemPosition = firstListItemPosition
				+ listView.getChildCount() - 1;

		if (pos < firstListItemPosition || pos > lastListItemPosition) {
			return listView.getAdapter().getView(pos, null, listView);
		} else {
			final int childIndex = pos - firstListItemPosition;
			return listView.getChildAt(childIndex);
		}
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		// Restore the previously serialized activated item position.
		if (savedInstanceState != null
				&& savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
			setActivatedPosition(savedInstanceState
					.getInt(STATE_ACTIVATED_POSITION));
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		drawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		drawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}
		mCallbacks = (Callbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = sDummyCallbacks;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mActivatedPosition != AdapterView.INVALID_POSITION) {
			// Serialize and persist the activated item position.
			outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
		}
	}

	/**
	 * Turns on activate-on-click mode. When this mode is on, list items will be
	 * given the 'activated' state when touched.
	 */
	public void setActivateOnItemClick(boolean activateOnItemClick) {
		// When setting CHOICE_MODE_SINGLE, ListView will automatically
		// give items the 'activated' state when touched.
		getListView().setChoiceMode(
				activateOnItemClick ? AbsListView.CHOICE_MODE_SINGLE
						: AbsListView.CHOICE_MODE_NONE);
	}

	private void setActivatedPosition(int position) {
		if (position == AdapterView.INVALID_POSITION) {
			getListView().setItemChecked(mActivatedPosition, false);
		} else {
			getListView().setItemChecked(position, true);
		}

		mActivatedPosition = position;
	}

	private ListView getListView() {
		return listView;
	}

	/*
	 * private void filterList(int state) { if (listView != null &&
	 * listView.getAdapter() != null) { if (state >= 0) { ((CustomListAdapter)
	 * listView.getAdapter()).getFilter().filter(
	 * CustomListAdapter.SEARCHTYPES[0] + CustomListAdapter.SEARCHCHAR + state);
	 * } else { ((CustomListAdapter) listView.getAdapter()).getFilter().filter(
	 * null); } } }
	 */

	private void filterList(String newText) {
		if (listView != null && listView.getAdapter() != null) {
			((CustomListAdapter) listView.getAdapter()).getFilter().filter(
					newText);
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
		inflater.inflate(R.menu.main, menu);

		MenuItem searchItem = menu.findItem(R.id.action_search);
		searchView = (SearchView) searchItem.getActionView();
		setupSearchView(searchItem);

		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.action_filter:
			openSortMenu();
			return true;
		case android.R.id.home: {
			if (drawerLayout.isDrawerVisible(drawerList)) {
				drawerLayout.closeDrawer(drawerList);
			} else if (this.isVisible()) {
				drawerLayout.openDrawer(drawerList);
			}
			return true;
		}
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	private void openSortMenu() {
		if (popup == null) {
			popup = new PopupMenu(getActivity(), getActivity().findViewById(
					R.id.action_filter));
			popup.getMenuInflater().inflate(R.menu.menu_sort, popup.getMenu());
			popup.setOnMenuItemClickListener(this);
			popup.getMenu().getItem(1).setChecked(true);
		}
		else{
			popup.getMenu().getItem(sharedpreferences.getInt(SELECTED_FILTER, 1)+1)
			.setChecked(true);//+1 because model doesn't handle ALL filter
		}
		
		popup.show();
	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		Editor editor = sharedpreferences.edit();
		switch (item.getItemId()) {
		case R.id.menu_group_all:
			new GetContactsTask(swipeLayout,
					(CustomListAdapter) listView.getAdapter(),
					ContactStateType.ACCEPTED).execute(user.getToken());
			item.setChecked(!item.isChecked());
			editor.putInt(SELECTED_FILTER, ContactStateType.ACCEPTED.getIntegerValue());
			editor.commit();
			return true;
		case R.id.menu_group_accepted:
			new GetContactsTask(swipeLayout,
					(CustomListAdapter) listView.getAdapter(),
					ContactStateType.ACCEPTED).execute(user.getToken());
			item.setChecked(!item.isChecked());
			editor.putInt(SELECTED_FILTER, ContactStateType.ACCEPTED.getIntegerValue());
			editor.commit();
			return true;
		case R.id.menu_group_sent:
			new GetContactsTask(swipeLayout,
					(CustomListAdapter) listView.getAdapter(),
					ContactStateType.SENT).execute(user.getToken());
			item.setChecked(!item.isChecked());
			editor.putInt(SELECTED_FILTER, ContactStateType.SENT.getIntegerValue());
			editor.commit();
			return true;
		case R.id.menu_group_received:
			new GetContactsTask(swipeLayout,
					(CustomListAdapter) listView.getAdapter(),
					ContactStateType.RECEIVED).execute(user.getToken());
			item.setChecked(!item.isChecked());
			editor.putInt(SELECTED_FILTER, ContactStateType.RECEIVED.getIntegerValue());
			editor.commit();
			return true;
		}
		return false;
	}

	/*
	 * private void openOverflowMenu() { PopupMenu popup = new
	 * PopupMenu(getActivity(), getActivity()
	 * .findViewById(R.id.action_overflow)); MenuInflater inflater =
	 * popup.getMenuInflater(); inflater.inflate(R.menu.menu_overflow,
	 * popup.getMenu()); popup.setOnMenuItemClickListener(this); popup.show(); }
	 */

	private void setupSearchView(MenuItem searchItem) {

		if (isAlwaysExpanded()) {
			searchView.setIconifiedByDefault(false);
		} else {
			searchItem.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM
					| MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
		}
		searchView.setOnQueryTextListener(this);
	}

	protected boolean isAlwaysExpanded() {
		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		filterList(newText);
		if (sharedpreferences != null) {
			Editor e = sharedpreferences.edit();
			e.putInt(SELECTED_FILTER, ContactStateType.ACCEPTED.getIntegerValue());
			e.commit();
		}
		return true;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		return false;
	}

}
