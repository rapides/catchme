package com.catchme.loginregister;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.LongSparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.catchme.R;
import com.catchme.contactlist.ItemListFragment;
import com.catchme.database.CatchmeDatabaseAdapter;
import com.catchme.loginregister.asynctasks.LoginRegisterInterface;
import com.catchme.loginregister.asynctasks.RegisterTask;
import com.catchme.model.LoggedUser;

public class RegisterFragment extends Fragment implements OnClickListener,
		LoginRegisterInterface {
	public static final String EMAIL = "useremail";
	private View rootView;
	private ProgressBar register_loading;
	private EditText email, name, surname, pass, conf_pass;
	private CatchmeDatabaseAdapter dbAdapter;

	public RegisterFragment(CatchmeDatabaseAdapter dbAdapter) {
		this.dbAdapter = dbAdapter;
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
		new RegisterTask(getActivity(), this).execute(
				name.getText().toString(), surname.getText().toString(), email
						.getText().toString(), pass.getText().toString(),
				conf_pass.getText().toString());
	}

	@Override
	public void onPreExecute() {
		register_loading.setVisibility(View.VISIBLE);
	}

	@Override
	public void onCompleted(LoggedUser user) {
		register_loading.setVisibility(View.GONE);
		Toast.makeText(getActivity(),
				"Success! Registered user: " + user.getFullName(),
				Toast.LENGTH_SHORT).show();
		ItemListFragment mainFragment = new ItemListFragment(dbAdapter);
		// replace old view
		getActivity().getSupportFragmentManager().beginTransaction()
				.replace(R.id.main_fragment_container, mainFragment).commit();
		// adjust actionBar
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		getActivity().getActionBar().setHomeButtonEnabled(true);
	}

	@Override
	public void onError(LongSparseArray<String> errors) {
		register_loading.setVisibility(View.GONE);
		String errorsSring = "";
		if (errors != null) {
			for (int i = 0; i < errors.size(); i++) {
				errorsSring += errors.get(errors.keyAt(i)) + "\n";
			}
		}
		Toast.makeText(getActivity(), errorsSring, Toast.LENGTH_SHORT).show();
	}

}
