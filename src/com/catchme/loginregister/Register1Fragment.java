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
import com.catchme.exampleObjects.LoggedUser;
import com.catchme.loginregister.asynctasks.LoginRegisterInterface;

public class Register1Fragment extends Fragment implements OnClickListener,
		LoginRegisterInterface {
	private View rootView;
	public static final String EMAIL = "useremail";
	private ProgressBar register_loading;
	private EditText email, pass, conf_pass;

	public Register1Fragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// inflate layout from xml
		rootView = inflater.inflate(R.layout.fragment_register1, container,
				false);

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
		if(email == null){
		email.setError("E-mail is required!");
		}
		if(pass == null){
			pass.setError("Password is required");
		}
		if(conf_pass == null){
			conf_pass.setError("Password Confirm is required");
		}
		
		Bundle arguments = new Bundle();
		arguments
				.putString(Register2Fragment.EMAIL, email.getText().toString());
		arguments.putString(Register2Fragment.PASS, pass.getText().toString());
		arguments.putString(Register2Fragment.CONFPASS, conf_pass.getText()
				.toString());
		Register2Fragment regFragment = new Register2Fragment();
		regFragment.setArguments(arguments);
		FragmentTransaction transaction = getActivity()
				.getSupportFragmentManager().beginTransaction();
		transaction.setCustomAnimations(android.R.anim.fade_in,
				android.R.anim.fade_out);
		transaction.replace(R.id.main_fragment_container, regFragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	@Override
	public void onPreExecute() {
		register_loading.setVisibility(View.VISIBLE);
	}

	@Override
	public void onCompleted(LoggedUser user) {
		register_loading.setVisibility(View.GONE);

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
