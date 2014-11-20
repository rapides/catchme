package com.catchme.contactlist;

import com.catchme.R;
import com.catchme.contactlist.asynctasks.LoginTask;
import com.catchme.contactlist.asynctasks.OnTaskCompleted;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class LoginFragment extends Fragment implements OnClickListener, OnTaskCompleted {
	private View rootView;

	

	public LoginFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_login, container, false);
		Button b = (Button) rootView.findViewById(R.id.button1);
		b.setOnClickListener(this);
		return rootView;
	}

	@Override
	public void onClick(View v) {
		LoginTask task = new LoginTask(getActivity(), this);
		task.execute("mailCzeslawa@cycki.pl", "appleseed");
		// show animation
	}

	@Override
	public void onTaskCompleted() {
		SharedPreferences preferences = getActivity().getSharedPreferences(
				ItemListActivity.PREFERENCES, Context.MODE_PRIVATE);
		if (preferences.contains(ItemListActivity.USER_TOKEN)) {
			ItemListFragment mainFragment = new ItemListFragment();
			getActivity().getSupportFragmentManager().beginTransaction()
					.replace(R.id.main_fragment_container, mainFragment)
					.commit();
		}
	}
}
