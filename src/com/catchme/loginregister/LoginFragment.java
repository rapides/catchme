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
import android.view.WindowManager;
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
	Drawable originalBackground;
	private EditText login, pass;

	public LoginFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// inflate layout from xml

		rootView = inflater.inflate(R.layout.fragment_login, container, false);
		getActivity().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		login = (EditText) rootView.findViewById(R.id.login_email);
		pass = (EditText) rootView.findViewById(R.id.login_pass);
		originalBackground = login.getBackground();
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
			if (validate()) {
				new LoginTask(getActivity(), this).execute(login.getText()
						.toString(), pass.getText().toString());
			}

		}
		if (v.getId() == R.id.goto_register) {
			// replace old view
			Bundle arguments = new Bundle();
			arguments.putString(Register1Fragment.EMAIL, login.getText()
					.toString());
			Register1Fragment regFragment = new Register1Fragment();
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
		login.addTextChangedListener(new watcher(login));
		pass.addTextChangedListener(new watcher(login));
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

		Toast.makeText(getActivity(), errorsSring, Toast.LENGTH_SHORT).show();
	}

	public class watcher implements TextWatcher {
		private EditText et;

		private watcher(EditText editText) {
			this.et = editText;
		}

		public void afterTextChanged(Editable s) {
			switch (et.getId()) {
			case R.id.login_email: {
				login.setBackground(originalBackground);
			}
				break;

			case R.id.login_pass: {
				pass.setBackground(originalBackground);

			}
				break;
			}
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			if (s.length() == 0) {
				switch (et.getId()) {
				case R.id.login_email: {
					// login.setError("E-mail is required!");
					login.setBackgroundResource(R.drawable.error_frame);
				}
					break;

				case R.id.login_pass: {
					// pass.setError("Password is required!");
					pass.setBackgroundResource(R.drawable.error_frame);
				}
					break;
				}
			}

		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}
	};

	public boolean validate() {
		int errCnt = 0;
		String errorsString = "";
		if (login.getText() == null || login.getText().toString().length() < 1) {
			login.setError("E-mail is required!");
			login.setBackgroundResource(R.drawable.error_frame);
			login.addTextChangedListener(new watcher(login));
			errorsString += "E-mail is required! \n";

			errCnt += 1;
		}
		if (login.getText().toString().length() > 0
				&& !android.util.Patterns.EMAIL_ADDRESS.matcher(
						login.getText().toString()).matches()) {
			login.setError("E-mail is incorrect!");
			login.setBackgroundResource(R.drawable.error_frame);
			login.addTextChangedListener(new watcher(login));
			errorsString += "E-mail is incorrect! \n";

			errCnt += 1;
		}

		if (pass.getText() == null || pass.getText().toString().length() < 1) {
			pass.setError("Password is required!");
			errorsString += "Password is required! \n";
			pass.setBackgroundResource(R.drawable.error_frame);
			pass.addTextChangedListener(new watcher(pass));

			errCnt += 1;
		} else if (pass.getText() != null
				&& pass.getText().toString().length() < 6) {
			pass.setError("Password must be atleast 6 characters long!");
			pass.setBackgroundResource(R.drawable.error_frame);
			errorsString += "Password must be atleast 6 characters long! ";
			pass.addTextChangedListener(new watcher(pass));
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
