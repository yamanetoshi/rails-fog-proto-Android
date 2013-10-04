package jp.shuri.railsclient;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.content.DialogInterface;
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

public class VMListFragment extends ListFragment {
	private final int RELOAD_ID = 0xdeadbeef;
	private final int ADD_ID = 0xdeadbeef + 1;
	
    public static class AddDialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
                AlertDialog.Builder Builder = new AlertDialog.Builder(getActivity());
                Builder.setTitle("Add New Item");
                Builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                	public void onClick(DialogInterface dialog, int whichButton) {
                		dialog.dismiss();
                	}
                });
                Builder.setCancelable(true);

                return Builder.create();
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
                super.onActivityCreated(savedInstanceState);
                getDialog().setCanceledOnTouchOutside(false);
        }
    }
	
    public static class ModifyDialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
                AlertDialog.Builder Builder = new AlertDialog.Builder(getActivity());
                Builder.setTitle("Start/Stop VM");
                Builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                	public void onClick(DialogInterface dialog, int whichButton) {
                		dialog.dismiss();
                	}
                });
                Builder.setCancelable(true);

                return Builder.create();
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
                super.onActivityCreated(savedInstanceState);
                getDialog().setCanceledOnTouchOutside(false);
        }
    }
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
	}

	@Override
	  public void onActivityCreated(Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);

	    ActionBar actionBar = getActivity().getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

	    String [] strArray = { "hoge", "fuga", "piyo", "fugahoge", "hogefuga",
	    						"abcdefg", "hijklmn", "opqrstu", "vwxyzzz",
	    						"mushroom sause", "food education", 
	    						"charity", "kids sports", "time to chill"};
	    
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
	    		  R.layout.vm_list_item_card, R.id.vm_title);

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
                FragmentManager manager = getFragmentManager();  
                ModifyDialog alertDialog = new ModifyDialog();
                alertDialog.show(manager, "dialog");
			}
	    	
	    });
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.add(Menu.NONE, ADD_ID, Menu.NONE, "New Connection")
			.setIcon(android.R.drawable.ic_menu_add)
			.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		menu.add(Menu.NONE, RELOAD_ID, Menu.NONE, "Reload")
			.setIcon(android.R.drawable.ic_menu_rotate)
			.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
        	case android.R.id.home:
        		getFragmentManager().popBackStack();
        		return true;
        	case ADD_ID:
                FragmentManager manager = getFragmentManager();  
                AddDialog alertDialog = new AddDialog();
                alertDialog.show(manager, "dialog");

                return true;
        	case RELOAD_ID:
        		Toast.makeText(getActivity(), "reload button from VMListFragment", Toast.LENGTH_SHORT).show();
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
