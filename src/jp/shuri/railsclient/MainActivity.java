package jp.shuri.railsclient;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends Activity 
	implements ConnsListFragment.OnConnectionSelectedListener {
	
	private SharedPreferences mPref;
	protected SharedPreferences getPref() { return mPref; }
	
	private String mAuthToken;
	protected String getAuthToken() { return mAuthToken; }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		mPref = PreferenceManager.getDefaultSharedPreferences(this);
		
		ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);
		
		if(null == savedInstanceState){
			Fragment newFragment = new ConnsListFragment();  
			FragmentTransaction transaction = getFragmentManager().beginTransaction();  
		  
			transaction.replace(R.id.container, newFragment);  
		  
			transaction.commit();
			
			loginProc();
		}
	}
	
	private void loginProc() {
		FragmentManager manager = getFragmentManager();  
        final MyProgressDialog pDialog = new MyProgressDialog();  
        pDialog.show(manager, "dialog");  
        
        new Thread(new Runnable() {

			@Override
			public void run() {
				ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("email", mPref.getString("login_account", "")));
                params.add(new BasicNameValuePair("password", mPref.getString("login_password", "")));
                
                String url = "http://cryptic-eyrie-8923.herokuapp.com/api/users/sign_in";
                
                try {
                	String str = JSONFunctions.POSTfromURL(url, new DefaultHttpClient(), params);
                	JSONObject obj = new JSONObject(str);
                	mAuthToken = obj.getString("auth_token");
                	pDialog.dismiss();
                } catch (Exception e) {
                	e.printStackTrace();

            		FragmentManager manager = getFragmentManager();  
                    final MyExceptionDialog dialog = new MyExceptionDialog();  
                    dialog.show(manager, "dialog");                  	
                }
			}
        }).start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            switch(item.getItemId()) {
            	case R.id.action_settings:
//            		Toast.makeText(this, "auth_token:" + mAuthToken, Toast.LENGTH_SHORT).show();
            		
            		Intent i = new Intent(this, PActivity.class);
            		startActivity(i);
            		return true;
                default:
                	return super.onOptionsItemSelected(item);
            }
    }

	@Override
	public void onConnectionSelected(int position) {
		Toast.makeText(this, "Conns list item clicked (" + position + ")", Toast.LENGTH_SHORT).show();
		Fragment newFragment = new VMListFragment();  
		FragmentTransaction transaction = getFragmentManager().beginTransaction();  
		  
		transaction.replace(R.id.container, newFragment);  
		transaction.addToBackStack(null);  
		  
		transaction.commit(); 		
	}

}
