package com.catchme.connections;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ServerConection {

	public static String GET(String url) {
		InputStream inputStream = null;

		HttpClient httpclient = new DefaultHttpClient();
		try {
			HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
			inputStream = httpResponse.getEntity().getContent();
			return convertInputStreamToString(inputStream);
		} catch (Exception e) {
			System.out.println(e);
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
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
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
	public static String JsonPOST(String url, HashMap<String, HashMap<String, String>> data) {
		DefaultHttpClient httpclient = new DefaultHttpClient();

		// url with the post data
		HttpPost httpost = new HttpPost(url);
		try {
			// convert parameters into JSON object
			JSONObject holder = getJsonObjectFromMap(data);

			// passes the results to a string builder/entity
			StringEntity se = new StringEntity(holder.toString());

			// sets the post request as the resulting string
			httpost.setEntity(se);
			// sets a request header so the page receving the request
			// will know what to do with it
			httpost.setHeader("Accept", "application/json");
			httpost.setHeader("Content-type", "application/json");

			// Handles what is returned from the page
			ResponseHandler responseHandler = new BasicResponseHandler();

			return httpclient.execute(httpost, responseHandler);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	private static JSONObject getJsonObjectFromMap(Map params)
			throws JSONException {

		// all the passed parameters from the post request
		// iterator used to loop through all the parameters
		// passed in the post request
		Iterator iter = params.entrySet().iterator();

		// Stores JSON
		JSONObject holder = new JSONObject();

		// using the earlier example your first entry would get email
		// and the inner while would get the value which would be 'foo@bar.com'
		// { fan: { email : 'foo@bar.com' } }

		// While there is another entry
		while (iter.hasNext()) {
			// gets an entry in the params
			Map.Entry pairs = (Map.Entry) iter.next();

			// creates a key for Map
			String key = (String) pairs.getKey();

			// Create a new map
			Map m = (Map) pairs.getValue();

			// object for storing Json
			JSONObject data = new JSONObject();

			// gets the value
			Iterator iter2 = m.entrySet().iterator();
			while (iter2.hasNext()) {
				Map.Entry pairs2 = (Map.Entry) iter2.next();
				data.put((String) pairs2.getKey(), (String) pairs2.getValue());
			}

			// puts email and 'foo@bar.com' together in map
			holder.put(key, data);
		}
		return holder;
	}
	public static boolean isOnline(Context c) {
	    ConnectivityManager cm =
	        (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    return netInfo != null && netInfo.isConnectedOrConnecting();
	}
}
