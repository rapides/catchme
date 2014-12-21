package com.catchme.loginregister;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import com.catchme.database.model.LoggedUser;
import com.catchme.loginregister.asynctasks.LoginRegisterInterface;
import com.catchme.loginregister.asynctasks.LoginTask;

public class LoginFragment extends Fragment implements OnClickListener,
		LoginRegisterInterface {
	private View rootView;
	private ProgressBar login_loading;
	private EditText login, pass;
	
	public LoginFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// inflate layout from xml
		rootView = inflater.inflate(R.layout.fragment_login, container, false);

		login = (EditText) rootView.findViewById(R.id.login_email);
		pass = (EditText) rootView.findViewById(R.id.login_pass);
		Button login_btn = (Button) rootView.findViewById(R.id.login);
		Button register_btn = (Button) rootView
				.findViewById(R.id.goto_register);
		login_loading = (ProgressBar) rootView.findViewById(R.id.login_spinner);
		login_loading.setVisibility(View.GONE);
		register_btn.setOnClickListener(this);
		login_btn.setOnClickListener(this);
		return rootView;
	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.login) {
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

		}
	}

	@Override
	public void onPreExecute() {
		login_loading.setVisibility(View.VISIBLE);
		// animaton.setVisible(VIw.Visible);
	}

	@Override
	public void onCompleted(LoggedUser user) {
		login_loading.setVisibility(View.GONE);
		Toast.makeText(getActivity(),
				"Success! Logged user: " + user.getFullName(),
				Toast.LENGTH_SHORT).show();
		ItemListFragment mainFragment = new ItemListFragment();
		// replace old view
		getActivity().getSupportFragmentManager().beginTransaction()
				.replace(R.id.main_fragment_container, mainFragment).commit();
		// adjust actionBar
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		getActivity().getActionBar().setHomeButtonEnabled(true);

	}

	@Override
	public void onError(LongSparseArray<String> errors) {

		login_loading.setVisibility(View.GONE);
		String errorsSring = "";
		if (errors != null) {
			for (int i = 0; i < errors.size(); i++) {
				errorsSring += errors.get(errors.keyAt(i)) + "\n";
			}
		}
		login.setBackgroundResource(R.drawable.error_frame);
		login.setError("Login is required!!!");
		pass.setError("Password is Required!!!");
		pass.setBackgroundResource(R.drawable.error_frame);
		Toast.makeText(getActivity(), errorsSring, Toast.LENGTH_SHORT).show();
	}
}
