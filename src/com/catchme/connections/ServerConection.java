package com.catchme.connections;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class ServerConection {

	public static String GET(String url) {
		InputStream inputStream = null;

		HttpClient httpclient = new DefaultHttpClient();
		try {
			HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
			inputStream = httpResponse.getEntity().getContent();
			return convertInputStreamToString(inputStream);
		} catch (Exception e) {
			Log.e("ConnectionError", e.getMessage());
		}

		return null;
	}

	public static String POST(String url, Map<String, String> params) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);
		InputStream inputStream = null;
		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			for (String name : params.keySet()) {
				nameValuePairs.add(new BasicNameValuePair(name, params
						.get(name)));
			}
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpResponse httpResponse = httpclient.execute(httppost);
			inputStream = httpResponse.getEntity().getContent();
			return convertInputStreamToString(inputStream);
		} catch (Exception e) {

			Log.e("ConnectionError", e.getMessage());
		}
		return null;
	}

	private static String convertInputStreamToString(InputStream in)
			throws IOException {
		InputStreamReader is = new InputStreamReader(in);
		StringBuilder sb = new StringBuilder();
		BufferedReader br = new BufferedReader(is);
		String read = br.readLine();

		while (read != null) {
			sb.append(read);
			read = br.readLine();
		}

		return sb.toString();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static JSONObject JsonPOST(String url, JSONObject data) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpPost httpost = new HttpPost(url);
		try {
			StringEntity se = new StringEntity(data.toString());
			httpost.setEntity(se);
			httpost.setHeader("Accept", "application/json");
			httpost.setHeader("Content-type", "application/json");
			ResponseHandler responseHandler = new BasicResponseHandler();
			return httpclient.execute(httpost, responseHandler);
		} catch (Exception e) {
			Log.e("ConnectionError",e.getMessage());
		}
		return null;
	}

	public static boolean isOnline(Context c) {
		ConnectivityManager cm = (ConnectivityManager) c
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		return netInfo != null && netInfo.isConnectedOrConnecting();
	}
}
