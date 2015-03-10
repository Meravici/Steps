package com.steps.activities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.steps.R;
import com.steps.managers.SessionManager;
import com.steps.networking.Contract;
import com.steps.networking.ErrorCode;
import com.steps.networking.OnFinishListener;
import com.steps.networking.ServerAPI;

public class AddNewTaskActivity extends LoadingActivity {

	private AddTaskFragment addTaskFragment;
	private int groupID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_new_task);

		groupID = getIntent().getExtras().getInt(Contract.Groups.ID);

		addTaskFragment = new AddTaskFragment();

		getFragmentManager().beginTransaction()
				.add(R.id.fragment, addTaskFragment).commit();
	}

	public void addTask(View v) {
		
		int userID = new SessionManager(getApplicationContext()).getUserId();
		EditText titleET = ((EditText) findViewById(R.id.title));
		EditText descriptionET = ((EditText) findViewById(R.id.description));

		if (validate(titleET, descriptionET)) {
			switchFragment(addTaskFragment, loadingFragment);
			String title = titleET.getText().toString();
			String description = descriptionET.getText().toString();

			DatePicker dp = (DatePicker) findViewById(R.id.date);
			TimePicker tp = (TimePicker) findViewById(R.id.time);

			Calendar calendar = new GregorianCalendar();
			calendar.set(dp.getYear(), dp.getMonth(), dp.getDayOfMonth(),
					tp.getCurrentHour(), tp.getCurrentMinute());
			SimpleDateFormat sdf = new SimpleDateFormat(
					"yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ENGLISH);
			String dateDue = sdf.format(calendar.getTime());

			ServerAPI.getInstance().addNewTask(groupID, title, description,
					dateDue, userID, new OnFinishListener() {

						@Override
						public void OnFinish(Cursor result, ErrorCode err) {
							Toast.makeText(getApplicationContext(), "Success",
									Toast.LENGTH_SHORT).show();
							finish();
						}
					});
		}
	}

	public static class AddTaskFragment extends Fragment {
		public AddTaskFragment() {
			super();
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_add_task,
					container, false);
			return rootView;
		}
	}
}
