package com.catchme.contactlist;

import java.util.ArrayList;

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

public class LoginFragment extends Fragment implements OnClickListener,
		OnTaskCompleted {
	private View rootView;

	public LoginFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// inflate layout from xml
		rootView = inflater.inflate(R.layout.fragment_login, container, false);

		// set behaviour after click
		Button b = (Button) rootView.findViewById(R.id.button1);
		b.setOnClickListener(this);
		return rootView;
	}

	@Override
	public void onClick(View v) {
		new LoginTask(getActivity(), this).execute("mailCzeslawa@cycki.pl",
				"appleseed");
		// TODO show animation or something else
	}

	@Override
	public void onTaskCompleted(ArrayList<String> errors) {
		//get storage
		SharedPreferences preferences = getActivity().getSharedPreferences(
				ItemListActivity.PREFERENCES, Context.MODE_PRIVATE);
		
		//if LoginTask was able to download user data
		if (preferences.contains(ItemListActivity.USER)) {
			//prepare new view
			ItemListFragment mainFragment = new ItemListFragment();
			//replace old view
			getActivity().getSupportFragmentManager().beginTransaction()
					.replace(R.id.main_fragment_container, mainFragment)
					.commit();
			//adjust actionBar
			getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
			getActivity().getActionBar().setHomeButtonEnabled(true);
		} else {
			//Here you can handle errors.
			//error messages returned by server are stored in errors
		}
	}
}
