package com.mittal.logintest;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import bolts.Continuation;
import bolts.Task;

import com.ibm.mobile.services.data.IBMDataObject;
import com.ibm.mobile.services.data.IBMDataObjectSpecialization;
import com.ibm.mobile.services.data.IBMQuery;

@IBMDataObjectSpecialization("Story")
public class Story extends IBMDataObject{
	
//	public String title;
//	public String author;
//	public boolean anonymous;
//	public String Date;
//	public int views;
//	public int interesting;
	public static final String CLASS_NAME = "Story";
	public static final String TITLE  =  "title";
	public static final String AUTHOR = "author";
	public static final String ANONYMOUS = "anonymous";
	public static final String DATE = "date";
	public static final String VIEWS = "views";
	public static final String INTERESTING = "interesting";
	public static final String CATEGORY = "category";
	public static final String STORY_BODY  = "storyBody";
	public static final String VIEWED_BY = "viewed_by";
	public static final String INTERESTED = "interested";
	public static final String YEAR = "year";
	public static final String MONTH = "month";
	
	
	public Story () {
		
	}
	public Story(String title, String author, String anonymous, String date,
			String category,String body) {
		// TODO Auto-generated constructor stub
		if(title != null) {
			Log.e("story", title);
			setTitle(title);
		}
		if(author != null) {
			setAuthor(author);
		}
		if(anonymous != null) {
			if(anonymous.equals("0")) {
				setAnonymous(false);
			} else if(anonymous.equals("1") ) {
				setAnonymous(true);
			}
			
		}
		if(date != null) {
			setDate(date);
		}
		if(category != null) {
			setCategory(category);
		}
		if(body != null) {
			setStoryBody(body);
		}
		
	}
	public String getTitle() {
		return (String)getObject(TITLE);
	}
	public void setTitle(String title) {
		setObject(TITLE, title);
	}
	public String getAuthor() {
		return (String)getObject(AUTHOR);
	}
	public void setAuthor(String author) {
		setObject(AUTHOR, author);
	}
	public boolean isAnonymous() {
		return (Boolean)getObject(ANONYMOUS);
	}
	public void setAnonymous(boolean anonymous) {
		setObject(ANONYMOUS, anonymous);
	}
	public String getDate() {
		return (String)getObject(DATE);
	}
	public void setDate(String date) {
		setObject(DATE, date);
	}
	public int getViews() {
		return (Integer)getObject(VIEWS);
	}
	public void setViews(int views) {
		setObject(VIEWS, VIEWS);
	}
	public int getInteresting() {
		return (Integer)getObject(INTERESTING);
	}
	public void setInteresting(int interesting) {
		setObject(INTERESTING, interesting);
	}
	
	public String getCategory() {
		return (String)getObject(CATEGORY);
	}
	public void setCategory(String category) {
		setObject(CATEGORY, category);
	}
	
	public String getStoryBody() {
		return (String)getObject(STORY_BODY);
	}
	public void setStoryBody(String storyBody) {
		setObject(STORY_BODY, storyBody);
	}
	public ArrayList<String> getViewedBy() {
		return (ArrayList<String>)getObject(VIEWED_BY);
	}
	public void setViewedBy(ArrayList<String> users) {
		setObject(VIEWED_BY, users);
	}
	public ArrayList<Interested> getInterested() {
		Log.e("story", " get caleesd" + "");
		return (ArrayList<Interested>)getObject(INTERESTED);
	}
	
	public void addInterested(Interested intrsted) {
		
		Log.e("story", "caleesd" + "");
		setObject(INTERESTED, new ArrayList<Interested>());
		Log.e("story", "caleesd 2" + "");

		ArrayList<Interested> intrsted_people;
		

		if ( getInterested().size() == 0) {
			intrsted_people = new ArrayList<Interested>();
			intrsted_people.add(intrsted);
		} else {
			intrsted_people = getInterested();
			intrsted_people.add(intrsted);
		}
		Log.e("story", intrsted_people.size() + "");
		setObject(INTERESTED, intrsted_people);

	}
	
	
	public int getYearFromDate() {
		String yearString = "";
			int lastIndex = getDate().length() - 4;
			yearString = getDate().substring(lastIndex);
			Log.e("get Year", getDate().substring(lastIndex));
			
		int year = Integer.parseInt(yearString);
		return year;
	}
	
	public int getMonth() {
		return (Integer)getObject(MONTH);
	}
	public void setMonth(int month) {
		setObject(MONTH, month);
	}
	
	public int getYear() {
		return (Integer)getObject(YEAR);
	}
	

	
	public void setYear(int month) {
		setObject(YEAR, month);
	}
	

}
