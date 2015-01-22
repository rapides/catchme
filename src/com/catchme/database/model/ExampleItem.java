package com.catchme.database.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.support.v4.util.LongSparseArray;

import com.catchme.R;
import com.catchme.connections.ServerConst;

public class ExampleItem {
	public static final int IMAGE_INVALID = -1;
	public static final long AVATAR_SMALL = 0;
	public static final long AVATAR_MEDIUM = 1;
	public static final long AVATAR_BIG = 2;
	public static final long AVATAR_URL = 3;

	public enum UserSex {
		UNKNOWN(0, ServerConst.USER_SEX_UNKNOWN), MAN(1,
				ServerConst.USER_SEX_MALE), WOMAN(2,
				ServerConst.USER_SEX_FEMALE);
		UserSex(int val, String stringVal) {
			intValue = val;
			this.stringVal = stringVal;
		}

		int intValue;
		String stringVal;

		public int getIntegerValue() {
			return intValue;
		}

		public static UserSex getSexByString(String sex) {
			if (sex.equals(MAN.stringVal)) {
				return MAN;
			} else if (sex.equals(WOMAN.stringVal)) {
				return WOMAN;
			} else {
				return UNKNOWN;
			}
		}

		public String getStringValue() {
			return stringVal;
		}

		public static String[] getStringTable() {
			return new String[] { UNKNOWN.getLocalString(),
					MAN.getLocalString(), WOMAN.getLocalString() };
		}

		private String getLocalString() {
			return localString;
		}
	}

	public enum ContactStateType {
		INVITED(0), ACCEPTED(1), REJECTED(2), SENT(3), RECEIVED(4), ALL(5);
		int intValue;

		ContactStateType(int val) {
			intValue = val;
		}

		public int getIntegerValue() {
			return intValue;
		}

		public static ContactStateType getStateType(int integerValue) {
			for (int i = 0; i < values().length; i++) {
				if (values()[i].getIntegerValue() == integerValue) {
					return values()[i];
				}
			}
			return null;
		}
	}

	private long id;// idcontactu
	private ContactStateType state;

	private String name;
	private String surname;
	private String email;

	private List<Long> conversationIds;
	protected LongSparseArray<String> avatars;
	private UserSex sex;
	private Date dateOfBirth;

	public ExampleItem(long id, String name, String surname, String email,
			ContactStateType state, List<Long> conv_ids,
			LongSparseArray<String> avatars, UserSex sex, String dob) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.surname = surname;
		this.state = state;
		this.conversationIds = conv_ids;
		this.avatars = avatars;
		this.sex = sex;
		this.dateOfBirth = new Date();// TODO importing date

	}

	public ExampleItem(ExampleItem item) {
		this.id = item.id;
		this.name = item.name;
		this.avatars = item.avatars;

		this.email = item.email;
		this.surname = item.surname;
		this.state = item.state;
		this.dateOfBirth = item.dateOfBirth;
		this.sex = item.sex;
	}

	@Override
	public String toString() {
		return "Id: " + id + ", " + name + " " + surname + ", " + email;
	}

	public String getName() {
		return name;
	}

	public String getFullName() {
		return name + " " + surname;
	}

	public String getSurname() {
		return surname;
	}

	public long getId() {
		return id;
	}

	public ContactStateType getState() {
		return state;
	}

	public String getEmail() {
		return email;
	}

	/**
	 * Returns first conversation id. Used in conversations between 2 people.
	 * 
	 * @return Conversation id;
	 */
	public Long getFirstConversationId() {
		return conversationIds.get(0);
	}

	public List<Long> getConversations() {
		return conversationIds;
	}

	public String getSmallImageUrl() {
		if (avatars != null) {
			String url = avatars.get(AVATAR_SMALL);
			if (url.length() > 4) {
				return url.contains(ServerConst.SERVER_IP) ? url
						: ServerConst.SERVER_IP + url;
			} else {
				return getDefaultImage();
			}
		} else {
			return getDefaultImage();
		}
	}

	public String getMediumImageUrl() {
		if (avatars != null) {
			String url = avatars.get(AVATAR_MEDIUM);
			if (url.length() > 4) {

				return url.contains(ServerConst.SERVER_IP) ? url
						: ServerConst.SERVER_IP + url;
			} else {
				return getDefaultImage();
			}
		} else {
			return getDefaultImage();
		}

	}

	public String getLargeImageUrl() {
		if (avatars != null) {
			String url = avatars.get(AVATAR_BIG);
			if (url.length() > 4) {
				return url.contains(ServerConst.SERVER_IP) ? url
						: ServerConst.SERVER_IP + url;
			} else {
				return getDefaultImage();
			}
		} else {
			return getDefaultImage();
		}
	}

	public String getOriginalImageURl() {
		if (avatars != null) {
			String url = avatars.get(AVATAR_URL);
			if (url.length() > 4) {
				return url.contains(ServerConst.SERVER_IP) ? url
						: ServerConst.SERVER_IP + url;
			} else {
				return getDefaultImage();
			}
		} else {
			return getDefaultImage();
		}
	}

	private String getDefaultImage() {
		return "drawable://" + R.drawable.loader;
	}

	public UserSex getSex() {
		return sex;
	}

	public String getBirthday() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd",
				Locale.getDefault());
		return dateFormat.format(dateOfBirth);
	}

}