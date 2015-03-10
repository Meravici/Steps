package com.steps.managers;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.steps.R;

public class IconSpinnerAdapter extends BaseAdapter {
	private static final int NUM_ICONS = 4;
	Drawable[] icons = new Drawable[NUM_ICONS];
	Activity activity;

	public IconSpinnerAdapter(Activity activity) {
		icons[0] = activity.getResources().getDrawable(R.drawable.h1);
		icons[1] = activity.getResources().getDrawable(R.drawable.h2);
		icons[2] = activity.getResources().getDrawable(R.drawable.h3);
		icons[3] = activity.getResources().getDrawable(R.drawable.h4);

		this.activity = activity;
	}

	@Override
	public int getCount() {
		return icons.length;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder mViewHolder = null;

		if (convertView == null) {

			mViewHolder = new ViewHolder();

			LayoutInflater vi = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			convertView = vi.inflate(R.layout.image_chooser_item, parent,
					false);
			mViewHolder.icon = (ImageView) convertView.findViewById(R.id.spinnerImage);
			convertView.setTag(mViewHolder);
			
		} else {
			mViewHolder = (ViewHolder) convertView.getTag();
		}

		mViewHolder.icon.setImageDrawable(icons[position]);
		return convertView;
	}

	static class ViewHolder {
		ImageView icon;
	}
}
