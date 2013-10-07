package jp.shuri.android.railsclient;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
	private Handler mHandler = new Handler();
	private int mIndex;
	
	ArrayAdapter<String> mAdapter;
	
	private MainActivity getParent() { return ((MainActivity)getActivity()); }
	private ShurijpApplication getMyApp() { return ((ShurijpApplication)getActivity().getApplication()); }

	private JSONObject mConn = null;
	private JSONObject mVMs = null;
	
	private final int RELOAD_ID = 0xdeadbeef;
	private final int ADD_ID = 0xdeadbeef + 1;
	
    public class AddDialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
                AlertDialog.Builder Builder = new AlertDialog.Builder(getActivity());
                Builder.setTitle("Add New Item");
                Builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                	public void onClick(DialogInterface dialog, int whichButton) {
                		dialog.dismiss();
                		kickoff(true);
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
    
    private void kickoff(final boolean isNew) {
		FragmentManager manager = getFragmentManager();  
        final MyProgressDialog pDialog = new MyProgressDialog();
        pDialog.show(manager, "dialog");  
        
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
				
                try {
                	if (isNew) {
                        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("hostname", ""));

        				String url = getMyApp().getURL() + 
        						"/vm_operations?auth_token=" + 
        						getMyApp().getAuthToken();
                		String str = JSONFunctions.POSTfromURL(url, new DefaultHttpClient(), params);
        				pDialog.dismiss();
                	} else {
                		
                	}
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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
	}

	@Override
	  public void onActivityCreated(Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);
	    
	    Bundle args = getArguments();
        mIndex = args.getInt("pos");
        Toast.makeText(getActivity(), "selected : " + mIndex, Toast.LENGTH_SHORT).show();

	    ActionBar actionBar = getActivity().getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
	    
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
	    
	    getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
                FragmentManager manager = getFragmentManager();  
                ModifyDialog alertDialog = new ModifyDialog();
                alertDialog.show(manager, "dialog");
			}
	    	
	    });
	    
	    setVMsListAdapter();
	}
	
	private void setVMsListAdapter() {
		FragmentManager manager = getFragmentManager();  
        final MyProgressDialog pDialog = new MyProgressDialog();  
        pDialog.show(manager, "dialog");  
        
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
						"/vm_operations/" + mIndex + ".json?auth_token=" + 
						getMyApp().getAuthToken();
                try {
                	String str = JSONFunctions.GETfromURL(url, new DefaultHttpClient());
                	JSONObject obj = new JSONObject(str);
                	mConn = obj.getJSONObject("conn");
                	mVMs = obj.getJSONObject("vms");
                	JSONArray tmpArray = mVMs.getJSONObject("listvirtualmachinesresponse")
                							.getJSONArray("virtualmachine");
            	    
            	    mAdapter = new ArrayAdapter<String>(getActivity(),
            	    		  R.layout.vm_list_item_card, R.id.vm_title);

                	for (int i = 0; i < tmpArray.length(); i++) {
                		JSONObject e = tmpArray.getJSONObject(i);
                		mAdapter.add(e.getString("displayname"));
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
        		setListAdapter(null);
        		setVMsListAdapter();
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
