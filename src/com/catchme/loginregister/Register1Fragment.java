package com.catchme.loginregister;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.LongSparseArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.catchme.R;
import com.catchme.database.model.LoggedUser;
import com.catchme.loginregister.asynctasks.LoginRegisterInterface;
import com.catchme.loginregister.asynctasks.RegisterTask;

public class Register1Fragment extends Fragment implements OnClickListener,
		LoginRegisterInterface {
	private View rootView;
	public static final String EMAIL = "useremail";
	private ProgressBar registerLoading;
	private EditText email, pass, conf_pass;
	private Drawable originalBackground;

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
		originalBackground = email.getBackground();

		
		if (getArguments().containsKey(EMAIL)) {
			email.setText(getArguments().getString(EMAIL), EditText.BufferType.EDITABLE);
		}

		registerLoading = (ProgressBar) rootView
				.findViewById(R.id.register_spinner);
		registerLoading.setVisibility(View.GONE);
		Button reg = (Button) rootView.findViewById(R.id.register);
		reg.setOnClickListener(this);
		return rootView;
	}

	@Override
	public void onClick(View v) {

		if (validate()) {
			new RegisterTask(getActivity(), this).execute(email.getText()
					.toString(), pass.getText().toString(), conf_pass.getText()
					.toString());
		}

	}

	@Override
	public void onPreExecute() {
		registerLoading.setVisibility(View.VISIBLE);
	}

	@Override
	public void onCompleted(LoggedUser user) {
		registerLoading.setVisibility(View.GONE);
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
	public void onError(LongSparseArray<String> errors) {
		registerLoading.setVisibility(View.GONE);
		String errorsString = "";
		if (errors != null) {
			for (int i = 0; i < errors.size(); i++) {
				errorsString += errors.get(errors.keyAt(i)) + "\n";
			}
		}
		Toast.makeText(getActivity(), errorsString, Toast.LENGTH_SHORT).show();
	}

	public class watcher implements TextWatcher {
		private EditText et;

		private watcher(EditText editText) {
			this.et = editText;
		}

		public void afterTextChanged(Editable s) {
			switch (et.getId()) {
			case R.id.reg_email: {
				email.setBackground(originalBackground);
			}
				break;

			case R.id.reg_pass: {
				pass.setBackground(originalBackground);

			}
				break;
			case R.id.reg_pass_conf: {
				conf_pass.setBackground(originalBackground);

			}
				break;
			}
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}
	};

	public boolean validate() {

		int errCnt = 0;
		String errorsString = "";
		if (email.getText() == null || email.getText().toString().length() < 1) {
			email.setError("E-mail is required!");
			email.setBackgroundResource(R.drawable.error_frame);
			email.addTextChangedListener(new watcher(email));
			errorsString += "E-mail is required! \n";

			errCnt += 1;
		}
		if (email.getText().toString().length() > 0
				&& !android.util.Patterns.EMAIL_ADDRESS.matcher(
						email.getText().toString()).matches()) {
			email.setError("E-mail is incorrect!");
			email.setBackgroundResource(R.drawable.error_frame);
			email.addTextChangedListener(new watcher(email));
			errorsString += "E-mail is incorrect! \n";

			errCnt += 1;
		}

		if (pass.getText() == null || pass.getText().toString().length() < 1) {
			pass.setError("Password is required!");
			errorsString += "Password is required! \n";
			pass.setBackgroundResource(R.drawable.error_frame);
			pass.addTextChangedListener(new watcher(pass));

			errCnt += 1;
		}
		if (pass.getText() != null && pass.getText().toString().length() < 6) {
			pass.setError("Password must be atleast 6 characters long!");
			pass.setBackgroundResource(R.drawable.error_frame);
			errorsString += "Password must be atleast 6 characters long! \n";
			pass.addTextChangedListener(new watcher(pass));
			errCnt += 1;
		}
		if (conf_pass.getText() == null
				|| conf_pass.getText().toString().length() < 1) {
			conf_pass.setError("Password Confirm is required!");
			errorsString += "Password Confirm is required! \n";
			conf_pass.setBackgroundResource(R.drawable.error_frame);
			conf_pass.addTextChangedListener(new watcher(conf_pass));

			errCnt += 1;
		}
		if (conf_pass.getText() != null
				&& pass.getText() != null
				&& pass.getText().toString().length() >= 6
				&& !conf_pass.getText().toString()
						.equals(pass.getText().toString())) {
			conf_pass.setError("Confirm Password is wrong!");
			errorsString += "Passwords do not match! ";
			conf_pass.setBackgroundResource(R.drawable.error_frame);
			conf_pass.addTextChangedListener(new watcher(conf_pass));

			errCnt += 1;
		}
		if (errCnt > 0) {
			Toast.makeText(getActivity(), errorsString, Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		return true;
	}

}
