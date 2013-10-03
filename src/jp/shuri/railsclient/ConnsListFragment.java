package jp.shuri.railsclient;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class ConnsListFragment extends ListFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
	}

	@Override
	  public void onActivityCreated(Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);

	    String [] strArray = { "hoge", "fuga", "piyo", "fugahoge", "hogefuga",
	    						"abcdefg", "hijklmn", "opqrstu", "vwxyzzz",
	    						"mushroom sause", "food education", 
	    						"charity", "kids sports", "time to chill"};
	    
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
	    		  R.layout.list_item_card, R.id.title);

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
	    
	    listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Toast.makeText(getActivity(), "list item clicked (" + position + ")", Toast.LENGTH_SHORT).show();
			}
	    	
	    });
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//		menu.add(Menu.NONE, R.id.action_add, Menu.NONE, "New Connection").setShowAsAction(
//				MenuItem.SHOW_AS_ACTION_IF_ROOM);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
        	case R.id.action_add:
        		Toast.makeText(getActivity(), "add button from ConnsListFragment", Toast.LENGTH_SHORT).show();
        		return true;
        	default:
        		return super.onOptionsItemSelected(item);
        }
	}
}
