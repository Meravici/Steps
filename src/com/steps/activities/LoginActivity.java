package com.steps.activities;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.steps.R;
import com.steps.managers.SessionManager;
import com.steps.networking.ErrorCode;
import com.steps.networking.OnFinishListener;
import com.steps.networking.ServerAPI;




public class LoginActivity extends LoadingActivity {
	private Fragment loginFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);		
		loginFragment = new LoginFragment();
		
		getFragmentManager().beginTransaction().
			add(R.id.fragment, loginFragment).commit();
		
//		switchFragment(loadingFragment, loginFragment);
	}	
		
	@Override
	public void onResume(){
		super.onResume();
		SessionManager sm = new SessionManager(getApplicationContext());
		if(sm.isLoggedIn()){
			Intent intent = new Intent(this, GroupsActivity.class);
			startActivity(intent);
			finish();
		}
	}
	
	public void login(View view){
		final EditText emailET = (EditText) findViewById(R.id.email);
		EditText passwordET = (EditText) findViewById(R.id.password);
		
		if(validate(emailET, passwordET)){
		
			//show loading
			switchFragment(loginFragment, loadingFragment);
			
			final Activity activity = this;
					
			ServerAPI.getInstance().login(emailET.getText().toString(), passwordET.getText().toString(), new OnFinishListener() {
				
				@Override
				public void OnFinish(Cursor result, ErrorCode error) {
					if(error != null){
						switchFragment(loadingFragment, loginFragment);
						emailET.setError(getString(R.string.email_and_or_password_is_incorrect));
					}else if(result !=null && result.getCount() == 1){
						result.moveToFirst();
						int id = result.getInt(0);
						if(id != 0){
							Toast toast = Toast.makeText(getApplicationContext(), "Welcome Back " + result.getString(1), Toast.LENGTH_LONG);
							toast.show();
							SessionManager manager = new SessionManager(getApplication());
							manager.loginUser(id);
							Intent intent = new Intent(activity, GroupsActivity.class);
							startActivity(intent);
							finish();
						}else{
							switchFragment(loadingFragment, loginFragment);
							emailET.setError(getString(R.string.email_and_or_password_is_incorrect));
						}
					}else{
						switchFragment(loadingFragment, loginFragment);
						emailET.setError(getString(R.string.email_and_or_password_is_incorrect));
					}					
				}
			});
		}
	}	
	
	public void signUp(View view){
		Intent intent = new Intent(this, SignUpActivity.class);
		startActivity(intent);
	}
	
	public static class LoginFragment extends Fragment{
		public LoginFragment() {super();}
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_login, container, false);
			return rootView;
		}	
	}

}
