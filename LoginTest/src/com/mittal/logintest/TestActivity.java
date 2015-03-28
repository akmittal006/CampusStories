package com.mittal.logintest;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import bolts.Continuation;
import bolts.Task;

import com.ibm.mobile.services.data.IBMQuery;

public class TestActivity extends Activity {
	
	private ArrayAdapter<String> adapter;
	private ArrayList<Story> mStories;
	private ListView lv;
	private ArrayList<String> titles;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		titles = new ArrayList<String>();
		mStories = new ArrayList<Story>();
		
		titles.add("hi hello");
		lv = (ListView) findViewById(R.id.storiesList);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titles);
		lv.setAdapter(adapter);
		
		Log.e("blahhh", "retrieving stories called");
		IBMQuery<Story> query = IBMQuery.queryForClass("Story");
		query.find().continueWith(new Continuation<List<Story>, Void>() {

			@Override
			public Void then(Task<List<Story>> task) throws Exception {
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
					
					adapter.notifyDataSetChanged();
					Log.i("main", "outloop");
					//onResume();
				
			
				return null;
			}
		},Task.UI_THREAD_EXECUTOR);
		setItemClickListener();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//adapter.notifyDataSetChanged();
//		Log.e("blahhh", "retrieving stories called");
//		IBMQuery<Story> query = IBMQuery.queryForClass("Story");
//		query.find().continueWith(new Continuation<List<Story>, Void>() {
//
//			@Override
//			public Void then(Task<List<Story>> task) throws Exception {
//				// TODO Auto-generated method stub
//				if (task.isFaulted()) {
//					Log.e("main ", "unable to retrieve");
//					return null;
//				}
//					//mStories.clear();
//					//titles.clear();
//					for (int i = 0; i < 2; i++) {
//
//						Story child = task.getResult().get(i);
//						Log.i("main", "loop" + i);
//						//mStories.add(child);
//						titles.add(child.getTitle());
//						Log.e("title" , titles.get(i));
//					}
//					adapter.notifyDataSetChanged();
//					
//			
//				return null;
//			}
//		});
//		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.test, menu);
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
	
	public void setItemClickListener() {
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(TestActivity.this, FullscreenActivity.class);
				intent.putExtra("title", mStories.get(position).getTitle());
				intent.putExtra("author", mStories.get(position).getAuthor());
				intent.putExtra("body", mStories.get(position).getStoryBody());
				startActivity(intent);
				
			}
		});
	}
}
