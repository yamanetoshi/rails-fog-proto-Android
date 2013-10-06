package jp.shuri.android.railsclient;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class ShurijpApplication extends Application {
	
	private String mAuthToken = "";
	protected String getAuthToken() { return mAuthToken; }
	protected void clearAuthToken() { mAuthToken = ""; }
	
	private String mURL = "http://cryptic-eyrie-8923.herokuapp.com";
	protected String getURL() { return mURL; }
	
	private SharedPreferences mPref;
	
	protected boolean setAuthToken() throws Exception {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("email", mPref.getString("login_account", "")));
		params.add(new BasicNameValuePair("password", mPref.getString("login_password", "")));

		String url = mURL + "/api/users/sign_in";

		String str = JSONFunctions.POSTfromURL(url, new DefaultHttpClient(), params);
		JSONObject obj = new JSONObject(str);
		if (obj.getBoolean("success"))
			mAuthToken = obj.getString("auth_token");
		
		return obj.getBoolean("success");
	}

	@Override
	public void onCreate() {
		super.onCreate();
		
		mPref = PreferenceManager.getDefaultSharedPreferences(this);
	}

}
