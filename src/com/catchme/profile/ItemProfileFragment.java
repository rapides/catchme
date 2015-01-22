package com.catchme.profile;

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
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.LongSparseArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.catchme.R;
import com.catchme.connections.ServerConst;
import com.catchme.contactlist.ItemListActivity;
import com.catchme.database.CatchmeDatabaseAdapter;
import com.catchme.database.model.ExampleItem;
import com.catchme.database.model.LoggedUser;
import com.catchme.database.model.ExampleItem.ContactStateType;
import com.catchme.database.model.ExampleItem.UserSex;
import com.catchme.itemdetails.ItemDetailsFragment;
import com.catchme.loginregister.asynctasks.LoginRegisterInterface;
import com.catchme.loginregister.asynctasks.PersonalDataTask;
import com.catchme.profile.asynctasks.UpdateAvatarTask;
import com.catchme.utils.FloatingActionButton;
import com.catchme.utils.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ItemProfileFragment extends Fragment implements OnClickListener,
		ContactChangedState, ImageUploadingListener, LoginRegisterInterface {
	private View rootView;
	private ExampleItem item;
	private boolean isLoggedUser, avatarChanged;
	public static final int PICK_IMAGE = 0;
	public static final int PIC_CROP = 1;

	private EditText name, surname;
	private RoundedImageView itemImage;
	private ProgressBar edit_change, avatar_uploading;
	private Spinner gender;
	private RelativeLayout profileEditdata, profileData;
	private FloatingActionButton avatarChangeBtn;
	private Drawable originalBackground;
	private UserSex sex;
	File avatar;

	public ItemProfileFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater
				.inflate(R.layout.fragment_profile, container, false);

		if (getArguments() == null) {
			isLoggedUser = true;
			item = ItemListActivity.getLoggedUser(getActivity());
		} else {
			isLoggedUser = false;
			long itemId = getArguments().getLong(
					ItemDetailsFragment.ARG_ITEM_ID);
			item = CatchmeDatabaseAdapter.getInstance(
					getActivity().getApplicationContext()).getItem(itemId);
		}
		// Progress Bars
		edit_change = (ProgressBar) rootView
				.findViewById(R.id.profile_edit_progress);
		edit_change.setVisibility(View.GONE);

		avatar_uploading = (ProgressBar) rootView
				.findViewById(R.id.profile_avatar_uploading);
		avatar_uploading.setVisibility(View.GONE);
		// Layouts
		profileData = (RelativeLayout) rootView
				.findViewById(R.id.profile_data_container);
		profileData.setVisibility(View.VISIBLE);
		profileEditdata = (RelativeLayout) rootView
				.findViewById(R.id.profile_edit_data_container);
		profileEditdata.setVisibility(View.GONE);

		TextView txtName = (TextView) rootView.findViewById(R.id.profile_name);
		TextView txtSurname = (TextView) rootView
				.findViewById(R.id.profile_surname);
		TextView txtEmail = (TextView) rootView
				.findViewById(R.id.profile_email);
		TextView txtSex = (TextView) rootView.findViewById(R.id.profile_sex);
		itemImage = (RoundedImageView) rootView
				.findViewById(R.id.profile_image);
		ImageLoader.getInstance().displayImage(item.getLargeImageUrl(),
				itemImage);

		RelativeLayout buttonsContainer = (RelativeLayout) rootView
				.findViewById(R.id.profile_state_buttons_container);

		txtName.setText(item.getName());
		txtSurname.setText(item.getSurname());
		txtEmail.setText(item.getEmail());
		txtSex.setText(item.getSex().getStringValue());

		// Edit Profile Variables
		name = (EditText) rootView.findViewById(R.id.profile_editName);
		surname = (EditText) rootView.findViewById(R.id.profile_editSurname);

		originalBackground = name.getBackground();
		name.setText(item.getName(), EditText.BufferType.EDITABLE);
		surname.setText(item.getSurname(), EditText.BufferType.EDITABLE);

		gender = (Spinner) rootView.findViewById(R.id.profile_editSex);
		String[] items = new String[] { "Choose gender", "Male", "Female" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, items);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		gender.setAdapter(adapter);

		// Buttons
		avatarChangeBtn = (FloatingActionButton) rootView
				.findViewById(R.id.profile_floating_action_button);
		avatarChangeBtn.setOnClickListener(this);
		avatarChangeBtn.setVisibility(View.GONE);
		Button editBtn = (Button) rootView.findViewById(R.id.profile_edit);
		editBtn.setOnClickListener(this);
		Button editBackBtn = (Button) rootView
				.findViewById(R.id.profile_editBack_btn);
		editBackBtn.setOnClickListener(this);
		Button editSaveBtn = (Button) rootView
				.findViewById(R.id.profile_editSave);
		editSaveBtn.setOnClickListener(this);
		editBtn.setVisibility(View.GONE);

		avatarChanged = false;

		if (isLoggedUser) {
			editBtn.setVisibility(View.VISIBLE);
			setHasOptionsMenu(true);
			// avatarChangeBtn.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// Intent intent = new Intent();
			// intent.setType("image/*");
			// intent.setAction(Intent.ACTION_GET_CONTENT);
			// startActivityForResult(
			// Intent.createChooser(intent, "Select Picture"),
			// PICK_IMAGE);
			// }
			// });
			// buttonsContainer.setVisibility(View.GONE);

		} else {
			if (item.getState() == ContactStateType.ACCEPTED) {
				buttonsContainer.setVisibility(View.GONE);
			} else {
				buttonsContainer.setVisibility(View.VISIBLE);
				Button accept = (Button) rootView
						.findViewById(R.id.profile_accept);
				Button reject = (Button) rootView
						.findViewById(R.id.profile_reject);
				accept.setOnClickListener(new ChangeStateButonListener(
						getActivity(), item, this));
				reject.setOnClickListener(new ChangeStateButonListener(
						getActivity(), item, this));
				setHasOptionsMenu(true);
			}
		}

		return rootView;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.profile_edit) {
			profileData.setVisibility(View.GONE);
			profileEditdata.setVisibility(View.VISIBLE);
			avatarChangeBtn.setVisibility(View.VISIBLE);

		}
		if (v.getId() == R.id.profile_floating_action_button) {
			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(
					Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);

		}
		if (v.getId() == R.id.profile_editBack_btn) {
			profileEditdata.setVisibility(View.GONE);
			avatarChangeBtn.setVisibility(View.GONE);
			ImageLoader.getInstance().displayImage(item.getLargeImageUrl(),
					itemImage);
			profileData.setVisibility(View.VISIBLE);
			avatarChanged = false;

		}
		if (v.getId() == R.id.profile_editSave) {
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

				if (avatarChanged) {
					String filePath = avatar.getAbsolutePath();
					new UpdateAvatarTask(this.getActivity(), this).execute(
							ItemListActivity.getLoggedUser(getActivity())
									.getToken(), filePath);
					new PersonalDataTask(getActivity(), this).execute(
							ItemListActivity.getLoggedUser(getActivity())
									.getToken(), name.getText().toString(),
							surname.getText().toString(), sex.getStringValue(),
							ServerConst.DATE_FORMAT);
				} else {
					new PersonalDataTask(getActivity(), this).execute(
							ItemListActivity.getLoggedUser(getActivity())
									.getToken(), name.getText().toString(),
							surname.getText().toString(), sex.getStringValue(),
							ServerConst.DATE_FORMAT);
				}

			}
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home: {
			getActivity().dispatchKeyEvent(
					new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
			getActivity().onBackPressed();
			return true;
		}
		}
		return super.onOptionsItemSelected(item);

	}

	@Override
	public void contactChangedState(ContactStateType newState) {
		if (newState != null) {

			if (newState == ContactStateType.ACCEPTED) {
				Toast.makeText(getActivity(),
						"ItemProfileFragment: Contact Accepted",
						Toast.LENGTH_SHORT).show();
			} else if (newState == ContactStateType.REJECTED) {
				Toast.makeText(getActivity(),
						"ItemProfileFragment: Contact Rejected",
						Toast.LENGTH_SHORT).show();
			}
			getActivity().dispatchKeyEvent(
					new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
			getActivity().onBackPressed();
		} else {
			Toast.makeText(getActivity(),
					"ItemProfileFragment: Server problem, try again later.",
					Toast.LENGTH_SHORT).show();
		}

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
		} else if (requestCode == PIC_CROP) {
			// get the returned data
			Bundle extras = data.getExtras();
			// get the cropped bitmap
			Bitmap thePic = extras.getParcelable("data");
			// image.setImageBitmap(thePic);

			// Cropped image save and update
			ContextWrapper cw = new ContextWrapper(getActivity());
			// path to /data/data/yourapp/app_data/imageDir
			File directory = cw.getExternalFilesDir("AvatarJPG");
			// Create imageDir
			if (!directory.exists())
				directory.mkdirs();
			avatar = new File(directory, "avatarTemp.jpg");
			if (avatar.exists())
				avatar.delete();

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

			ImageLoader.getInstance().displayImage(
					"file://" + avatar.getAbsolutePath(), itemImage);
			avatarChanged = true;
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
		avatar_uploading.setVisibility(View.VISIBLE);
		Toast.makeText(getActivity(), "Started", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onImageUploaded() {
		avatar_uploading.setVisibility(View.GONE);
		/*
		 * item = ItemListActivity.getLoggedUser(getActivity());
		 * ImageLoader.getInstance().displayImage(item.getLargeImageUrl(),
		 * itemImage); Toast.makeText(getActivity(), "Done",
		 * Toast.LENGTH_SHORT).show();
		 */
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
			case R.id.profile_editName: {
				name.setBackgroundDrawable(originalBackground);
			}
				break;

			case R.id.profile_editSurname: {
				surname.setBackgroundDrawable(originalBackground);

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
			errorsString += "Surname is required! \n";
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

	// Profile update operations

	@Override
	public void onPreExecute() {
		edit_change.setVisibility(View.VISIBLE);
	}

	@Override
	public void onCompleted(LoggedUser user) {
		edit_change.setVisibility(View.GONE);

		Toast.makeText(getActivity(),
				"Success! Edited user: " + user.getFullName(),
				Toast.LENGTH_SHORT).show();
		ItemProfileFragment frag = new ItemProfileFragment();
		FragmentTransaction transaction = getActivity()
				.getSupportFragmentManager().beginTransaction();
		transaction.setCustomAnimations(android.R.anim.fade_in,
				android.R.anim.fade_out);
		transaction.replace(R.id.main_fragment_container, frag);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	@Override
	public void onError(LongSparseArray<String> errors) {
		edit_change.setVisibility(View.GONE);
		String errorsSring = "";
		if (errors != null) {
			for (int i = 0; i < errors.size(); i++) {
				errorsSring += errors.get(errors.keyAt(i)) + "\n";
			}
		}
		Toast.makeText(getActivity(), errorsSring, Toast.LENGTH_SHORT).show();
	}
}
