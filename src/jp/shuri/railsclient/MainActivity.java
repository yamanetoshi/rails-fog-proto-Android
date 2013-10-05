package jp.shuri.railsclient;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends Activity 
	implements ConnsListFragment.OnConnectionSelectedListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);
		
		if(null == savedInstanceState){
			Fragment newFragment = new ConnsListFragment();  
			FragmentTransaction transaction = getFragmentManager().beginTransaction();  
		  
			transaction.replace(R.id.container, newFragment);  
			transaction.addToBackStack(null);  
		  
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
