package com.catchme.connections;


public class ServerConst {
	//URLs
	public static final String SERVER_IP ="http://156.17.130.202:3003";
	public static final String URL_AUTH = SERVER_IP + "/api/v1/auth";
	public static final String URL_POSITION_CREATE = SERVER_IP+"/api/v1/position/create";
	public static final String URL_USER_UPDATE = SERVER_IP+"/api/v1/user/update";
	public static final String URL_USER_CREATE = SERVER_IP+"/api/v1/user/create";
	public static final String URL_USER_UPDATE_AVATAR = SERVER_IP+"/api/v1/user/upload_avatar";
	public static final String URL_USER_LOGOUT = SERVER_IP+"/api/v1/auth/";//+ userID
	public static final String URL_CONTACTS_CREATE = SERVER_IP+"/api/v1/contact/create";
	public static final String URL_CONTACTS_ALL = SERVER_IP+ "/api/v1/contacts/all";
	public static final String URL_CONTACTS_SENT = SERVER_IP+ "/api/v1/contacts/invited_by_user";
	public static final String URL_CONTACTS_RECEIVED = SERVER_IP+ "/api/v1/contacts/invited_by_others";
	public static final String URL_CONTACTS_UPDATE_STATE_PART1 = SERVER_IP+"/api/v1/contact/";
	public static final String URL_CONTACTS_UPDATE_STATE_PART2 = "/update";
	public static final String URL_MESSAGES_SEND = SERVER_IP+"/api/v1/conversations/create_message";
	public static final String URL_MESSAGES_GET_PART1 = SERVER_IP+"/api/v1/conversations/";
	public static final String URL_MESSAGES_GET_PART2_TYPE_NEWER = "/messages/recent/";
	public static final String URL_MESSAGES_GET_PART2_TYPE_OLDER = "/messages/older/";
	public static final String URL_MESSAGES_GET_PART2_TYPE_INIT = "/messages/init";
	public static final String URL_POSITIONS_GET = SERVER_IP+"/api/v1/position/list";
	//People stuff
	public static final String USER_EMAIL = "email";
	public static final String USER_PASSWORD = "password";
	public static final String USER_PASSWORD_CONFIRMATION = "TODOTODO";
	public static final String USER = "user";
	public static final String USER_NAME = "name";
	public static final String USER_SURNAME = "surname";
	public static final String USER_ID = "id";
	public static final String USER_STATE = "state";
	public static final String CONTACTS = "contacts";
	public static final String USER_CONVERSATIONS = "conversation_ids";
	
	//Connection stuff
	public static final String TOKEN_GET = "Auth-Token";
	public static final String TOKEN_RESPONSE = "token";
	public static final String SUCCESS = "success";
	public static final String SUCCESS_TRUE = "true";
	public static final String SUCCESS_FALSE = "false";
	public static final String ERROR_MESSAGES = "messages";
	
	//Positions stuff
	public static final String POSITION = "user_position";
	public static final String POSITION_LATITUDE = "latitude";
	public static final String POSITION_LONGITUDE = "longitude";
	public static final String POSITION_KEY = "for_positions";
	public static final String POSITION_CONTACTS = "contact_ids";
	public static final String POSITION_NUMBER = "number";
	
	//Messages stuff
	public static final String MESSAGE = "message";
	public static final String MESSAGES = "messages";
	public static final String MESSAGE_CONVERSATION_ID = "conversation_id";
	public static final String MESSAGE_CONTENT = "content";
	public static final String MESSAGE_CONTAINER = "chat_data";
	public static final String MESSAGE_READ_FEEDS = "read_feeds";
	public static final String MESSAGE_CREATED_AT= "created_at";
	public static final String MESSAGE_LAST_ID = "?last_message_id=";
	public static final String MESSAGE_ID = "id";
	public static final String USER_AVATAR = "avatar";
	public static final String USER_AVATAR_SMALL = "small";
	public static final String USER_AVATAR_MEDIUM = "medium";
	public static final String USER_AVATAR_BIG = "big";
	public static final String USER_AVATAR_URL = "url";
	public static final String POSITION_ACCURACY = "accuracy";
	public static final String POSITION_FIX_TIME = "fix_time";
	
	
}
