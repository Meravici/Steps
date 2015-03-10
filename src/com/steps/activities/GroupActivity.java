package com.steps.activities;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.steps.R;
import com.steps.managers.MyCursorAdapter;
import com.steps.managers.SessionManager;
import com.steps.networking.Contract;
import com.steps.networking.ErrorCode;
import com.steps.networking.OnFinishListener;
import com.steps.networking.ServerAPI;

public class GroupActivity extends LoadingActivity{
	
	private TasksFragment tasksFragment;
	private int id;
	private Bundle bundle;
	private MyCursorAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group);
		
		bundle = getIntent() == null ? savedInstanceState.getBundle("data") : getIntent().getExtras();
		
		
		id = bundle.getInt(Contract.Groups.ID);
		String name = bundle.getString(Contract.Groups.NAME);
		int iconID = bundle.getInt(Contract.Groups.ICON);
		
		switch(iconID){
		case 0:
			((ImageView)findViewById(R.id.group_icon)).setImageResource(R.drawable.h1);
			break;
		case 1:
			((ImageView)findViewById(R.id.group_icon)).setImageResource(R.drawable.h2);
			break;
		case 2:
			((ImageView)findViewById(R.id.group_icon)).setImageResource(R.drawable.h3);
			break;
		case 3:
			((ImageView)findViewById(R.id.group_icon)).setImageResource(R.drawable.h4);
			break;
		}
		
		
		((TextView)findViewById(R.id.group_name)).setText(name);

		
		
		tasksFragment = new TasksFragment();
		
		getFragmentManager().beginTransaction().add(R.id.fragment, tasksFragment)
			.commit();
		
		adapter = new MyCursorAdapter(
				getBaseContext(), 
				R.layout.task_compact, 
				null, 
				new String[]{
					Contract.Tasks.STATUS,
					Contract.Tasks.ID, 
					Contract.Tasks.TITLE,
					Contract.Tasks.DATE_DUE}, 
				new int[]{
					R.id.status,
					R.id.id,
					R.id.title, 
					R.id.time_left}, 
				SimpleCursorAdapter.NO_SELECTION);
		
		tasksFragment.setListAdapter(adapter);
		refresh();
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		refresh();
	}
	@Override
	protected void onSaveInstanceState(Bundle bundle) {
		  super.onSaveInstanceState(bundle);
		  bundle.putBundle("data", this.bundle);
		}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.group, menu);
		return true;
	}

	
	public void getMembers(MenuItem item){
		Intent intent = new Intent(this, GroupMembersActivity.class);
		intent.putExtra(Contract.Groups.ID, id);
		startActivity(intent);
	}
	
	
	public void addMember(MenuItem item){
		Intent intent = new Intent(this, AddToGroupActivity.class);
		intent.putExtra(Contract.Groups.ID, id);
		startActivity(intent);
	}
	
	public void leaveGroup(MenuItem item){
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        switch (which){
		        case DialogInterface.BUTTON_POSITIVE:
		            ServerAPI.getInstance().leaveGroup(id, new SessionManager(getApplicationContext()).getUserId(), new OnFinishListener() {
						
						@Override
						public void OnFinish(Cursor result, ErrorCode err) {
							Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
							finish();
						}
					});

		        case DialogInterface.BUTTON_NEGATIVE:
		            //No button clicked
		            break;
		        }
		    }
		};
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Are you sure you want to leave?").setPositiveButton("Yes", dialogClickListener)
		    .setNegativeButton("No", dialogClickListener).show();
	}
	
	public void addTask(MenuItem item){
		Intent intent = new Intent(this, AddNewTaskActivity.class);
		intent.putExtra(Contract.Groups.ID, id);
		startActivity(intent);
	}
	
	public void refresh(MenuItem item){
		refresh();
	}
	
	private void refresh(){
		switchFragment(tasksFragment, loadingFragment);
		ServerAPI.getInstance().getGroupTasks(id, new OnFinishListener() {
			
			@Override
			public void OnFinish(Cursor result, ErrorCode err) {
				switchFragment(loadingFragment, tasksFragment);
				adapter.swapCursor(result);
			}
		});
	}
	
	public static class TasksFragment extends ListFragment{
		public TasksFragment() {super();}
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_tasks, container, false);
			return rootView;
		}
		
		@Override
	    public void onListItemClick(ListView l, View v, int position, long id) {
	        super.onListItemClick(l, v, position, id);
	        
	        TextView idTV = (TextView)v.findViewById(R.id.id);
	        int taskID = Integer.parseInt(idTV.getText().toString());
	        
	        
			Intent intent = new Intent(getActivity(), TaskActivity.class);
			
			intent.putExtra(Contract.Tasks.ID, taskID);

			startActivity(intent);	        
	        
	    }
	}
}
