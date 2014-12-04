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
import android.widget.ProgressBar;
import android.widget.Toast;

public class RegisterFragment extends Fragment implements OnClickListener,
		OnTaskCompleted {
	public static final String EMAIL = "useremail";
	private View rootView;
	private ProgressBar register_loading;
	private EditText email, name, surname, pass, conf_pass;

	public RegisterFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// inflate layout from xml
		rootView = inflater.inflate(R.layout.fragment_register, container,
				false);
		name = (EditText) rootView.findViewById(R.id.reg_name);
		surname = (EditText) rootView.findViewById(R.id.reg_surname);
		email = (EditText) rootView.findViewById(R.id.reg_email);
		pass = (EditText) rootView.findViewById(R.id.reg_pass);
		conf_pass = (EditText) rootView.findViewById(R.id.reg_pass_conf);

		Bundle arguments = getArguments();
		String email_arg = arguments.getString(EMAIL);
		if (email_arg != null) {

			email.setText(email_arg, EditText.BufferType.EDITABLE);
		}
		register_loading = (ProgressBar) rootView
				.findViewById(R.id.register_spinner);
		register_loading.setVisibility(View.GONE);
		Button reg = (Button) rootView.findViewById(R.id.register);
		reg.setOnClickListener(this);
		return rootView;
	}

	@Override
	public void onClick(View v) {

		/*
		 * Toast.makeText(rootView.getContext(), name.toString()+
		 * " ---- "+pass.toString()+ " ---- "+surname.toString() +
		 * " ---"+email.toString()+"--p2--"+conf_pass.toString(),
		 * Toast.LENGTH_LONG).show();
		 */
		new RegisterTask(getActivity(), this, register_loading).execute(name
				.getText().toString(), surname.getText().toString(), email
				.getText().toString(), pass.getText().toString(), conf_pass
				.getText().toString());

		// TODO show animation or something else
	}

	@Override
	public void onTaskCompleted(ArrayList<String> errors) {
		// get storage
		SharedPreferences preferences = getActivity().getSharedPreferences(
				ItemListActivity.PREFERENCES, Context.MODE_PRIVATE);

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
			// Here you can handle errors.
			if (errors == null) {
				// not known error
			} else {
				String errorsSring = "";
				for (int i = 0; i < errors.size(); i++) {
					errorsSring += errors.get(i) + "\n";
				}
				Toast.makeText(getActivity(), errorsSring, Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

}
