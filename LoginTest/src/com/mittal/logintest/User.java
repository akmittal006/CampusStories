package com.mittal.logintest;

import java.util.ArrayList;

import com.ibm.mobile.services.data.IBMDataObject;
import com.ibm.mobile.services.data.IBMDataObjectSpecialization;

@IBMDataObjectSpecialization("User")
public class User extends IBMDataObject {

	public String name;
	public String username;
	public String password;
	public boolean loggedIn;

	public static final String CLASS_NAME = "User";
	public static final String NAME = "name";
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	public static final String LOGGED_IN = "loggedIn";
	public static final String INTEREST = "interest";
	public static final String VIEWS = "views";

	public String getName() {
		return (String) getObject(NAME);
	}

	public void setName(String name) {
		setObject("name", name);
	}

	public String getUsername() {
		return (String) getObject(USERNAME);
	}

	public void setUsername(String username) {
		setObject(USERNAME, username);
	}

	public String getPassword() {
		return (String) getObject(PASSWORD);
	}

	public void setPassword(String password) {
		setObject(PASSWORD, password);
	}

	public boolean isLoggedIn() {
		return (Boolean) getObject(LOGGED_IN);
	}

	public void setLoggedIn(boolean loggedIn) {
		setObject(LOGGED_IN, loggedIn);
	}

	public void addInterest(Interest intrst) {

		ArrayList<Interest> intrsts;

		if (getInterest() == null || (getInterest().size() == 0)) {
			intrsts = new ArrayList<Interest>();
			intrsts.add(intrst);
		} else {
			
			intrsts = getInterest();
			intrsts.add(intrst);
		}
		setObject(INTEREST, intrsts);

	}

	public ArrayList<Interest> getInterest() {
		return (ArrayList<Interest>) getObject(INTEREST);
	}

	public void addView(String view) {
		ArrayList<String> views;

		if (getViews() == null || (getViews().size() == 0)) {
			views = new ArrayList<String>();
			views.add(view);
		} else {
			getViews().add(view);
			views = getViews();
		}
		setObject(VIEWS, views);
	}

	public ArrayList<String> getViews() {
		return (ArrayList<String>) getObject(VIEWS);
	}
}
