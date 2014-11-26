package com.catchme.loginregister;

import java.util.ArrayList;

import com.catchme.R;
import com.catchme.contactlist.ItemListActivity;
import com.catchme.contactlist.ItemListFragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterFragment extends Fragment implements OnClickListener,
	OnTaskCompleted{
	private View rootView;

	public RegisterFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// inflate layout from xml
		rootView = inflater.inflate(R.layout.fragment_register, container, false);

		// set behavior after click
		Button reg = (Button) rootView.findViewById(R.id.register);
		reg.setOnClickListener(this);
		return rootView;
	}

	@Override
	public void onClick(View v) {
		
		EditText name = (EditText) rootView.findViewById(R.id.reg_imie);
		EditText surname = (EditText) rootView.findViewById(R.id.reg_naz);
		EditText email = (EditText) rootView.findViewById(R.id.reg_email);
		EditText pass = (EditText) rootView.findViewById(R.id.reg_pass);
		EditText conf_pass = (EditText) rootView.findViewById(R.id.reg_pass_conf);
		
		Toast.makeText(rootView.getContext(), name.toString()+ " ---- "+pass.toString()+
				" ---- "+surname.toString() + " ---"+email.toString()+"--p2--"+conf_pass.toString(), Toast.LENGTH_LONG).show();
		new RegisterTask(getActivity(), this).execute(name.toString(),surname.toString(),email.toString(),pass.toString(),conf_pass.toString());
		//new LoginTask(getActivity(), this).execute("mailCzeslawa@cycki.pl",
		//		"appleseed");
		// TODO show animation or something else
	}

	@Override
	public void onTaskCompleted(ArrayList<String> errors) {
		//get storage
		//SharedPreferences preferences = getActivity().getSharedPreferences(
			//	ItemListActivity.PREFERENCES, Context.MODE_PRIVATE);
		
	//	if (preferences.contains(ItemListActivity.USER)) {
			//prepare new view
			LoginFragment loginFragment = new LoginFragment();
			//replace old view
			getActivity().getSupportFragmentManager().beginTransaction()
					.replace(R.id.main_fragment_container, loginFragment)
					.commit();
			//adjust actionBar
			//getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
			//getActivity().getActionBar().setHomeButtonEnabled(true);
		} else {
			//Here you can handle errors.
			//error messages returned by server are stored in errors
		}
	}

}
