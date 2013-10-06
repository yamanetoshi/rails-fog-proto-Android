package jp.shuri.android.railsclient;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.ActionBar;
import android.app.Activity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {
	
	private SharedPreferences mPref;
	protected SharedPreferences getPref() { return mPref; }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		mPref = PreferenceManager.getDefaultSharedPreferences(this);
		
		ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);
		
		if(null == savedInstanceState){
			FragmentTransaction transaction = getFragmentManager().beginTransaction();
			transaction.replace(R.id.container, new ConnsListFragment());  
			transaction.commit();
		}
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
            		Intent i = new Intent(this, PActivity.class);
            		startActivity(i);
            		return true;
                default:
                	return super.onOptionsItemSelected(item);
            }
    }
}
