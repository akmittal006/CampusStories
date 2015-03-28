package com.mittal.logintest;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import com.ibm.mobile.services.core.IBMBluemix;
import com.ibm.mobile.services.data.IBMData;

public class LoginTestApplication extends Application {
	
	public static final int EDIT_ACTIVITY_RC = 1;
	private static final String PROPS_FILE = "bluelist.properties";
	private static final String CLASS_NAME = LoginTestApplication.class.getSimpleName();

	public static final String app_id = "ba36e180-cd7f-4a2f-98d5-499c62c2cbde";
	public static final String app_secret = "2466718b3cdc987e2af668144c6dce7d93e550b5";
	public static final String app_route= "http://campusstories.eu-gb.mybluemix.net";
//	public static final String app_id = "04727a7b-3219-4939-9ff6-4115bbb3462d";
//	public static final String app_secret = "a927d95d565ead9fc47d3e4452446914193e11e4";
//	public static final String app_route= "http://first-kyun.eu-gb.mybluemix.net";
	public User currentUser = null;
	DatabaseHandler db;

	public User user;
	

	public LoginTestApplication() {
		
		
		
		registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
			@Override
			public void onActivityCreated(Activity activity,Bundle savedInstanceState) {
				Log.d(CLASS_NAME, "Activity created: " + activity.getLocalClassName());
			}
			@Override
			public void onActivityStarted(Activity activity) {
				Log.d(CLASS_NAME, "Activity started: " + activity.getLocalClassName());
			}
			@Override
			public void onActivityResumed(Activity activity) {
				Log.d(CLASS_NAME, "Activity resumed: " + activity.getLocalClassName());
			}
			@Override
			public void onActivitySaveInstanceState(Activity activity,Bundle outState) {
				Log.d(CLASS_NAME, "Activity saved instance state: " + activity.getLocalClassName());
			}
			@Override
			public void onActivityPaused(Activity activity) {
				Log.d(CLASS_NAME, "Activity paused: " + activity.getLocalClassName());
			}
			@Override
			public void onActivityStopped(Activity activity) {
				Log.d(CLASS_NAME, "Activity stopped: " + activity.getLocalClassName());
			}
			@Override
			public void onActivityDestroyed(Activity activity) {
				Log.d(CLASS_NAME, "Activity destroyed: " + activity.getLocalClassName());
			}
		});
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		user = new User();
		IBMBluemix.initialize(this, app_id, app_secret, app_route);
		IBMData.initializeService();
		User.registerSpecialization(User.class);
		Story.registerSpecialization(Story.class);
		
	}
	
	public void setCurrentUser(User user) {
		currentUser = user;
		if(db.getUser() == null) {
			//add user
			db.addUser(user);
		} else {
			db.updateUser(user) ;
			
		}
	}
	
	}


