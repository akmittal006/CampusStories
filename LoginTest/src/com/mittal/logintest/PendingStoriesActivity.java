package com.mittal.logintest;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class PendingStoriesActivity extends Activity {
	
	private ArrayList<Story> mStories;
	private DraftAdapter adapter;
	private DatabaseHandler db;
	private ListView lv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pending_stories);
		setTitle("Your Stories");
		lv = (ListView)findViewById(R.id.draftsView);
		db = new DatabaseHandler(this);
		mStories = db.getStories();
		Log.i("darfts", mStories.size() + "");
		adapter = new DraftAdapter(this, mStories);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Story story = mStories.get(position);
				//Log.e("drafts", story.getStoryBody());
				Intent intent = new Intent(PendingStoriesActivity.this, WriteStoryActivity.class);
				intent.putExtra("title", story.getTitle());
				intent.putExtra("date", story.getDate());
				intent.putExtra("cat", story.getCategory());
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pending_stories, menu);
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
}
