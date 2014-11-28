package com.catchme.loginregister;

import java.util.ArrayList;

import com.catchme.R;
import com.catchme.contactlist.ItemListActivity;
import com.catchme.contactlist.ItemListFragment;
import com.catchme.itemdetails.ItemDetailsFragment;
import com.catchme.profile.ItemProfileFragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

		// set behavior after click
		Button login_btn = (Button) rootView.findViewById(R.id.login);
		Button register_btn = (Button) rootView.findViewById(R.id.goto_register);
		register_btn.setOnClickListener(this);
		login_btn.setOnClickListener(this);
		return rootView;
	}

	@Override
	public void onClick(View v) {
		EditText login = (EditText) rootView.findViewById(R.id.login_email);
		if (v.getId() == R.id.login) {
			EditText pass = (EditText) rootView.findViewById(R.id.login_pass);

			// Toast.makeText(rootView.getContext(), login.getText().toString()+
			// " ---- "+pass.getText().toString(), Toast.LENGTH_LONG).show();
			new LoginTask(getActivity(), this).execute(login.getText()
					.toString(), pass.getText().toString());
		}
		if (v.getId() == R.id.goto_register) {
			// replace old view
			Bundle arguments = new Bundle();
			arguments.putString(RegisterFragment.EMAIL, login.getText()
					.toString());
			RegisterFragment regFragment = new RegisterFragment();
			regFragment.setArguments(arguments);
			FragmentTransaction transaction = getActivity()
					.getSupportFragmentManager().beginTransaction();
			transaction.setCustomAnimations(android.R.anim.fade_in,
					android.R.anim.fade_out);
			transaction.replace(R.id.main_fragment_container, regFragment);
			transaction.addToBackStack(null);
			transaction.commit();

			// adjust actionBar
		}

		// new LoginTask(getActivity(), this).execute("mailCzeslawa@cycki.pl",
		// "appleseed");
		// TODO show animation or something else
	}

	@Override
	public void onTaskCompleted(ArrayList<String> errors) {
		// get storage
		SharedPreferences preferences = getActivity().getSharedPreferences(
				ItemListActivity.PREFERENCES, Context.MODE_PRIVATE);

		// if LoginTask was able to download user data
		if (preferences.contains(ItemListActivity.USER)) {
			// prepare new view
			ItemListFragment mainFragment = new ItemListFragment();
			// replace old view
			getActivity().getSupportFragmentManager().beginTransaction()
					.replace(R.id.main_fragment_container, mainFragment)
					.commit();
			// adjust actionBar
			getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
			getActivity().getActionBar().setHomeButtonEnabled(true);
		} else {
			if (errors == null ) {
				// not known error
			} else {
				String errorsSring = "";
				for(int i=0;i<errors.size();i++){
					errorsSring += errors.get(i)+"\n";
				}
				Toast.makeText(getActivity(), errorsSring, Toast.LENGTH_SHORT)
				.show();
			}
			// Here you can handle errors.
			// error messages returned by server are stored in errors
		}
	}
}
