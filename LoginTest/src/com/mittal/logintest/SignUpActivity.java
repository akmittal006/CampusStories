package com.mittal.logintest;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import bolts.Continuation;
import bolts.Task;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.ibm.mobile.services.data.IBMDataObject;
import com.ibm.mobile.services.data.IBMQuery;

public class SignUpActivity extends Activity {

	public EditText nameField;
	public EditText usernameField;
	public EditText passwordField;
	public Button signupButton;
	private TextView loginView;
	private boolean errorStatus = false;
	private ProgressBar mProgBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		setTitle("Sign up!");
		nameField = (EditText) findViewById(R.id.signupNameEditText);
		usernameField = (EditText) findViewById(R.id.signupUsernameEditText);
		passwordField = (EditText) findViewById(R.id.signupPasswordEditText);
		signupButton = (Button) findViewById(R.id.login_button);
		loginView = (TextView) findViewById(R.id.signUpTextView);
		mProgBar = (ProgressBar) findViewById(R.id.signProg);
		ImageView imageView = (ImageView) findViewById(R.id.imageView1);
		YoYo.with(Techniques.Bounce)
	    .duration(1200)
	    .playOn(findViewById(R.id.imageView1));
		signupButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
			
				User newUser = new User();
				mProgBar.setVisibility(View.VISIBLE);
				if (nameField.getText().toString() != "") {
					Log.e("siggggggg",nameField.getText().toString() );
					newUser.setName(nameField.getText().toString());
					Log.e("siggggggg",newUser.getName() );
					errorStatus = true;
				} else {
					nameField.setError("name cant be empty");
					errorStatus = false;
				}
				if (errorStatus == true) {
					if (usernameField.getText().toString() != "") {
						newUser.setUsername(usernameField.getText().toString());
						errorStatus = true;
					} else {
						usernameField.setError("username cant be empty");
						errorStatus = false;
					}
					if (errorStatus == true) {
						if (passwordField.getText().toString() != "") {
							newUser.setPassword(passwordField.getText()
									.toString());
							errorStatus = true;
						} else {
							passwordField.setError("password cant be empty");
							errorStatus = false;
						}
					}

				}

				if (errorStatus == true) {
					nameField.setText("");
					usernameField.setText("");
					passwordField.setText("");
					newUser.setLoggedIn(false);
					Log.e("sign up", newUser.getName() + " " + newUser.getUsername());
					signup(newUser);
				}
				YoYo.with(Techniques.Tada)
			    .duration(700)
			    .playOn(findViewById(R.id.login_button));
			}
		});

		loginView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				startActivity(new Intent(SignUpActivity.this,
						LoginActivity.class));

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}
		return super.onOptionsItemSelected(item);
	}

	private void signup(final User newUser) {

		IBMQuery<User> query = IBMQuery.queryForClass("User");
		query.whereKeyEqualsTo("username", newUser.getUsername());
		query.find().continueWith(new Continuation<List<User>, Void>() {

			@Override
			public Void then(Task<List<User>> task) throws Exception {

				// TODO Auto-generated method stub
				mProgBar.setVisibility(View.INVISIBLE);
				if (task.isFaulted()) {
					Log.e("signup", "error signing up");
					return null;
				}
				Log.e("signup", "signing up" + task.getResult().size());
				if (task.getResult().size() == 0) {
					// unique
					Log.e("sign up 222", newUser.getName() + " " + newUser.getUsername());
					newUser.save().continueWith(
							new Continuation<IBMDataObject, Void>() {

								@Override
								public Void then(Task<IBMDataObject> task)
										throws Exception {
									LoginTestApplication ltapp = (LoginTestApplication) getApplication();
									ltapp.currentUser = null;
									if (task.isFaulted()) {
										Log.e("signup activity",
												"Alas no sign upped");
										return null;
									}
									Log.e("signup activity", "huray sign upped");
									startActivity(new Intent(
											SignUpActivity.this,
											LoginActivity.class));
									return null;
								}
							});
				} else {
					Toast.makeText(SignUpActivity.this,
							"Username already taken! Try another",
							Toast.LENGTH_LONG).show();
				}
				return null;
			}
		}, Task.UI_THREAD_EXECUTOR);

	}
}
