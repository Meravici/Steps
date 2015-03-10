package com.steps.activities;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.steps.R;
import com.steps.managers.SessionManager;
import com.steps.networking.ErrorCode;
import com.steps.networking.OnFinishListener;
import com.steps.networking.ServerAPI;

public class SignUpActivity extends LoadingActivity {

	private Fragment signupFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);

		signupFragment = new SignupFragment();

		getFragmentManager().beginTransaction()
				.add(R.id.fragment, signupFragment).commit();

		switchFragment(loadingFragment, signupFragment);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sign_up, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void signUp(View view) {
		final EditText emailET = (EditText) findViewById(R.id.email);
		EditText passwordET = (EditText) findViewById(R.id.password);
		EditText displayNameET = (EditText) findViewById(R.id.display_name);
		if (validate(emailET, passwordET, displayNameET)) {
			switchFragment(signupFragment, loadingFragment);
			ServerAPI.getInstance().register(emailET.getText().toString(),
					passwordET.getText().toString(),
					displayNameET.getText().toString(), new OnFinishListener() {

						@Override
						public void OnFinish(Cursor result, ErrorCode err) {
							if (err != null) {
								switchFragment(loadingFragment, signupFragment);
								if (err == ErrorCode.ER_DUP_ENTRY)
									emailET.setError("Email address already in use");
								else
									Toast.makeText(getApplicationContext(),
											"Houston, we have a problem!",
											Toast.LENGTH_SHORT).show();

							} else if (result != null) {
								result.moveToFirst();
								Toast toast = Toast.makeText(
										getApplicationContext(), "Welcome "
												+ result.getString(1),
										Toast.LENGTH_LONG);
								toast.show();

								SessionManager manager = new SessionManager(
										getApplicationContext());
								manager.loginUser(result.getInt(0));

								finish();
							} else {
								switchFragment(loadingFragment, signupFragment);
								Toast.makeText(getApplicationContext(),
										"Houston, we have a problem!",
										Toast.LENGTH_SHORT).show();
							}
						}
					});
		}
	}

	public static class SignupFragment extends Fragment {
		public SignupFragment() {
			super();
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_signup,
					container, false);
			return rootView;
		}
	}

}
