package com.steps.activities;

import android.app.ListFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.steps.R;
import com.steps.managers.MyCursorAdapter;
import com.steps.networking.Contract;
import com.steps.networking.ErrorCode;
import com.steps.networking.OnFinishListener;
import com.steps.networking.ServerAPI;

public class GroupMembersActivity extends LoadingActivity {
	private GroupMembersFragment groupMembersFragment;
	private SimpleCursorAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_members);
		
		
		groupMembersFragment = new GroupMembersFragment();
		
		getFragmentManager().beginTransaction().add(R.id.fragment, groupMembersFragment).commit();
		
		
		adapter = new SimpleCursorAdapter(
				getBaseContext(), 
				R.layout.user_compact, 
				null, 
				new String[]{
					Contract.Users.ID,
					Contract.Users.DISPLAY_NAME}, 
				new int[]{
					R.id.id,
					R.id.name}, 
				MyCursorAdapter.NO_SELECTION);

		groupMembersFragment.setListAdapter(adapter);
		
		int id = getIntent().getExtras().getInt(Contract.Groups.ID);
		
		switchFragment(groupMembersFragment, loadingFragment);
		ServerAPI.getInstance().getGroupUsers(id, new OnFinishListener() {
			
			@Override
			public void OnFinish(Cursor result, ErrorCode err) {
				//TODO error checking
				adapter.swapCursor(result);	
				switchFragment(loadingFragment, groupMembersFragment);
			}
		});
		
	}

	public static class GroupMembersFragment extends ListFragment {
		public GroupMembersFragment() {
			super();
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_group_members,
					container, false);
			return rootView;
		}

		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {
			super.onListItemClick(l, v, position, id);
			Toast.makeText(getActivity().getApplicationContext(), "Disabled", Toast.LENGTH_SHORT).show();
		}
	}
}
