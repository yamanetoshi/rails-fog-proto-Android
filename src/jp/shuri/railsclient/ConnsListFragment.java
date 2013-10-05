package jp.shuri.railsclient;

import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
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
	
	private ArrayAdapter<String> mAdapter;
	private Handler mHandler = new Handler();
	
    public interface OnConnectionSelectedListener {  
        public void onConnectionSelected(int position);
    }  
    
    OnConnectionSelectedListener mListener;  

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
	}

	@Override
	  public void onActivityCreated(Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);
	    
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
				
//				Toast.makeText(getParent(), "Conns list item clicked (" + id + ")", Toast.LENGTH_SHORT).show();
				Fragment newFragment = new VMListFragment();
				
				Bundle args = new Bundle();
		        args.putInt("pos", (int)id);
		        newFragment.setArguments(args);
		        
				FragmentTransaction transaction = getFragmentManager().beginTransaction();  
				  
				transaction.replace(R.id.container, newFragment);  
				transaction.addToBackStack(null);  
				  
				transaction.commit(); 		

//				mListener.onConnectionSelected(position);
//				Toast.makeText(getActivity(), "list item clicked (" + position + ")", Toast.LENGTH_SHORT).show();
			}
	    	
	    });
	    
	    if (mJsonArray == null) {
	    	setConnsListAdapter();
	    } else {
	    	setListAdapter(mAdapter);
	    }
	}
	
	private void setConnsListAdapter() {
		/*
           String [] strArray = { "hoge", "fuga", "piyo", "fugahoge", "hogefuga",
                                                       "abcdefg", "hijklmn", "opqrstu", "vwxyzzz",
                                                       "mushroom sause", "food education", 
                                                       "charity", "kids sports", "time to chill"};
           
           ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                         R.layout.list_item_card, R.id.conns_title);

           for (String str : strArray) {
               adapter.add(str);
           }
           
           setListAdapter(adapter);
		 */
		
		FragmentManager manager = getFragmentManager();  
        final MyProgressDialog pDialog = new MyProgressDialog();  
        pDialog.show(manager, "dialog");  
        
        new Thread(new Runnable() {

			@Override
			public void run() {
				if (getParent().getAuthToken().equals("")) {
					getParent().setAuthToken();
				}
				
				String url = getParent().getURL() + 
						"/conns.json?auth_token=" + 
						getParent().getAuthToken();
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
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
        try {  
            mListener = (OnConnectionSelectedListener) activity;  
        } catch (ClassCastException e) {  
            throw new ClassCastException(activity.toString() + " must implement OnConnectionSelectedListener");  
        }  

	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		setListAdapter(null);
	}
}
