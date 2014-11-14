package com.catchme.connections;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
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
			// return new
			// JSONObject(EntityUtils.toString(responsePost.getEntity()));

			// String response = httpclient.execute(httpost, responseHandler);
			result = new JSONObject(response);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			System.out.println(response);
			Log.e("ConnectionError", response);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
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
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(url);

			if (header != null) {
				for (String s : header.keySet()) {
					get.setHeader(s, header.get(s));
				}
			}
			HttpResponse responseGet = client.execute(get);
			return new JSONObject(EntityUtils.toString(responseGet.getEntity()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
