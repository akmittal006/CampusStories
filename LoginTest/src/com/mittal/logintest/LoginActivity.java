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
import bolts.Continuation;
import bolts.Task;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.ibm.mobile.services.data.IBMDataObject;
import com.ibm.mobile.services.data.IBMQuery;

public class LoginActivity extends Activity {
	
	private EditText nameField;
	private EditText passwordField;
	private Button loginButton;
	private TextView signUpView;
	private boolean errorStatus = false;
	private ProgressBar mProgBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		
		
		nameField = (EditText) findViewById(R.id.nameEditText);
		passwordField = (EditText) findViewById(R.id.passwordEditText);
		loginButton = (Button) findViewById(R.id.login_button);
		signUpView = (TextView)findViewById(R.id.signUpTextView);
		mProgBar = (ProgressBar)findViewById(R.id.loginProg);
		ImageView imageView = (ImageView) findViewById(R.id.imageView);
		YoYo.with(Techniques.Bounce)
	    .duration(1200)
	    .playOn(findViewById(R.id.imageView));
		
		loginButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				mProgBar.setVisibility(View.VISIBLE);
				User newUser = new User();
				if(nameField.getText().toString() != "") {
					newUser.setUsername(nameField.getText().toString());
					Log.e("checking username" , nameField.getText().toString() + " :::" + newUser.getUsername()); 
					errorStatus = true;
				} else {
					nameField.setError("name cant be empty");
					errorStatus = false;
				}
				
				if(passwordField.getText().toString() != "") {
					newUser.setPassword(passwordField.getText().toString());
					Log.e("checking password" , passwordField.getText().toString() + " :::" + newUser.getPassword()); 
					errorStatus = true;
				} else {
					nameField.setError("password cant be empty");
					errorStatus = false;
				}
				if(errorStatus == true) {
					nameField.setText("");
					passwordField.setText("");
					newUser.setLoggedIn(false);
					login(newUser);
					YoYo.with(Techniques.Tada)
				    .duration(700)
				    .playOn(findViewById(R.id.login_button));
				} 
			}
		});
		signUpView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
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
	
	private void login(final User newUser) {
		Log.e("login activity", "again checking" + newUser.getUsername() + " " + newUser.getName());
		IBMQuery<User> query = IBMQuery.queryForClass("User");
		query.whereKeyEqualsTo("username", newUser.getUsername());
		query.find().continueWith(new Continuation<List<User>, Void>() {

			@Override
			public Void then(Task<List<User>> task) throws Exception {
				// TODO Auto-generated method stub
				//mProgBar.setVisibility(View.INVISIBLE);
				LoginTestApplication ltapp = (LoginTestApplication) getApplication();
				ltapp.currentUser = null;
				if(task.isFaulted()) {
					Log.e("login activity", "noo login :(");
					return null;
				}
				Log.e("login activity", "yayy user mil gaya " + task.getResult().size());
				User user = task.getResult().get(0);
				Log.e("login activity result", "logging in" + user.getUsername() + " " + user.getName());
				Log.e("login activity result 2 ", "logging in" + user.getPassword() + " " + newUser.getPassword());
				if(user.getPassword().equals(newUser.getPassword())) {
					Log.e("login activity", "yayy logged in " );
					ltapp.currentUser = user;
					
					DatabaseHandler db = new DatabaseHandler(LoginActivity.this);
					db.addUser(user);
					Intent intent = new Intent(LoginActivity.this,
							CampusStories.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
					user.setLoggedIn(true);
					user.save().continueWith(new Continuation<IBMDataObject, Void>() {

						@Override
						public Void then(Task<IBMDataObject> task)
								throws Exception {
							if(task.isFaulted()) {
								Log.e("login", "not logged in");
								return null;
							}
							Log.e("login", " logged in status");
							return null;
						}
					});
				} else {
					// wrong password
					passwordField.setError("Wrong Password");
					passwordField.requestFocus();
					Log.e("login activity", "not logged in " );
				}
				
				return null;
			}
		},Task.UI_THREAD_EXECUTOR);
	}
}
