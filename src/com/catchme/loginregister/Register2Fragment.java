package com.catchme.loginregister;

import java.io.File;
import java.io.FileOutputStream;

import android.content.ActivityNotFoundException;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.LongSparseArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import com.catchme.R;
import com.catchme.connections.ServerConst;
import com.catchme.contactlist.ItemListActivity;
import com.catchme.contactlist.ItemListFragment;
import com.catchme.database.model.LoggedUser;
import com.catchme.database.model.ExampleItem.UserSex;
import com.catchme.loginregister.asynctasks.LoginRegisterInterface;
import com.catchme.loginregister.asynctasks.PersonalDataTask;
import com.catchme.profile.ImageUploadingListener;
import com.catchme.profile.asynctasks.UpdateAvatarTask;
import com.catchme.utils.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class Register2Fragment extends Fragment implements OnClickListener,
		LoginRegisterInterface, ImageUploadingListener {

	public static final String EMAIL = "useremail";
	public static final String PASS = "password";
	public static final String CONFPASS = "confirmpassword";
	public static final int PICK_IMAGE = 0;

	public static final int PIC_CROP = 1;

	private ProgressBar registerLoading, avatarUploading;
	private EditText name, surname;
	private RoundedImageView image;
	private UserSex sex;
	private Drawable originalBackground;
	private Spinner gender;

	public Register2Fragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// inflate layout from xml

		View rootView = inflater.inflate(R.layout.fragment_register2,
				container, false);
		name = (EditText) rootView.findViewById(R.id.reg_name);
		surname = (EditText) rootView.findViewById(R.id.reg_surname);
		image = (RoundedImageView) rootView.findViewById(R.id.user_avatar);
		originalBackground = name.getBackground();


		/*
		 * Bundle arguments = getArguments(); email =
		 * arguments.getString(Register1Fragment.EMAIL); pass =
		 * arguments.getString(PASS); conf_pass = arguments.getString(CONFPASS);
		 */

		registerLoading = (ProgressBar) rootView
				.findViewById(R.id.register_spinner);
		registerLoading.setVisibility(View.GONE);
		avatarUploading = (ProgressBar) rootView

				.findViewById(R.id.upload_avatar_progress);
		avatarUploading.setVisibility(View.GONE);

		// Choose Gender Spinner
		gender = (Spinner) rootView.findViewById(R.id.reg_sex);
		String[] items = new String[] { "Choose gender", "Male", "Female" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, items);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		gender.setAdapter(adapter);

		Button reg = (Button) rootView.findViewById(R.id.register);
		Button avatar = (Button) rootView.findViewById(R.id.change_avatar);

		avatar.setOnClickListener(this);

		reg.setOnClickListener(this);
		return rootView;
	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.change_avatar) {
			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(
					Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
		}
		if (v.getId() == R.id.register) {
			if (validate()) {
				if (gender.getSelectedItemPosition() == 0) {
					sex = UserSex.UNKNOWN;
				}
				if (gender.getSelectedItemPosition() == 1) {
					sex = UserSex.MAN;
				}
				if (gender.getSelectedItemPosition() == 2) {
					sex = UserSex.WOMAN;
				}

				new PersonalDataTask(getActivity(), this).execute(
						ItemListActivity.getLoggedUser(getActivity())
								.getToken(), name.getText().toString(), surname
								.getText().toString(), sex.getStringValue(),
						ServerConst.DATE_FORMAT);
			}
		}

		/*
		 * new RegisterTask(getActivity(), this).execute(
		 * name.getText().toString(), surname.getText().toString(), email, pass,
		 * conf_pass);
		 */
	}

	@Override
	public void onPreExecute() {
		registerLoading.setVisibility(View.VISIBLE);
	}

	@Override
	public void onCompleted(LoggedUser user) {
		registerLoading.setVisibility(View.GONE);

		Toast.makeText(getActivity(),
				"Success! Registered user: " + user.getFullName(),
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
		registerLoading.setVisibility(View.GONE);
		String errorsSring = "";
		if (errors != null) {
			for (int i = 0; i < errors.size(); i++) {
				errorsSring += errors.get(errors.keyAt(i)) + "\n";
			}
		}
		Toast.makeText(getActivity(), errorsSring, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PICK_IMAGE && data != null && data.getData() != null) {
			Uri uri = data.getData();
			String imageFilePath = null;
			if (uri.getScheme().equals("content")) {// fromGallery

				// User had pick an image.
				Cursor cursor = getActivity()
						.getContentResolver()
						.query(uri,
								new String[] { android.provider.MediaStore.Images.ImageColumns.DATA },
								null, null, null);
				cursor.moveToFirst();

				// Link to the image
				imageFilePath = cursor.getString(0);
				cursor.close();
			} else {
				imageFilePath = uri.getSchemeSpecificPart();
			}

			performCrop(imageFilePath);
			/*Toast.makeText(getActivity(),
					"DEBUG: " + "file://" + imageFilePath, Toast.LENGTH_LONG)
					.show();*/
			// Wyswietla obraz z galeria jako avatar
			/*
			 * ImageLoader.getInstance().displayImage("file://" + imageFilePath,
			 * image);
			 */
			/*
			 * new UpdateAvatarTask(this.getActivity(), this).execute(
			 * ((LoggedUser) item).getToken(), imageFilePath);
			 */
		} else if (requestCode == PIC_CROP) {
			// get the cropped bitmap
			Bitmap thePic = data.getExtras().getParcelable("data");
			// image.setImageBitmap(thePic);

			// Cropped image save and update
			ContextWrapper cw = new ContextWrapper(getActivity());
			// path to /data/data/yourapp/app_data/imageDir
			File directory = cw.getExternalFilesDir("AvatarJPG");
			// Create imageDir
			if (!directory.exists()) {
				directory.mkdirs();
			}
			File avatar = new File(directory, "profile.jpg");
			if (avatar.exists()) {
				avatar.delete();
			}

			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(avatar);
				// Use the compress method on the BitMap object to write image
				// to the OutputStream
				thePic.compress(Bitmap.CompressFormat.JPEG, 100, fos);
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			Toast.makeText(getActivity(),
					"DEBUG: " + directory.getAbsolutePath(), Toast.LENGTH_LONG)
					.show();
			ImageLoader.getInstance().displayImage(
					"file://" + directory.getAbsolutePath() + "/profile.jpg",
					image);
			new UpdateAvatarTask(this.getActivity(), this).execute(
					ItemListActivity.getLoggedUser(getActivity()).getToken(),
					directory.getAbsolutePath() + "/profile.jpg");
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void performCrop(String imageFilePath) {
		try {
			// call the standard crop action intent (the user device may not
			// support it)
			Intent cropIntent = new Intent("com.android.camera.action.CROP");
			// indicate image type and Uri
			File file = new File(imageFilePath);
			Uri uri = Uri.fromFile(file);
			cropIntent.setDataAndType(uri, "image/*");
			// set crop properties
			cropIntent.putExtra("crop", "true");
			// indicate aspect of desired crop
			cropIntent.putExtra("aspectX", 1);
			cropIntent.putExtra("aspectY", 1);
			// indicate output X and Y
			cropIntent.putExtra("outputX", 256);
			cropIntent.putExtra("outputY", 256);
			// retrieve data on return
			cropIntent.putExtra("return-data", true);
			// start the activity - we handle returning in onActivityResult
			startActivityForResult(cropIntent, PIC_CROP);

		} catch (ActivityNotFoundException anfe) {
			// display an error message
			String errorMessage = "Whoops - your device doesn't support the crop action!";
			Toast toast = Toast.makeText(getActivity(), errorMessage,
					Toast.LENGTH_SHORT);
			toast.show();
		}

	}

	@Override
	public void onPreUpdate() {
		avatarUploading.setVisibility(View.VISIBLE);

	}

	@Override
	public void onImageUploaded() {

		avatarUploading.setVisibility(View.GONE);


	}

	@Override
	public void onImageUploadError(LongSparseArray<String> errors) {
		Toast.makeText(getActivity(), "UPLOAD FAIL:\n" + errors.toString(),
				Toast.LENGTH_LONG).show();

	}

	public class watcher implements TextWatcher {
		private EditText et;

		private watcher(EditText editText) {
			this.et = editText;
		}

		public void afterTextChanged(Editable s) {
			switch (et.getId()) {
			case R.id.reg_name: {
				name.setBackground(originalBackground);
			}
				break;

			case R.id.reg_surname: {
				surname.setBackground(originalBackground);

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
		if (name.getText() == null || name.getText().toString().length() < 1) {
			name.setError("Name is required!");
			name.setBackgroundResource(R.drawable.error_frame);
			name.addTextChangedListener(new watcher(name));
			errorsString += "Name is required! \n";

			errCnt += 1;
		}
		if (surname.getText() == null
				|| surname.getText().toString().length() < 1) {
			surname.setError("Surname is required!");
			errorsString += "Password is required! \n";
			surname.setBackgroundResource(R.drawable.error_frame);
			surname.addTextChangedListener(new watcher(surname));

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
