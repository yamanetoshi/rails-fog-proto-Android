package jp.shuri.railsclient;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);
		
		Fragment newFragment = new ConnsListFragment();  
		FragmentTransaction transaction = getFragmentManager().beginTransaction();  
		  
		transaction.replace(R.id.container, newFragment);  
		transaction.addToBackStack(null);  
		  
		transaction.commit(); 
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
                    case android.R.id.home:
                            finish();
                            return true;
                    case R.id.action_settings:
                            Toast.makeText(this, "setting button", Toast.LENGTH_SHORT).show();
//                            Intent i = new Intent(this, SCPreferences.class);
//                            startActivity(i);
                            return true;
                    default:
                            return super.onOptionsItemSelected(item);
            }
    }

}
