package com.catchme.database;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.support.v4.util.LongSparseArray;
import android.util.Log;

import com.catchme.connections.ServerConst;
import com.catchme.database.model.ExampleItem;
import com.catchme.database.model.ExampleItem.ContactStateType;
import com.catchme.database.model.Message;

public class CatchmeDatabaseAdapter {
	private SQLiteDatabase db;
	private Context context;
	private DatabaseHelper dbHelper;

	private static final String DEBUG_TAG = "SqLiteManager";

	private static final int DB_VERSION = 5;
	private static final String DB_NAME = "catchmeDB.db";
	private static final String DB_ITEM_TABLE = "item";
	private static final String DB_ITEM_ID = "itemId";
	private static final String DB_ITEM_NAME = "name";
	private static final String DB_ITEM_SURNAME = "surname";
	private static final String DB_ITEM_EMAIL = "email";
	private static final String DB_ITEM_STATE = "state";
	private static final String DB_ITEM_SEX = "sex";
	private static final String DB_ITEM_BIRDTHDAY = "birthday";

	// private static final String DB_AVATAR_TABLE = "avatars";
	// private static final String DB_AVATAR_ID = "avatarId";
	private static final String DB_AVATAR_SMALL = "small";
	private static final String DB_AVATAR_MEDIUM = "medium";
	private static final String DB_AVATAR_BIG = "big";
	private static final String DB_AVATAR_URL = "url";

	private static final String DB_LOCATIONS_TABLE = "locations";
	private static final String DB_LOCATIONS_ACCURACY = "accuracy";
	private static final String DB_LOCATIONS_LATITUDE = "latitude";
	private static final String DB_LOCATIONS_LONGITUDE = "longitude";
	private static final String DB_LOCATIONS_TIME = "fixTime";

	private static final String DB_CONVERSATIONS_TABLE = "conversations";
	private static final String DB_CONVERSATIONS_ID = "convId";// database Id

	private static final String DB_MESSAGES_TABLE = "messages";
	private static final String DB_MESSAGES_ID = "messagesId";
	private static final String DB_MESSAGES_SENDER = "senderId";
	private static final String DB_MESSAGES_CONTENT = "content";
	private static final String DB_MESSAGES_TIME = "time";

	private static final String DB_CREATE_ITEM_TABLE = "CREATE TABLE "
			+ DB_ITEM_TABLE + " (" + DB_ITEM_ID + " INTEGER PRIMARY KEY, "
			+ DB_ITEM_NAME + " TEXT NOT NULL, " + DB_ITEM_SURNAME
			+ " TEXT NOT NULL, " + DB_ITEM_EMAIL + " TEXT NOT NULL, "
			+ DB_ITEM_STATE + " INTEGER, " + DB_ITEM_SEX + " TEXT NOT NULL, "
			+ DB_ITEM_BIRDTHDAY + " DATETIME, " + DB_AVATAR_SMALL
			+ " TEXT NOT NULL, " + DB_AVATAR_MEDIUM + " TEXT NOT NULL, "
			+ DB_AVATAR_BIG + " TEXT NOT NULL, " + DB_AVATAR_URL
			+ " TEXT NOT NULL " + ")";

	/*
	 * private static final String DB_CREATE_AVATARS_TABLE = "CREATE TABLE " +
	 * DB_AVATAR_TABLE + " (" + DB_AVATAR_ID
	 * +" INTEGER PRIMARY KEY AUTOINCREMENT, " + DB_ITEM_ID+
	 * " INTEGER PRIMARY KEY, " + DB_AVATAR_SMALL + " TEXT NOT NULL, " +
	 * DB_AVATAR_MEDIUM + " TEXT NOT NULL, " + DB_AVATAR_BIG+ " TEXT NOT NULL, "
	 * + DB_AVATAR_URL + " TEXT NOT NULL " + ")";
	 */

	private static final String DB_CREATE_LOCATIONS_TABLE = "CREATE TABLE "
			+ DB_LOCATIONS_TABLE + " (" + DB_ITEM_ID + " INTEGER NOT NULL, "
			+ DB_LOCATIONS_ACCURACY + " REAL NOT NULL, "
			+ DB_LOCATIONS_LATITUDE + " REAL NOT NULL, "
			+ DB_LOCATIONS_LONGITUDE + " REAL NOT NULL, " + DB_LOCATIONS_TIME
			+ " INTEGER NOT NULL," + " PRIMARY KEY ( " + DB_ITEM_ID + ", "
			+ DB_LOCATIONS_TIME + ")" + ")";

	private static final String DB_CREATE_CONVERSATIONS_TABLE = "CREATE TABLE "
			+ DB_CONVERSATIONS_TABLE + " (" + DB_CONVERSATIONS_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + DB_ITEM_ID
			+ " INTEGER, " + DB_MESSAGES_ID + " INTEGER " + ")";

	private static final String DB_CREATE_MESSAGES_TABLE = "CREATE TABLE "
			+ DB_MESSAGES_TABLE + " (" + DB_MESSAGES_ID
			+ " INTEGER PRIMARY KEY, " + DB_CONVERSATIONS_ID + " INTEGER, "
			+ DB_MESSAGES_SENDER + " INTEGER, " + DB_MESSAGES_CONTENT
			+ " TEXT NOT NULL, " + DB_MESSAGES_TIME + " TEXT NOT NULL " + ")";

	private class DatabaseHelper extends SQLiteOpenHelper {
		public DatabaseHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.d(DEBUG_TAG, "Database creating...");
			db.execSQL(DB_CREATE_ITEM_TABLE);
			db.execSQL(DB_CREATE_CONVERSATIONS_TABLE);
			db.execSQL(DB_CREATE_LOCATIONS_TABLE);
			db.execSQL(DB_CREATE_MESSAGES_TABLE);
			Log.d(DEBUG_TAG, "Tables" + " ver." + DB_VERSION + " created");

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.d(DEBUG_TAG, "Database updating...");
			// on upgrade drop older tables
			clear();

			Log.d(DEBUG_TAG, "Tables" + " updated from ver." + oldVersion
					+ " to ver." + newVersion);
			Log.d(DEBUG_TAG, "All data is lost.");
		}

		public void clear() {
			if (db != null) {
				db.execSQL("DROP TABLE IF EXISTS " + DB_ITEM_TABLE);
				db.execSQL("DROP TABLE IF EXISTS " + DB_CONVERSATIONS_TABLE);
				db.execSQL("DROP TABLE IF EXISTS " + DB_LOCATIONS_TABLE);
				db.execSQL("DROP TABLE IF EXISTS " + DB_MESSAGES_TABLE);
				onCreate(db);
			}
		}
	}

	public CatchmeDatabaseAdapter(Context context) {
		this.context = context;
	}

	public CatchmeDatabaseAdapter open() {
		dbHelper = new DatabaseHelper(context, DB_NAME, null, DB_VERSION);
		try {
			db = dbHelper.getWritableDatabase();
		} catch (SQLException e) {
			db = dbHelper.getReadableDatabase();
		}
		return this;
	}

	public void close() {
		if (isOpened()) {
			dbHelper.close();
		}
	}

	public void clear() {
		dbHelper.clear();
	}

	public boolean isOpened() {
		return db != null && db.isOpen();
	}

	public ExampleItem getItem(long itemId) {
		String where = DB_ITEM_ID + "=" + itemId;
		String[] columns = { DB_ITEM_ID, DB_ITEM_NAME, DB_ITEM_SURNAME,
				DB_ITEM_EMAIL, DB_ITEM_STATE, DB_ITEM_SEX, DB_ITEM_BIRDTHDAY,
				DB_AVATAR_SMALL, DB_AVATAR_MEDIUM, DB_AVATAR_BIG, DB_AVATAR_URL };
		Cursor cursor = db.query(DB_ITEM_TABLE, columns, where, null, null,
				null, null);
		ExampleItem result = null;
		if (cursor != null && cursor.moveToFirst()) {
			result = getItemFromCursor(cursor);
		}
		return result;
	}

	private ExampleItem getItemFromCursor(Cursor cursor) {
		long id = cursor.getLong(cursor.getColumnIndex(DB_ITEM_ID));
		String name = cursor.getString(cursor.getColumnIndex(DB_ITEM_NAME));
		String surname = cursor.getString(cursor
				.getColumnIndex(DB_ITEM_SURNAME));
		String email = cursor.getString(cursor.getColumnIndex(DB_ITEM_EMAIL));
		ContactStateType state = ContactStateType.getStateType(cursor
				.getInt(cursor.getColumnIndex(DB_ITEM_STATE)));
		String sex = cursor.getString(cursor.getColumnIndex(DB_ITEM_SEX));
		String dob = cursor.getString(cursor.getColumnIndex(DB_ITEM_BIRDTHDAY));
		LongSparseArray<String> avatars = new LongSparseArray<String>(4);
		avatars.put(ExampleItem.AVATAR_SMALL,
				cursor.getString(cursor.getColumnIndex(DB_AVATAR_SMALL)));
		avatars.put(ExampleItem.AVATAR_MEDIUM,
				cursor.getString(cursor.getColumnIndex(DB_AVATAR_MEDIUM)));
		avatars.put(ExampleItem.AVATAR_BIG,
				cursor.getString(cursor.getColumnIndex(DB_AVATAR_BIG)));
		avatars.put(ExampleItem.AVATAR_URL,
				cursor.getString(cursor.getColumnIndex(DB_AVATAR_URL)));
		List<Long> convIds = getConvId(id);
		return new ExampleItem(id, name, surname, email, state, convIds,
				avatars, sex, dob);
	}

	private List<Long> getConvId(long id) {
		LinkedList<Long> result = new LinkedList<Long>();
		String[] columns = { DB_MESSAGES_ID };
		String where = DB_ITEM_ID + "=" + id;
		Cursor cursor = db.query(DB_CONVERSATIONS_TABLE, columns, where, null,
				null, null, null);
		if (cursor.moveToFirst()) {
			while (cursor.isAfterLast() == false) {
				result.add(cursor.getLong(cursor.getColumnIndex(DB_MESSAGES_ID)));
				cursor.moveToNext();
			}
		}
		return result;
	}

	public ArrayList<ExampleItem> getItems(ContactStateType state) {
		ArrayList<ExampleItem> result = new ArrayList<ExampleItem>();
		String[] columns = { DB_ITEM_ID, DB_ITEM_NAME, DB_ITEM_SURNAME,
				DB_ITEM_EMAIL, DB_ITEM_STATE, DB_ITEM_SEX, DB_ITEM_BIRDTHDAY,
				DB_AVATAR_SMALL, DB_AVATAR_MEDIUM, DB_AVATAR_BIG, DB_AVATAR_URL };
		Cursor cursor = db.query(DB_ITEM_TABLE, columns, null, null, null,
				null, null);
		if (state == null) {

		} else if (state == ContactStateType.ACCEPTED) {

		} else if (state == ContactStateType.SENT) {

		} else if (state == ContactStateType.RECEIVED) {
			// TODO Auto-generated method stub
		}
		if (cursor.moveToFirst()) {
			while (cursor.isAfterLast() == false) {
				ExampleItem item = getItemFromCursor(cursor);
				result.add(item);
				cursor.moveToNext();
			}
		}
		return result;
	}

	public void updateItems(ArrayList<ExampleItem> contactList) {
		for (ExampleItem item : contactList) {
			updateItem(item);
		}
	}

	private boolean updateItem(ExampleItem item) {
		ContentValues itemVal = new ContentValues();
		itemVal.put(DB_ITEM_ID, item.getId());
		itemVal.put(DB_ITEM_NAME, item.getName());
		itemVal.put(DB_ITEM_SURNAME, item.getSurname());
		itemVal.put(DB_ITEM_EMAIL, item.getEmail());
		itemVal.put(DB_ITEM_STATE, item.getState().getIntegerValue());
		itemVal.put(DB_ITEM_SEX, item.getSex().getIntegerValue());
		itemVal.put(DB_ITEM_BIRDTHDAY, item.getBirthday());
		itemVal.put(DB_AVATAR_SMALL, item.getSmallImageUrl());
		itemVal.put(DB_AVATAR_MEDIUM, item.getMediumImageUrl());
		itemVal.put(DB_AVATAR_BIG, item.getLargeImageUrl());
		itemVal.put(DB_AVATAR_URL, item.getOriginalImageURl());
		List<Long> convIds = item.getConversations();
		for (long id : convIds) {
			ContentValues convVal = new ContentValues();
			convVal.put(DB_ITEM_ID, item.getId());
			convVal.put(DB_MESSAGES_ID, id);
			db.insertWithOnConflict(DB_CONVERSATIONS_TABLE, null, convVal,
					SQLiteDatabase.CONFLICT_REPLACE);
		}
		return db.insertWithOnConflict(DB_ITEM_TABLE, null, itemVal,
				SQLiteDatabase.CONFLICT_REPLACE) == -1;
	}

	public void insertMessages(long conversationId, LinkedList<Message> messages) {
		for (Message m : messages) {
			insertMessage(conversationId, m);
		}
	}

	private void insertMessage(long conversationId, Message m) {
		ContentValues messageVal = new ContentValues();
		messageVal.put(DB_MESSAGES_ID, m.getMessageId());
		messageVal.put(DB_MESSAGES_CONTENT, m.getContent());
		messageVal.put(DB_MESSAGES_TIME, m.getTime());
		messageVal.put(DB_CONVERSATIONS_ID, conversationId);
		messageVal.put(DB_MESSAGES_SENDER, m.getSenderId());
		db.insertWithOnConflict(DB_MESSAGES_TABLE, null, messageVal,
				SQLiteDatabase.CONFLICT_IGNORE);
	}

	public List<Message> getMessages(long conversationId) {
		LinkedList<Message> result = new LinkedList<Message>();
		String[] columns = { DB_MESSAGES_ID, DB_CONVERSATIONS_ID,
				DB_MESSAGES_CONTENT, DB_MESSAGES_TIME, DB_MESSAGES_SENDER };
		String where = DB_CONVERSATIONS_ID + "=" + conversationId;
		Cursor cursor = db.query(DB_MESSAGES_TABLE, columns, where, null, null,
				null, null);

		if (cursor.moveToFirst()) {
			while (cursor.isAfterLast() == false) {
				Message item = getMessageFromCursor(cursor);
				result.add(item);
				cursor.moveToNext();
			}
		}
		return result;
	}

	private Message getMessageFromCursor(Cursor cursor) {
		long messageId = cursor.getLong(cursor.getColumnIndex(DB_MESSAGES_ID));
		String content = cursor.getString(cursor
				.getColumnIndex(DB_MESSAGES_CONTENT));
		Date time = null;
		try {
			time = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
					.parse(cursor.getString(cursor
							.getColumnIndex(DB_MESSAGES_TIME)));
		} catch (ParseException e) {
			Log.e("DatabaseError", e.getMessage());
		}
		long senderId = cursor.getLong(cursor
				.getColumnIndex(DB_MESSAGES_SENDER));

		return new Message(messageId, content, time, senderId, null);
	}

	public Message getLastMessage(long conversationId) {
		List<Message> list = getMessages(conversationId);
		if(list.size()>0){
			return list.get(list.size() - 1);
		}else{
			return null;
		}
	}

	public Long getOldestMessageId(long conversationId) {
		// TODO improve, bad solution
		List<Message> list = getMessages(conversationId);
		return list.get(0).getMessageId();
	}

	public Location getLastLocation(long id) {
		//TODO improve
		LinkedList<Location> l = getLocations(id);
		if(l.size()>0){
			return l.get(0);
		}else{
			return null;
		}
	}

	public void updateLocations(LongSparseArray<ArrayList<Location>> locations) {
		for (int i = 0; i < locations.size(); i++) {
			ArrayList<Location> itemLocations = locations.valueAt(i);
			for (Location l : itemLocations) {
				updateItemLocation(locations.keyAt(i), l);
			}
		}

	}

	private boolean updateItemLocation(long itemId, Location itemLocation) {
		ContentValues locationVal = new ContentValues();
		locationVal.put(DB_LOCATIONS_ACCURACY, itemLocation.getAccuracy());
		locationVal.put(DB_ITEM_ID, itemId);
		locationVal.put(DB_LOCATIONS_LATITUDE, itemLocation.getLatitude());
		locationVal.put(DB_LOCATIONS_LONGITUDE, itemLocation.getLongitude());
		locationVal.put(DB_LOCATIONS_TIME, itemLocation.getTime());

		return db.insertWithOnConflict(DB_LOCATIONS_TABLE, null, locationVal,
				SQLiteDatabase.CONFLICT_REPLACE) == -1;
	}

	public LinkedList<Location> getLocations(long itemId) {
		LinkedList<Location> result = new LinkedList<Location>();
		String[] columns = { DB_LOCATIONS_ACCURACY, DB_ITEM_ID,
				DB_LOCATIONS_LATITUDE, DB_LOCATIONS_LONGITUDE,
				DB_LOCATIONS_TIME };
		String where = DB_ITEM_ID + "=" + itemId;
		String orderby = DB_LOCATIONS_TIME+ " DESC";
		Cursor cursor = db.query(DB_LOCATIONS_TABLE, columns, where, null,
				null, null, orderby);

		if (cursor.moveToFirst()) {
			while (cursor.isAfterLast() == false) {
				Location l = getLocationFromCursor(cursor);
				result.add(l);
				cursor.moveToNext();
			}
		}
		return result;
	}

	private Location getLocationFromCursor(Cursor cursor) {
		float accuracy = cursor.getFloat(cursor
				.getColumnIndex(DB_LOCATIONS_ACCURACY));
		double longitude = cursor.getDouble(cursor
				.getColumnIndex(DB_LOCATIONS_LONGITUDE));
		double latitude = cursor.getDouble(cursor
				.getColumnIndex(DB_LOCATIONS_LATITUDE));
		long time = cursor.getLong(cursor.getColumnIndex(DB_LOCATIONS_TIME));
		Location result = new Location(ServerConst.SERVER_NAME);
		result.setAccuracy(accuracy);
		result.setLongitude(longitude);
		result.setLatitude(latitude);
		result.setTime(time);
		return result;
	}
}