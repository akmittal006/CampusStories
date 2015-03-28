package com.mittal.logintest;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import bolts.Continuation;
import bolts.Task;

import com.ibm.mobile.services.data.IBMQuery;

public class CampusStories extends Activity {
	private LoginTestApplication app;
	private DatabaseHandler db;
	private List<Story> mStories;
	private ArrayList<String> titles;
	private Continuation<List<Story>, Void> continuer;
	ArrayAdapter<String> adapter;
	private ListView catListView;
	private ListView recentView;
	private StoryAdapter stryAdptr;
	private TextView textView;
	private ProgressBar prog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ConnectivityManager cm = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (!((ni != null) && (ni.isConnected()))) { 
			// net connected
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Oops!");
			builder.setMessage("Internet connection not available");
			builder.setPositiveButton("ok", null);
			AlertDialog dialog = builder.create();
			dialog.show();
		}
		app = (LoginTestApplication) getApplication();
		if (app.currentUser == null) {
			Intent intent = new Intent(CampusStories.this, LoginActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		}
		
		prog = (ProgressBar) findViewById(R.id.mainProg);
		//prog.setVisibility(View.VISIBLE);
		
		textView = (TextView) findViewById(R.id.textView1);
		Typeface type = Typeface.createFromAsset(getAssets(),"fonts/vavont.ttf");
		textView.setTypeface(type);
		
		setTitle("Campus Stories");
		catListView = (ListView) findViewById(R.id.categoryListView);
		recentView = (ListView) findViewById(R.id.recentView);
		final String[] categories = {Category.ACHIEVEMENT, 
				Category.CHUTZPAH,
				Category.CLUB,
				Category.EXPERIENCE,
				Category.HAPPENING,
				Category.LOVE,
				Category.PLACEMENT,
				Category.SAD,
				Category.ALL};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, categories);
		catListView.setAdapter(adapter);
		catListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				Log.e("main List", "" + categories[position] + "  " + position);
				Intent intent = new Intent (CampusStories.this, DisplayStoriesActivity.class);
				intent.putExtra("catName", categories[position]);
				
				startActivity(intent);

				
			}
		});
		// to set side drawer
		

		// to open side drawer
		
		//

		//
		

		titles = new ArrayList<String>();
		// to open new activity


		// ListView itemsLV = (ListView)findViewById(R.id.storiesList);
		// adapter = new ArrayAdapter<String>(this,
		// android.R.layout.simple_list_item_1, titles);
		// itemsLV.setAdapter(adapter);

		mStories = new ArrayList<Story>();
		stryAdptr = new StoryAdapter(this, mStories);
		recentView.setAdapter(stryAdptr);

		db = new DatabaseHandler(this);
		if (db.getUser() == null) {
			app.currentUser = null;
		} else {
			app.currentUser = db.getUser();
		}
		

		// adapter.notifyDataSetChanged();
		 //recheckLogin();
		 
		 IBMQuery<Story> query = IBMQuery.queryForClass("Story");
			query.find().continueWith(new Continuation<List<Story>, Void>() {

				
				@Override
				public Void then(Task<List<Story>> task) throws Exception {
					prog.setVisibility(View.INVISIBLE);
					// TODO Auto-generated method stub
					if (task.isFaulted()) {
						Log.e("main ", "unable to retrieve");
						return null;
					}
					mStories.clear();
					titles.clear();
						
						for (int i = 0; i < task.getResult().size(); i++) {

							Story child = task.getResult().get(i);
							Log.i("main", "loop" + i);
							mStories.add(child);
							titles.add(child.getTitle());
							if(child.getAuthor() != null) {
								Log.e("title" , titles.get(i) + " " + child.getAuthor());
							}
						
						}
						
						
						
						stryAdptr.notifyDataSetChanged();
						Log.i("main", "outloop");
						//onResume();
					
				
					return null;
				}
			},Task.UI_THREAD_EXECUTOR);
			
			recentView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Intent intent = new Intent(CampusStories.this, FullscreenActivity.class);
					intent.putExtra("title", mStories.get(position).getTitle());
					intent.putExtra("author", mStories.get(position).getAuthor());
					intent.putExtra("body", mStories.get(position).getStoryBody());
					startActivity(intent);
					
				}
			});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// adapter.notifyDataSetChanged();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_sign_out) {
			db.DeleteUser(app.currentUser);
			IBMQuery<User> query = IBMQuery.queryForClass("User");
			query.whereKeyEqualsTo("name", app.currentUser.getName());
			query.find().continueWith(new Continuation<List<User>, Void>() {

				@Override
				public Void then(Task<List<User>> task) throws Exception {
					// TODO Auto-generated method stub
					if (task.isFaulted()) {
						return null;
					}
					if (task.getResult().size() > 0) {
						User user = task.getResult().get(0);
						if (!user.isLoggedIn()) {
							signout(user);
						}
					}

					return null;
				}
			});
			app.currentUser = null;

			Intent intent = new Intent(CampusStories.this, LoginActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			return true;
		} else if (id == R.id.action_write_story) {
			startActivity(new Intent(CampusStories.this,
					WriteStoryActivity.class));
		} else if (id == R.id.action_drafts) {
			startActivity(new Intent(CampusStories.this,
					PendingStoriesActivity.class));
		}
		return super.onOptionsItemSelected(item);
	}

	public void recheckLogin() {
		IBMQuery<User> query = IBMQuery.queryForClass("User");
		query.whereKeyEqualsTo("name", app.currentUser.getName());
		query.find().continueWith(new Continuation<List<User>, Void>() {

			@Override
			public Void then(Task<List<User>> task) throws Exception {
				// TODO Auto-generated method stub
				if (task.isFaulted()) {
					return null;
				}
				if (task.getResult().size() > 0) {
					User user = task.getResult().get(0);
					if (!user.isLoggedIn()) {
						signout(user);
					}
				}
				return null;
			}
		});

	}

	public void signout(User user) {
		db.DeleteUser(app.currentUser);
		user.setLoggedIn(false);
		user.save();
		app.currentUser = null;
		db.DeleteAllUsers();
		Intent intent = new Intent(CampusStories.this, LoginActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	public void retrieveStories() {

	}

	// public void setView() {
	// Log.e("blahhh", "done " + titles.size());
	// setListAdapter(new
	// ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, titles));
	// Log.e("blahhh", "done");
	//
	// Log.e("blahhh", "done");
	// }
}
