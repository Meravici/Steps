package com.steps.managers;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.steps.R;
import com.steps.networking.Contract;
import com.steps.networking.Contract.Tasks;


public class MyCursorAdapter extends SimpleCursorAdapter {

	public MyCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to, int flags) {
		super(context, layout, c, from, to, flags);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		super.bindView(view, context, cursor);

		int iconColumnIndex = cursor.getColumnIndex(Contract.Groups.ICON);

		int statusColumnIndex = cursor.getColumnIndex(Contract.Tasks.STATUS);

		int dueDateColumnIndex = cursor.getColumnIndex(Contract.Tasks.DATE_DUE);

		if (iconColumnIndex != -1) {
			setImage(cursor.getInt(iconColumnIndex), view);
		}

		if (statusColumnIndex != -1) {
			setStatus(cursor.getInt(statusColumnIndex), view, context);
		}

		if (dueDateColumnIndex != -1) {
			TextView tv = (TextView) view.findViewById(R.id.time_left);
			String date = cursor.getString(dueDateColumnIndex);
			String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
			
			DateTimeFormatter formatter = DateTimeFormat.forPattern(pattern);
			DateTime d = formatter.parseDateTime(date);

			
			String time = getTimeLeft(d);

				if (time == null
						&& cursor.getInt(statusColumnIndex) != Tasks.STATUSES.FINISHED) {
					setStatus(Contract.Tasks.STATUSES.FAILED, view, context);
					tv.setText("Expired");
					tv.setTextColor(context.getResources()
							.getColor(R.color.red));
				} else if (cursor.getInt(statusColumnIndex) == Contract.Tasks.STATUSES.FINISHED) {
					setStatus(Contract.Tasks.STATUSES.FINISHED, view, context);
					tv.setText("Finished");
					tv.setTextColor(context.getResources().getColor(
							R.color.green));
				} else
					tv.setText(time);
		}

	}

	private void setStatus(int status, View view, Context context) {
		TextView tv = (TextView) view.findViewById(R.id.status);
		tv.setText("");

		switch (status) {
		case Contract.Tasks.STATUSES.DEFAULT:
			tv.setBackgroundColor(context.getResources().getColor(R.color.grey));
			break;
		case Contract.Tasks.STATUSES.FAILED:
			tv.setBackgroundColor(context.getResources().getColor(R.color.red));
			break;
		case Contract.Tasks.STATUSES.FINISHED:
			tv.setBackgroundColor(context.getResources()
					.getColor(R.color.green));
			break;
		case Contract.Tasks.STATUSES.TAKEN:
			tv.setBackgroundColor(context.getResources().getColor(
					R.color.yellow));
			break;
		default:
			break;
		}
	}

	private void setImage(int iconId, View view) {
		ImageView iv = (ImageView) view.findViewById(R.id.icon);
		switch (iconId) {
		case 0:
			iv.setImageResource(R.drawable.h1);
			break;
		case 1:
			iv.setImageResource(R.drawable.h2);
			break;
		case 2:
			iv.setImageResource(R.drawable.h3);
			break;
		case 3:
			iv.setImageResource(R.drawable.h4);
			break;
		default:
			iv.setImageResource(R.drawable.ic_launcher);
			break;
		}
	}

	private String getTimeLeft(DateTime date){
		DateTime currentDate = new DateTime();
			
		if(date.isBefore(currentDate)) return null;
		
		Interval interval = new Interval(currentDate, date);
		
	
		
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
}
