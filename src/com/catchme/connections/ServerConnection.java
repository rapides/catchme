package com.catchme.connections;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
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
import android.util.Base64;
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
			System.out.println("Response: "+response);
			result = new JSONObject(response);
		}catch(Exception e){
			e.printStackTrace();
		}
		/*} catch (UnsupportedEncodingException e) {
			Log.e("JSON Parse error", e.getMessage());
		} catch (ClientProtocolException e) {
			Log.e("ConnectionError", e.getMessage());
		} catch (IOException e) {
			Log.e("ConnectionError", e.getMessage());
		} catch (ParseException e) {
			Log.e("JSON Parse error", e.getMessage());
		} catch (JSONException e) {
			Log.e("JSON Parse error", e.getMessage());
		}*/

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
			Log.e("ConnectionError", responseGet + "\n" + e.getMessage());
			// e.printStackTrace();
		}
		return new JSONObject();
	}

	public static JSONObject DELETE(String url, Map<String, String> header) {
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
			// System.out.println("\nHeader: ");
			/*
			 * for (Header h : delete.getAllHeaders()) { System.out.println(h);
			 * }
			 */
			responseDelete = client.execute(delete);
			return new JSONObject(EntityUtils.toString(responseDelete
					.getEntity()));
		} catch (Exception e) {
			Log.e("ConnectionError", responseDelete + "\n" + e.getMessage());
		}
		return null;
	}

	public static JSONObject uploadImage(String url, String filePath,
			Map<String, String> header) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost postMethod = new HttpPost(url);

		JSONObject result = new JSONObject();
		try {
			JSONObject jsonObject = constructPictureJson(filePath);
			postMethod.setEntity(new StringEntity(jsonObject.toString()));

			if (header != null) {
				for (String s : header.keySet()) {
					postMethod.setHeader(s, header.get(s));
				}
			}
			String response;
			HttpResponse responsePost = httpClient.execute(postMethod);
			response = EntityUtils.toString(responsePost.getEntity());
			result = new JSONObject(response);
		} catch (HttpResponseException error) {
			error.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}

	private static JSONObject constructPictureJson(String fileName)
			throws JSONException, IOException {

		String[] file = fileName.split("/");// TODO check what happens if
											// platform is android
		JSONObject request = new JSONObject();
		JSONObject user = new JSONObject();
		JSONObject avatar = new JSONObject();
		/*avatar.put("user_id", "1");
		avatar.put("folder_id", "1"); 
        avatar.put("picture_name", "picture name");
        avatar.put("picture_description", "1"); 
        avatar.put("content_type", "jpg");
        avatar.put("original_filename", "base64:"+file[file.length-1]);*/
		avatar.put("filename", file[file.length-1]);
		avatar.put("data", encodePicture(fileName));
		user.put("avatar", avatar);
		request.put("user", user);
		return request;
	}

	private static String encodePicture(String fileName) throws IOException {
		File picture = new File(fileName);
		return Base64.encodeToString(FileUtils.readFileToByteArray(picture),Base64.DEFAULT);
	}
}
