package com.steps.activities;

import android.app.FragmentManager;
import android.app.ListFragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.steps.managers.MyCursorAdapter;
import com.steps.managers.SessionManager;
import com.steps.networking.Contract;
import com.steps.networking.ErrorCode;
import com.steps.networking.ServerAPI;
import com.steps.networking.OnFinishListener;
import com.steps.R;

public class GroupsActivity extends LoadingActivity {

	private GroupFragment list;
	private MyCursorAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_groups);
		FragmentManager fm = getFragmentManager();
		if (fm.findFragmentById(android.R.id.content) == null) {  
			   list = new GroupFragment();  
			   fm.beginTransaction().add(R.id.fragment, list).commit();  
			  }
		adapter = new MyCursorAdapter(
				getBaseContext(), 
				R.layout.group_compact, 
				null, 
				new String[]{
					Contract.Groups.ID,
					Contract.Groups.NAME
				}, 
				new int[]{
					R.id.id,
					R.id.name
				}, 
				SimpleCursorAdapter.NO_SELECTION);
		list.setListAdapter(adapter);
		refresh();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.groups, menu);
		return true;
	}
	
	@Override
	public void onResume(){
		super.onResume();
		refresh();
	}
	
	
	
	public void logOut(MenuItem item){
		SessionManager manager = new SessionManager(getApplicationContext());
		manager.logoutUser();
		finish();
	}
	
	public void createGroup(MenuItem item){
		Intent intent = new Intent(this, AddGroupActivity.class);
		startActivity(intent);
	}
	
	public void refresh(MenuItem item){
		refresh();
	}
	
	private void refresh(){
		switchFragment(list, loadingFragment);
		SessionManager manager = new SessionManager(getApplicationContext());
		ServerAPI.getInstance().getUserGroups(manager.getUserId(), new OnFinishListener() {
			
			@Override
			public void OnFinish(Cursor result, ErrorCode err) {
				switchFragment(loadingFragment, list);
				adapter.swapCursor(result);	
			}
		});
		
	}	
	
	
	public static class GroupFragment extends ListFragment{
		public GroupFragment() {super();}
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_groups, container, false);
			return rootView;
		}
		
		@Override
	    public void onListItemClick(ListView l, View v, int position, long id) {
	        super.onListItemClick(l, v, position, id);
	        
	        TextView idTV = (TextView)v.findViewById(R.id.id);
	        int groupID = Integer.parseInt(idTV.getText().toString());
	        System.out.println(groupID);
	        ServerAPI.getInstance().getGroup(groupID, new OnFinishListener() {
				
				@Override
				public void OnFinish(Cursor result, ErrorCode err) {
					result.moveToFirst();
					
					Intent intent = new Intent(getActivity(), GroupActivity.class);
					
					intent.putExtra(Contract.Groups.ID, result.getInt(result.getColumnIndex(Contract.Groups.ID)));
					intent.putExtra(Contract.Groups.NAME, result.getString(result.getColumnIndex(Contract.Groups.NAME)));
					intent.putExtra(Contract.Groups.ICON, result.getInt(result.getColumnIndex(Contract.Groups.ICON)));
					
			        startActivity(intent);
				}
			});
	        
	        
	    }
	}
}
