package com.steps.activities;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import com.steps.R;
import com.steps.managers.IconSpinnerAdapter;
import com.steps.managers.SessionManager;
import com.steps.networking.ErrorCode;
import com.steps.networking.OnFinishListener;
import com.steps.networking.ServerAPI;

public class AddGroupActivity extends LoadingActivity {

	private AddGroupFragment addGroupFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_group);

		addGroupFragment = new AddGroupFragment();

		getFragmentManager().beginTransaction()
				.add(R.id.fragment, addGroupFragment).commit();
	}

	public void createGroup(View view) {
		switchFragment(addGroupFragment, loadingFragment);

		EditText nameET = (EditText) findViewById(R.id.name);

		if (validate(nameET)) {

			Spinner iconSpinner = addGroupFragment.getSpinner();

			SessionManager se = new SessionManager(getApplicationContext());
			int iconID = (Integer) iconSpinner.getSelectedItem();
			ServerAPI.getInstance().createGroup(iconID, se.getUserId(),
					nameET.getText().toString(), new OnFinishListener() {

						@Override
						public void OnFinish(Cursor result, ErrorCode err) {
							finish();
						}
					});
		}
	}

	public static class AddGroupFragment extends Fragment {
		private Spinner spinner;

		public AddGroupFragment() {
			super();
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_add_group,
					container, false);

			spinner = (Spinner) rootView.findViewById(R.id.icon_spinner);
			Log.d("BLA", "" + (spinner == null));
			spinner.setAdapter(new IconSpinnerAdapter(getActivity()));

			return rootView;
		}

		public Spinner getSpinner() {
			return spinner;
		}
	}
}