package jp.shuri.railsclient;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ConnsListFragment extends ListFragment {
	
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

	    String [] strArray = { "hoge", "fuga", "piyo", "fugahoge", "hogefuga",
	    						"abcdefg", "hijklmn", "opqrstu", "vwxyzzz",
	    						"mushroom sause", "food education", 
	    						"charity", "kids sports", "time to chill"};
	    
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
	    		  R.layout.list_item_card, R.id.conns_title);

	    for (String str : strArray) {
	        adapter.add(str);
	    }
	    
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
	    
	    setListAdapter(adapter);
	    
	    getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				mListener.onConnectionSelected(position);
//				Toast.makeText(getActivity(), "list item clicked (" + position + ")", Toast.LENGTH_SHORT).show();
			}
	    	
	    });
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
