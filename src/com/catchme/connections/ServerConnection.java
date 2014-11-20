package com.catchme.connections;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class ServerConnection {

	public static JSONObject JsonPOST(String url, JSONObject data,
			Map<String, String> header) {
		JSONObject result = new JSONObject();
		HttpParams httpParameters = new BasicHttpParams();
		int timeoutConnection = 3000;
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				timeoutConnection);
		int timeoutSocket = 5000;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
		HttpPost httpost = new HttpPost(url);

		HttpResponse responsePost = null;
		String response = "";
		try {
			if (data != null) {
				StringEntity se = new StringEntity(data.toString(), "UTF-8");
				httpost.setEntity(se);
			}

			if (header != null) {
				for (String s : header.keySet()) {
					httpost.setHeader(s, header.get(s));
				}
			}
			responsePost = httpclient.execute(httpost);
			response = EntityUtils.toString(responsePost.getEntity());
			result = new JSONObject(response);
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			Log.e("ConnectionError", "Error: "+response);
		} catch (IOException e) {
			System.out.println(e.getMessage());
			Log.e("ConnectionError", "Error: "+e.getMessage());
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			//Log.e("JSON Parse error", e.getMessage());
		}

		return result;
	}

	public static boolean isOnline(Context c) {
		ConnectivityManager cm = (ConnectivityManager) c
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		return netInfo != null && netInfo.isConnectedOrConnecting();
	}

	public static JSONObject GET(String url, Map<String, String> header) {
		HttpResponse responseGet = null;
		try {
			HttpParams httpParameters = new BasicHttpParams();
			int timeoutConnection = 3000;
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					timeoutConnection);
			int timeoutSocket = 5000;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			HttpClient client = new DefaultHttpClient(httpParameters);
			HttpGet get = new HttpGet(url);
			
			if (header != null) {
				for (String s : header.keySet()) {
					get.setHeader(s, header.get(s));
				}
			}
			responseGet = client.execute(get);
			return new JSONObject(EntityUtils.toString(responseGet.getEntity()));
		} catch (Exception e) {
			Log.e("ConnectionError", responseGet+"\n"+e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public static JSONObject DELETE(String url, Map<String, String> header ){
		HttpResponse responseDelete = null;
		try {
			HttpParams httpParameters = new BasicHttpParams();
			int timeoutConnection = 3000;
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					timeoutConnection);
			int timeoutSocket = 5000;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			HttpClient client = new DefaultHttpClient(httpParameters);
			HttpDelete delete = new HttpDelete(url);
			
			if (header != null) {
				for (String s : header.keySet()) {
					delete.setHeader(s, header.get(s));
				}
			}
			System.out.println("\nHeader: ");
			for(Header h: delete.getAllHeaders()){
				System.out.println(h);
			}
			System.out.println();
			responseDelete = client.execute(delete);
			return new JSONObject(EntityUtils.toString(responseDelete.getEntity()));
		} catch (Exception e) {
			//Log.e("ConnectionError", responseDelete+"\n"+e.getMessage());
			//e.printStackTrace();
		}
		return null;
	}

}
