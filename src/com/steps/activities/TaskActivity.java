package com.steps.activities;

import static com.steps.networking.Contract.Tasks.STATUSES.DEFAULT;
import static com.steps.networking.Contract.Tasks.STATUSES.FAILED;
import static com.steps.networking.Contract.Tasks.STATUSES.FINISHED;
import static com.steps.networking.Contract.Tasks.STATUSES.TAKEN;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import android.app.Fragment;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.steps.R;
import com.steps.managers.SessionManager;
import com.steps.networking.Contract;
import com.steps.networking.ErrorCode;
import com.steps.networking.OnFinishListener;
import com.steps.networking.ServerAPI;

public class TaskActivity extends LoadingActivity {
	private TaskFragment taskFragment;
	private int taskID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task);
		taskFragment = new TaskFragment();

		getFragmentManager().beginTransaction()
				.add(R.id.fragment, taskFragment).commit();

		taskID = getIntent().getExtras().getInt(Contract.Tasks.ID);

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.task, menu);
		return true;
	}

	@Override
	public void onResume() {
		super.onResume();
		refresh();
	}

	public void refresh(MenuItem item) {
		refresh();
	}

	private void refresh() {
		switchFragment(taskFragment, loadingFragment);
		ServerAPI.getInstance().getTask(taskID, new OnFinishListener() {

			@Override
			public void OnFinish(Cursor result, ErrorCode err) {
				result.moveToFirst();
				String title = result.getString(result
						.getColumnIndex(Contract.Tasks.TITLE));
				String description = result.getString(result
						.getColumnIndex(Contract.Tasks.DESCRIPTION));
				String userCreated = result.getString(result
						.getColumnIndex(Contract.Tasks.USER_CREATED));
				String userExecuted = result.getString(result
						.getColumnIndex(Contract.Tasks.USER_EXECUTED));
				String dateUpdated = getFormattedDate(result.getString(result
						.getColumnIndex(Contract.Tasks.DATE_UPDATED)));
				String dateDue = result.getString(result
						.getColumnIndex(Contract.Tasks.DATE_DUE));
				String dateExecuted = getFormattedDate(result.getString(result
						.getColumnIndex(Contract.Tasks.DATE_EXECUTED)));
				
				
				
				String userExecutedIDString = result.getString(result.getColumnIndex(Contract.Tasks.USER_EXECUTED_ID));
				int userExecutedID;
				if(userExecutedIDString.equals("null"))
					userExecutedID = -1;
				else
					userExecutedID = Integer.parseInt(userExecutedIDString);
				String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
				
				DateTimeFormatter formatter = DateTimeFormat.forPattern(pattern);
				DateTime date = formatter.parseDateTime(dateDue);
				Interval timeLeftInterval = getTimeLeft(date);
				
				long timeInterval = timeLeftInterval == null ? -1 : timeLeftInterval.getEndMillis() - timeLeftInterval.getStartMillis();
				
				String timeLeft = getTimeLeft(timeLeftInterval);
				int status = getStatus(result.getInt(result
						.getColumnIndex(Contract.Tasks.STATUS)), timeInterval);


				((TextView) findViewById(R.id.task_name)).setText(title);
				((TextView) findViewById(R.id.task_description))
						.setText(description);
				((TextView) findViewById(R.id.task_author))
						.setText(userCreated);
				((TextView) findViewById(R.id.task_updated))
						.setText(dateUpdated);

				if (!userExecuted.equals("null")) {
					if (dateExecuted.equals("null"))
						((TextView) findViewById(R.id.task_executor_lable))
								.setText("Accepted By");

					((TextView) findViewById(R.id.task_executor))
							.setText(userExecuted);
					((View) findViewById(R.id.task_executor_group))
							.setVisibility(View.VISIBLE);
				}
				
				View statusGroup = (View) findViewById(R.id.task_status_group);
				TextView statusLable = (TextView) findViewById(R.id.status_lable);
				TextView statusTV = (TextView)findViewById(R.id.task_status);

				String statusText = null;
				Drawable bg = null;
				switch (status) {
				case TAKEN:
					statusText = "Waiting...";
					bg = getResources()
							.getDrawable(R.drawable.task_list_yellow);
					if(userExecutedID == new SessionManager(getApplicationContext()).getUserId())
						((View)findViewById(R.id.finish_button)).setVisibility(View.VISIBLE);
					break;
				case DEFAULT:
					statusText = "Open";
					bg = getResources().getDrawable(R.drawable.task_list_grey);
					((View)findViewById(R.id.accept_button)).setVisibility(View.VISIBLE);
					break;
				case FINISHED:
					statusText = "Finished";
					bg = getResources().getDrawable(R.drawable.task_list_green);
					statusLable.setTextColor(getResources().getColor(R.color.white));
					statusTV.setTextColor(getResources().getColor(R.color.white));
					break;
				case FAILED:
					statusText = "Expired";
					statusLable.setTextColor(getResources().getColor(R.color.white));
					statusTV.setTextColor(getResources().getColor(R.color.white));
					bg = getResources().getDrawable(R.drawable.task_list_red);
					break;
				}

				int padding_in_dp = 10;
				final float scale = getResources().getDisplayMetrics().density;
				int padding_in_px = (int) (padding_in_dp * scale + 0.5f);

				statusTV.setText(statusText);
				statusGroup.setBackground(bg);
				statusGroup.setPadding(padding_in_px, padding_in_px, padding_in_px, padding_in_px);

				if (timeLeft != null && status != FINISHED) {

					((TextView) findViewById(R.id.task_due_date))
							.setText(timeLeft);

					((View) findViewById(R.id.task_due_date_group))
							.setVisibility(View.VISIBLE);
				}

				switchFragment(loadingFragment, taskFragment);
			}
		});
	}
	
	public void accept(View view){
		switchFragment(taskFragment, loadingFragment);
		ServerAPI.getInstance().assignTask(taskID, new SessionManager(getApplicationContext()).getUserId(), new OnFinishListener() {
			
			@Override
			public void OnFinish(Cursor result, ErrorCode err) {
				((View)findViewById(R.id.accept_button)).setVisibility(View.GONE);
				switchFragment(loadingFragment, taskFragment);
				refresh();
			}
		});
	}
	
	public void finish(View view){
		switchFragment(taskFragment, loadingFragment);
		ServerAPI.getInstance().finishTask(taskID, new OnFinishListener() {
			
			@Override
			public void OnFinish(Cursor result, ErrorCode err) {
				switchFragment(loadingFragment, taskFragment);
				refresh();
			}
		});
	}

	private String getFormattedDate(String orig) {
		if (orig.equals("null"))
			return orig;
		SimpleDateFormat sdf = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ENGLISH);

		String dateString = null;
		try {
			Date date = sdf.parse(orig);
			sdf.applyLocalizedPattern("yyy/MM/dd HH:mm");
			dateString = sdf.format(date);
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
		return dateString;
	}

	private int getStatus(int status, long timeLeft) {
		switch (status) {
		case DEFAULT:
		case TAKEN:
			if (timeLeft <= 0)
				return FAILED;
			else
				return status;

		default:
			return status;
		}
	}

	private Interval getTimeLeft(DateTime date){
		DateTime currentDate = new DateTime();
			
		if(date.isBefore(currentDate)) return null;
		
		return new Interval(currentDate, date);
	}
	
	private String getTimeLeft(Interval interval){
		if(interval == null)
			return null;
		
		Period period = interval.toPeriod();
		PeriodFormatter daysHoursMinutes = new PeriodFormatterBuilder()
	    .appendYears()
	    .appendSuffix("y")
	    .appendSeparator(" ")
	    .appendMonths()
	    .appendSuffix("m")
	    .appendSeparator(" ")
		.appendDays()
	    .appendSuffix("d")
	    .appendSeparator(" ")
	    .appendHours()
	    .appendSuffix("h")
	    .appendSeparator(" ")
	    .appendMinutes()
	    .appendSuffix("m")
	    .appendSeparator(" ")
	    .appendSeconds()
	    .appendSuffix("s")
	    .toFormatter();


		return daysHoursMinutes.print(period);
	}
	public static class TaskFragment extends Fragment {
		public TaskFragment() {
			super();
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_task, container,
					false);
			return rootView;
		}
	}
}
