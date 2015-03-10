package com.steps.activities;

import com.steps.R;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class LoadingActivity extends Activity {
	protected LoadingFragment loadingFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		loadingFragment = new LoadingFragment();
		getFragmentManager()
			.beginTransaction()
			.add(R.id.fragment, loadingFragment)
			.hide(loadingFragment)
			.commit();
	}	
	
	protected void switchFragment(Fragment from, Fragment to) {
		getFragmentManager().
			beginTransaction().
			hide(from).
			show(to).
			commit();
	}
	
	protected boolean validate(EditText...ets){
		for(EditText et : ets)
			if(et.getText().toString().length() == 0){
				et.setError(getString(R.string.must_be_filled));
				return false;
			}
		return true;
	}
	
	protected static class LoadingFragment extends Fragment{
		public LoadingFragment() {super();}
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_loading, container, false);
			return rootView;
		}
	}	
}
