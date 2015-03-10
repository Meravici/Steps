package com.steps.activities;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.steps.R;
import com.steps.managers.MyCursorAdapter;
import com.steps.networking.Contract;
import com.steps.networking.ErrorCode;
import com.steps.networking.OnFinishListener;
import com.steps.networking.ServerAPI;

public class AddToGroupActivity extends LoadingActivity {

	private MembersFragment membersFragment;
	private SimpleCursorAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_to_group);
		membersFragment = new MembersFragment();
		membersFragment.setArguments(getIntent().getExtras());

		getFragmentManager().beginTransaction()
				.add(R.id.fragment, membersFragment).commit();

		adapter = new SimpleCursorAdapter(getBaseContext(), R.layout.user_compact, null, new String[]{
					Contract.Users.ID,
					Contract.Users.DISPLAY_NAME}, 
				new int[]{
					R.id.id,
					R.id.name}, 
				MyCursorAdapter.NO_SELECTION);
		
		membersFragment.setListAdapter(adapter);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		/** Create an option menu from res/menu/items.xml */
		getMenuInflater().inflate(R.menu.add_to_group, menu);

		/** Get the action view of the menu item whose id is search */
		View v = (View) menu.findItem(R.id.search).getActionView();

		/** Get the edit text from the action view */
		EditText txtSearch = (EditText) v.findViewById(R.id.txt_search);

		/** Setting an action listener */
		txtSearch.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				switchFragment(membersFragment, loadingFragment);
				ServerAPI.getInstance().getUserByName(v.getText().toString(),
						new OnFinishListener() {

							@Override
							public void OnFinish(Cursor result, ErrorCode err) {
								switchFragment(loadingFragment, membersFragment);
								adapter.swapCursor(result);
							}
						});
				return true;
			}
		});

		return super.onCreateOptionsMenu(menu);
	}

	
	
	public static class MembersFragment extends ListFragment {
		private int groupID;
		
		public MembersFragment() {
			super();
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_group_members,
					container, false);
			
			groupID = getArguments().getInt(Contract.Groups.ID);
			return rootView;
		}

		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {
			super.onListItemClick(l, v, position, id);
			final int userID = Integer.parseInt(((TextView)v.findViewById(R.id.id)).getText().toString());
			DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			    @Override
			    public void onClick(DialogInterface dialog, int which) {
			        switch (which){
			        case DialogInterface.BUTTON_POSITIVE:
			            ServerAPI.getInstance().joinGroup(userID, groupID, new OnFinishListener() {
							
							@Override
							public void OnFinish(Cursor result, ErrorCode err) {
								Toast.makeText(getActivity().getApplicationContext(), "Successfully Added", Toast.LENGTH_SHORT).show();
								getActivity().finish();
							}
						});
			            break;

			        case DialogInterface.BUTTON_NEGATIVE:
			            //No button clicked
			            break;
			        }
			    }
			};

			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage("Are you sure you want to add?").setPositiveButton("Yes", dialogClickListener)
			    .setNegativeButton("No", dialogClickListener).show();
		}
	}
}
