package com.mittal.logintest;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import bolts.Continuation;
import bolts.Task;

import com.ibm.mobile.services.data.IBMDataObject;

public class ContinueStoryActivity extends Activity {

	private EditText storyBodyEditor;
	public String title;
	private DatabaseHandler db;
	private LoginTestApplication app;
	private int month;
	private int year;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_continue_story);
		db = new DatabaseHandler(this);
		app = new LoginTestApplication();
		Bundle extras = getIntent().getExtras();
		
		
		storyBodyEditor = (EditText) findViewById(R.id.storyBodyEditText);
		storyBodyEditor.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
				//ContinueStoryActivity.this.getActionBar().hide();
				return false;
			}
		});
		
		if(extras != null) {
			title = extras.getString("title");
			month = extras.getInt("month");
			year = extras.getInt("year");
			Story x = db.getStoryFromTitle(title);
			//Log.e("body1", x.getStoryBody());
			storyBodyEditor.setText(db.getStoryFromTitle(title).getStoryBody());
			Log.e("body", title);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.continue_story, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.share) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					ContinueStoryActivity.this);
			
					
			builder.setTitle("How to share?");
			builder.setMessage("Which way u wanna share your story");
			builder.setNegativeButton("Anonymously", new OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					final Story story = db.getStoryFromTitle(title);
					Log.e("continue", story.getTitle());
					story.setStoryBody(storyBodyEditor.getText().toString());
					story.setAnonymous(true);
					story.setAuthor(db.getUser().getUsername());
					story.setMonth(month);
					story.setYear(year);
					Log.e("continue", story.getTitle() + " " + db.getUser().getName() );
					story.save().continueWith(new Continuation<IBMDataObject, Void>() {

						@Override
						public Void then(Task<IBMDataObject> task)
								throws Exception {
							if(task.isFaulted()) {
								Log.e("continue story activity", "not saved non-anonymous");
								return null;
							}
							Log.e("continue story activity", "yayyy saved non-anonymous");
							//db.DeleteStory(story.getTitle());
							db.updateStory(story);
							return null;
						}
					});
					
					startActivity(new Intent(ContinueStoryActivity.this, CampusStories.class));
				}
			});
			builder.setPositiveButton("Non-Anonymously", new OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					final Story story = db.getStoryFromTitle(title);
					Log.e("continue", story.getTitle());
					story.setStoryBody(storyBodyEditor.getText().toString());
					story.setAnonymous(false);
					story.setAuthor(db.getUser().getName());
					story.setMonth(month);
					story.setYear(year);
					Log.e("continue", story.getTitle() + " " + db.getUser().getName() );
					story.save().continueWith(new Continuation<IBMDataObject, Void>() {

						@Override
						public Void then(Task<IBMDataObject> task)
								throws Exception {
							if(task.isFaulted()) {
								Log.e("continue story activity", "not saved non-anonymous");
								return null;
							}
							Log.e("continue story activity", "yayyy saved non-anonymous");
							//db.DeleteStory(story.getTitle());
							db.updateStory(story);
							return null;
						}
					});
					Intent intent = new Intent(ContinueStoryActivity.this, CampusStories.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);

				}
				
			});
			AlertDialog dialog = builder.create();
			dialog.show();
			return true;
		} else if (id == R.id.update) {
			Log.i("continue activity", title);
			Story story = db.getStoryFromTitle(title);
			story.setStoryBody(storyBodyEditor.getText().toString());
			db.updateStory(story);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
