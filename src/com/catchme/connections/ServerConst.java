package com.catchme.connections;

public class ServerConst {
	//URLs
	public static final String SERVER_IP = "http://192.168.1.100:3000";
	public static final String URL_AUTH = SERVER_IP + "/api/v1/auth";
	public static final String URL_POSITION_CREATE = SERVER_IP+"/api/v1/position/create/";
	public static final String URL_USER_UPDATE = SERVER_IP+"/api/v1/user/update";
	public static final String URL_USER_CREATE = SERVER_IP+"/api/v1/user/create";
	public static final String URL_USER_LOGOUT = SERVER_IP+"/api/v1/user/";//po sleszu id usera
	public static final String URL_CONTACTS_CREATE = SERVER_IP+"/api/v1/contact/create";
	public static final String URL_CONTACTS_ALL = SERVER_IP+ "/api/v1/contacts/all";
	public static final String URL_CONTACTS_SENT = SERVER_IP+ "/api/v1/contacts/invited_by_user";//
	public static final String URL_CONTACTS_RECEIVED = SERVER_IP+ "/api/v1/contacts/invited_by_others";
	public static final String URL_MESSAGES = SERVER_IP+"/api/v1/messages/create";
	
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
	
	//Messages stuff
	public static final String MESSAGE = "message";
	public static final String MESSAGE_ID = "conversation_id";
	public static final String MESSAGE_CONTENT = "content";
	
}
