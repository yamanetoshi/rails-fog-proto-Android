package jp.shuri.android.railsclient;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

import android.view.KeyEvent;
import android.view.MenuItem;

public class PActivity extends PreferenceActivity {

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction()==KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
            	case KeyEvent.KEYCODE_BACK:
                	Intent i = new Intent(this, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);

                	return true;
            }
        }
		return super.dispatchKeyEvent(event);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		MyPreferenceFragment obj = new MyPreferenceFragment();
		
		transaction.replace(android.R.id.content, obj);  
		  
		transaction.commit(); 		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
        case android.R.id.home:
        	Intent i = new Intent(this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);

        	return true;	
        default:
            return super.onOptionsItemSelected(item);
        }
    }
        
	public static class MyPreferenceFragment extends PreferenceFragment
    {

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			ActionBar actionBar = getActivity().getActionBar();
	        actionBar.setHomeButtonEnabled(true);
	        actionBar.setDisplayHomeAsUpEnabled(true);
			
			addPreferencesFromResource(R.xml.app_preferences);
		}

    }

}
