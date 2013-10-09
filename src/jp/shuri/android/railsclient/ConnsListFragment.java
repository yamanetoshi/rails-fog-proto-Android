package jp.shuri.android.railsclient;

import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ConnsListFragment extends ListFragment {

	private JSONArray mJsonArray = null;
	protected JSONArray getJSONArray() { return mJsonArray; }
	
	private MainActivity getParent() { return ((MainActivity)getActivity()); }
	private ShurijpApplication getMyApp() { return ((ShurijpApplication)getActivity().getApplication()); }
	
	private ArrayAdapter<String> mAdapter;
	private Handler mHandler = new Handler();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
	}

	@Override
	  public void onActivityCreated(Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);
		
		if (getParent().getPref().getString("login_account", "").equals("")) {
			Toast.makeText(getParent(), "アカウント未設定です", Toast.LENGTH_LONG).show();
			Intent i = new Intent(getParent(), PActivity.class);
			startActivity(i);
			return;
		}
	    
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
	    
	    int padding = (int) (getResources().getDisplayMetrics().density * 8); // 8dip
	    ListView listView = getListView();
	    listView.setPadding(padding, 0, padding, 0);
	    listView.setScrollBarStyle(ListView.SCROLLBARS_OUTSIDE_OVERLAY);
	    listView.setDivider(null);
	    
	    LayoutInflater inflater = LayoutInflater.from(getActivity());
	    View header = inflater.inflate(R.layout.list_header_footer, listView, false);
	    View footer = inflater.inflate(R.layout.list_header_footer, listView, false);
	    listView.addHeaderView(header, null, false);
	    listView.addFooterView(footer, null, false);
	    
	    listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Fragment newFragment = new VMListFragment();
				
				Bundle args = new Bundle();
		        args.putInt("pos", (int)id);
		        newFragment.setArguments(args);
		        
				FragmentTransaction transaction = getFragmentManager().beginTransaction();  
				  
				transaction.replace(R.id.container, newFragment);  
				transaction.addToBackStack(null);  
				  
				transaction.commit(); 		
			}
	    	
	    });
	    
	    if (mJsonArray == null) {
	    	setConnsListAdapter();
	    } else {
	    	setListAdapter(mAdapter);
	    }
	}
	
	private void setConnsListAdapter() {

        new Thread(new Runnable() {

			@Override
			public void run() {
				if (getMyApp().getAuthToken().equals("")) {
					try {
						if (!getMyApp().setAuthToken()) {
							Intent i = new Intent(getActivity(), PActivity.class);
							startActivity(i);
							return;
						}
					} catch (Exception e) {
	                	e.printStackTrace();

	            		FragmentManager manager = getFragmentManager();  
	                    final MyExceptionDialog dialog = new MyExceptionDialog();  
	                    dialog.show(manager, "dialog");                  	
					}
				}
				
				String url = getMyApp().getURL() + 
						"/conns.json?auth_token=" + 
						getMyApp().getAuthToken();
                try {
                	String str = JSONFunctions.GETfromURL(url, new DefaultHttpClient());
                	mJsonArray = new JSONArray(str);

            	    
            	    mAdapter = new ArrayAdapter<String>(getActivity(),
            	    		  R.layout.list_item_card, R.id.conns_title);

                	for (int i = 0; i < mJsonArray.length(); i++) {
                		JSONObject e = mJsonArray.getJSONObject(i);
                		mAdapter.add(e.getString("name"));
                	}

                	mHandler.post(new Runnable() {

						@Override
						public void run() {
		                	setListAdapter(mAdapter);
						}
                		
                	});
        			
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
	public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
        	case android.R.id.home:
        		getActivity().finish();
        		return true;
        	default:
        		return super.onOptionsItemSelected(item);
        }
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		setListAdapter(null);
	}
}
