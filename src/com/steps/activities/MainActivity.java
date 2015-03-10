package com.steps.activities;

import com.steps.managers.SessionManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(checkLogin())
			startGroupsActivity();
		else
			startLoginActivity();
		finish();
	}
	
	
	
	private void startLoginActivity() {
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
	}



	private void startGroupsActivity() {
		Intent intent = new Intent(this, GroupsActivity.class);
		startActivity(intent);
	}



	private boolean checkLogin(){
		SessionManager sm = new SessionManager(getApplicationContext());
		return sm.isLoggedIn();
	}
	
}
