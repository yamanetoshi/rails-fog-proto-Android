package jp.shuri.railsclient;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class AppListFragment extends ListFragment {
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
	}
}
