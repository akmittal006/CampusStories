package com.mittal.logintest;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "usersData3";

	private static final String TABLE_USER = "User";
	private static final String TABLE_STORIES = "Stories";
	private static final String TABLE_USER_INTEREST = "User_interest";
	// Table user columns
	private static final String USER_NAME = "UserName";
	private static final String NAME = "Name";
	private static final String KEY_ID = "Id";
	// Table user_interest columns
	public static final String INTEREST_IN = "interest_in";
	public static final String INTEREST_AS = "interest_as";
	private static final String INTEREST_KEY_ID = "Id";
	// Table Stories columns
	public static final String TITLE = "title";
	public static final String AUTHOR = "author";
	public static final String ANONYMOUS = "anonymous";
	public static final String DATE = "date";
	public static final String CATEGORY = "category";
	public static final String STORY_BODY = "storyBody";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);

	}

	// create user table
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_USER + "("
				+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + USER_NAME
				+ " TEXT, " + NAME + " TEXT " + ")";
		db.execSQL(CREATE_CONTACTS_TABLE);
		// create user_interest table
		String CREATE_TABLE_USER_INTEREST = "CREATE TABLE "
				+ TABLE_USER_INTEREST + "(" + INTEREST_KEY_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + INTEREST_IN
				+ " TEXT, " + INTEREST_AS + " TEXT " + ")";
		db.execSQL(CREATE_TABLE_USER_INTEREST);

		// create story table
		String CREATE_STORIES_TABLE = "CREATE TABLE " + TABLE_STORIES + "("
				+ TITLE + " TEXT PRIMARY KEY , " + AUTHOR + " TEXT, "
				+ ANONYMOUS + " INTEGER, " + DATE + " TEXT, " + CATEGORY
				+ " TEXT, " + STORY_BODY + " TEXT" + ")";
		db.execSQL(CREATE_STORIES_TABLE);
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER + "," + TABLE_STORIES
				+ "," + TABLE_USER_INTEREST);

		// Create tables again
		onCreate(db);
	}

	

	public void addUser(User user) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(USER_NAME, user.getUsername());
		values.put(NAME, user.getName());

		// Inserting Row
		db.insert(TABLE_USER, null, values);
		db.close(); // Closing database connection
	}

	public boolean addStory(Story story) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(AUTHOR, story.getAuthor());
		// values.put(ANONYMOUS, story.isAnonymous());
		values.put(DATE, story.getDate());
		values.put(CATEGORY, story.getCategory());
		values.put(STORY_BODY, story.getStoryBody());
		if (isTitleUnique(story.getTitle())) {
			values.put(TITLE, story.getTitle());
			db.insert(TABLE_STORIES, null, values);
			db.close();
			return true;
		} else {
			return false;
		}

	}
	
	public boolean addInterest(Interest interest){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		//
		values.put(INTEREST_IN, interest.getIn());
		values.put(INTEREST_AS, interest.getAs());
		if(!isAlreadyIntrestedIn(interest.getIn())){
			db.insert(TABLE_USER_INTEREST, null, values);
		}
		
		return false;
	}

	private String ToString(boolean anon) {

		if (anon) {
			return "true";
		}
		return "false";
	}

	private boolean ToBoolean(String anon) {

		if (anon.equals("true")) {
			return true;
		}
		return false;
	}
 
	public User getUser() {
		User user = new User();
		String selectQuery = "SELECT  * FROM " + TABLE_USER;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			user.setUsername(cursor.getString(1));
			user.setName(cursor.getString(2));

		} else {
			return null;
		}
		db.close();

		return user;
	}
	// get interested_in_stories from object_id as string
 public boolean isAlreadyIntrestedIn(String object_id) {
	 SQLiteDatabase db = this.getReadableDatabase();
	 Cursor cursor = db.query(TABLE_USER_INTEREST, new String[] { INTEREST_IN, INTEREST_AS,
			 INTEREST_KEY_ID }, INTEREST_IN + "=?",
				new String[] { object_id }, null, null, null, null);
	 Log.e("handler", "" + cursor.getCount() );
		 if(cursor.getCount() == 0) {
			 db.close();
			 return false;
		 } else {
			 db.close();
			 return true;
		 }
		 
		 
 }
	// get story from title
	public Story getStoryFromTitle(String title) {

		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_STORIES, new String[] { TITLE, AUTHOR,
				ANONYMOUS, DATE, CATEGORY, STORY_BODY }, TITLE + "=?",
				new String[] { title }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Story story = new Story(cursor.getString(0), cursor.getString(1),
				cursor.getString(2), cursor.getString(3), cursor.getString(4),
				cursor.getString(5));
		// return story
		return story;
	}

	public boolean isTitleUnique(String title) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_STORIES, new String[] { TITLE, AUTHOR,
				ANONYMOUS, DATE, CATEGORY, STORY_BODY }, TITLE + "=?",
				new String[] { title }, null, null, null, null);
		if (cursor != null) {
			if (cursor.getCount() > 0) {
				cursor.moveToFirst();
				Log.i("handler", cursor.getString(0) + " " + title);
				return false;
			} else {
				return true;
			}
		}
		return true;
	}

	public void updateUser(User user) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(USER_NAME, user.getUsername());
		values.put(NAME, user.getName());

		db.update(TABLE_USER, values, KEY_ID + " = ?", new String[] { "1" });
		db.close();
	}

	public boolean updateStory(Story story) {
		boolean fluffy = false;
		SQLiteDatabase db = this.getWritableDatabase();
		String title = story.getTitle();
		ContentValues values = new ContentValues();
		values.put(STORY_BODY, story.getStoryBody());
		int tuffy;
		tuffy = db.update(TABLE_STORIES, values, TITLE + " = ?",
				new String[] { title });
		db.close();
		if (tuffy > 0)
			return true;
		return fluffy;

	}
	
	public ArrayList<Story> getStories() {
		
		String selectQuery = "SELECT  * FROM " + TABLE_STORIES;
		
		 SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		ArrayList<Story> contactList = new ArrayList<Story>();
       
 
        
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	Story story = new Story();
                story.setAuthor(cursor.getString(1));
                story.setTitle(cursor.getString(0));
                story.setDate(cursor.getString(3));
                story.setCategory(cursor.getString(4));
                story.setStoryBody(cursor.getString(5));
                // Adding contact to list
                contactList.add(story);
            } while (cursor.moveToNext());
        } db.close();
 
        // return contact
        return contactList;
		
	}
	
	public void deleteInterest(String object_id){
		SQLiteDatabase db = this.getReadableDatabase();
		 Cursor cursor = db.query(TABLE_USER_INTEREST, new String[] { INTEREST_IN, INTEREST_AS,
				 INTEREST_KEY_ID }, INTEREST_IN + "=?",
					new String[] { object_id }, null, null, null, null);
		 if(cursor!= null){
			 db.delete(TABLE_USER_INTEREST, INTEREST_IN + " = ?", new String[] { "object_id" });
		 }
		 
	}

	public boolean updateStory(String oldTitle, Story story) {
		boolean fluffy = false;
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(STORY_BODY, story.getStoryBody());
		values.put(TITLE, story.getTitle());
		values.put(AUTHOR, story.getAuthor());
		values.put(ANONYMOUS, story.isAnonymous());
		values.put(DATE, story.getDate());
		values.put(CATEGORY, story.getCategory());
		values.put(STORY_BODY, story.getStoryBody());
		int tuffy;
		tuffy = db.update(TABLE_STORIES, values, TITLE + " = ?",
				new String[] { oldTitle });
		db.close();
		if (tuffy > 0)
			return true;
		return fluffy;

	}

	// Deleting single contact
	public void DeleteUser(User user) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_USER, KEY_ID + " = ?", new String[] { "1" });
		db.close();

	}

	// Deleting single story
	public void DeleteStory(String title) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_USER, TITLE + " = ?", new String[] { title });
		db.close();
	}
	public void DeleteAllUsers(){
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_USER, null, null);
		db.delete(TABLE_STORIES, null, null);
		db.close();
	}

}
