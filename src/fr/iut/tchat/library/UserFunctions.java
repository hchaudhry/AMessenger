package fr.iut.tchat.library;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class UserFunctions {

	private JSONParser jsonParser;

	// Testing in localhost using wamp or xampp
	// use http://10.0.2.2/ to connect to your localhost ie http://localhost/
	private static String loginURL = "http://hussamandroid.net23.net/android_api/";
	private static String registerURL = "http://hussamandroid.net23.net/android_api/";
	private static String passwordResetURL = "http://hussamandroid.net23.net/android_api/";

	private static String login_tag = "login";
	private static String register_tag = "register";
	private static String password_reset = "passwordReset";

	// constructor
	public UserFunctions() {
		jsonParser = new JSONParser();
	}

	/**
	 * function make Login Request
	 * 
	 * @param email
	 * @param password
	 * */
	public JSONObject loginUser(String email, String password) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", login_tag));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("password", password));
		JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
		// return json
//		 Log.e("JSON", json.toString());
		return json;
	}
	
	/**
	 * function make password reset
	 * 
	 * @param email
	 */
	
	public JSONObject passwordReset(String email){
		// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("tag", password_reset));
			params.add(new BasicNameValuePair("email", email));
			JSONObject json = jsonParser.getJSONFromUrl(passwordResetURL, params);
			// return json
//				 Log.e("JSON", json.toString());
			return json;
	}

	/**
	 * function make Login Request
	 * 
	 * @param lastName
	 * @param firstName
	 * @param pseudo
	 * @param email
	 * @param password
	 * @param sex
	 * */
	public JSONObject registerUser(String sex, String lastName, String firstName, String pseudo, String email, String password) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", register_tag));
		params.add(new BasicNameValuePair("sex", sex));
		params.add(new BasicNameValuePair("lastName", lastName));
		params.add(new BasicNameValuePair("firstName", firstName));
		params.add(new BasicNameValuePair("pseudo", pseudo));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("password", password));

		// getting JSON Object
		JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
		// return json
		return json;
	}

	/**
	 * Function get Login status
	 * */
	public boolean isUserLoggedIn(Context context) {
		DatabaseHandler db = new DatabaseHandler(context);
		int count = db.getRowCount();
		if (count > 0) {
			// user logged in
			return true;
		}
		return false;
	}

	/**
	 * Function to logout user Reset Database
	 * */
	public boolean logoutUser(Context context) {
		DatabaseHandler db = new DatabaseHandler(context);
		db.resetTables();
		return true;
	}

}