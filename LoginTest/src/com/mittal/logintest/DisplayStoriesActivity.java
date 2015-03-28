package com.mittal.logintest;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import bolts.Continuation;
import bolts.Task;

import com.ibm.mobile.services.data.IBMQuery;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;

public class DisplayStoriesActivity extends Activity {

	private ListView storiesView;
	private ArrayList<String> titles;
	private ArrayList<Story> mStories;
	private EditText yearEditText;
	private RadioGroup dateGroup;
	private RadioButton beforeRadio;
	private RadioButton afterRadio;
	private RadioButton exactRadio;
	private Button filterButton;
	private SlidingUpPanelLayout mLayout;
	private ArrayList<Story> filteredStories;
	private ArrayList<String> filteredTitles;
	private StoryAdapter storyAdapter;
	private DatabaseHandler db;
	private TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_display_stories);
		storiesView = (ListView) findViewById(R.id.storiesView);
		dateGroup = (RadioGroup) findViewById(R.id.dateGroup);
		beforeRadio = (RadioButton) findViewById(R.id.beforeRadio);
		afterRadio = (RadioButton) findViewById(R.id.afterRadio);
		exactRadio = (RadioButton) findViewById(R.id.exactRadio);
		filterButton = (Button) findViewById(R.id.filterButton);
		mLayout = (SlidingUpPanelLayout) findViewById(R.id.filterSliding_layout);
		yearEditText = (EditText) findViewById(R.id.yearEditText);
		tv = (TextView) findViewById(R.id.tv);
		Typeface type = Typeface.createFromAsset(getAssets(),"fonts/vavont.ttf");
		tv.setTypeface(type);
			
			getWindow().setSoftInputMode(
				    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
				);
		
		titles = new ArrayList<String>();
		mStories = new ArrayList<Story>();
		storyAdapter = new StoryAdapter(this, mStories);

		// adding click listner to filter button
		filterButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean error = true;
				View focusView = null;
				int year =0;
				try {
					year = Integer.parseInt(yearEditText.getText().toString());
					if (year > 1949 && year < 2021) {
						error = false;
					} else {
						error = true;
						focusView = yearEditText;
					}
				} catch (Exception e) {
					// handle error
					error = true;
					Log.e("Display stories", "not valid string unbale to parse");
					focusView = yearEditText;
				}

				if (!error) {
					// get selected radio
					if (dateGroup.getCheckedRadioButtonId() == R.id.beforeRadio) {
						filterBeforeYear(year);
					} else if (dateGroup.getCheckedRadioButtonId() == R.id.afterRadio) {
						filterAfterYear(year);
					} else {
						filterForYear(year);
					}
					// sliding down the panel
					if (mLayout != null
							&& (mLayout.getPanelState() == PanelState.EXPANDED || mLayout
									.getPanelState() == PanelState.ANCHORED)) {
						mLayout.setPanelState(PanelState.COLLAPSED);
					}
				} else {

					if (focusView != null) {
						// set error and request focus
						yearEditText
								.setError("Enter Valid Date Between 1950 and 2020");
						focusView.requestFocus();
					} else {
						Toast.makeText(DisplayStoriesActivity.this,
								"Sorry unable to filter", Toast.LENGTH_SHORT).show();
					}
				}
				
			}
		});

		
//		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//				android.R.layout.simple_list_item_1, titles);
		storiesView.setAdapter(storyAdapter);
		storiesView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(DisplayStoriesActivity.this, FullscreenActivity.class);
				intent.putExtra("title", mStories.get(position).getTitle());
				intent.putExtra("author", mStories.get(position).getAuthor());
				intent.putExtra("body", mStories.get(position).getStoryBody());
				startActivity(intent);
				
			}
		});
		Bundle extras = getIntent().getExtras();
		String catName;

		if (extras != null) { 
			
			catName = extras.getString("catName");
			Log.e("intent received", "" + catName);

			if(catName.equals(Category.ALL)) {
				Log.e("main ", "unable to retrieve");
				
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
							
							
							
							storyAdapter.notifyDataSetChanged();
							Log.i("main", "outloop");
							//onResume();
						
					
						return null;
					}
				},Task.UI_THREAD_EXECUTOR);
				
			} else {
				IBMQuery<Story> query = IBMQuery.queryForClass("Story");
				query.whereKeyEqualsTo("category", catName);
				query.find().continueWith(new Continuation<List<Story>, Void>() {

					@Override
					public Void then(Task<List<Story>> task) throws Exception {
						if (task.isFaulted()) {
							Log.e("display stories", "failed to load cat stories");
							return null;
						}
						if (!isFinishing()) {
							titles.clear();
							mStories.clear();
							for (Story child : task.getResult()) {
								Log.e("display stories", "loop " + child.getTitle()
										+ " " + child.getAuthor());
								titles.add(child.getTitle());
								mStories.add(child);
							}
							storyAdapter.notifyDataSetChanged();
						}
						return null;
					}
				}, Task.UI_THREAD_EXECUTOR);
			}
			
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_stories, menu);
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

	@Override
	public void onBackPressed() {
		if (mLayout != null
				&& (mLayout.getPanelState() == PanelState.EXPANDED || mLayout
						.getPanelState() == PanelState.ANCHORED)) {
			mLayout.setPanelState(PanelState.COLLAPSED);
		} else {
			super.onBackPressed();
		}

	}

	public void filterBeforeYear(int year) {
		
		filteredStories = new ArrayList<Story>();
		filteredTitles = new ArrayList<String>();
		
		filteredStories.clear();
		filteredTitles.clear();
		
		for(Story story : mStories) {
			int filterYear = story.getYearFromDate();
			Log.e("Display story" , "year-" + filterYear);
			if(filterYear <= year) {
				filteredStories.add(story);
				filteredTitles.add(story.getTitle());
				Log.i("filtered story" , story.getDate());
			}
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, filteredTitles);
		storiesView.setAdapter(adapter);
		adapter.notifyDataSetChanged();

	}

	public void filterAfterYear(int year) {
		
		filteredStories = new ArrayList<Story>();
		filteredTitles = new ArrayList<String>();
		
		filteredStories.clear();
		filteredTitles.clear();
		
		for(Story story : mStories) {
			int filterYear = story.getYearFromDate();
			Log.e("Display story" , "year-" + filterYear);
			if(filterYear >= year) {
				filteredStories.add(story);
				filteredTitles.add(story.getTitle());
				Log.i("filtered story" , story.getDate());
			}
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, filteredTitles);
			storiesView.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		}

	}

	public void filterForYear(int year) {
		
		filteredStories = new ArrayList<Story>();
		filteredTitles = new ArrayList<String>();
		
		filteredStories.clear();
		filteredTitles.clear();
		
		for(Story story : mStories) {
			int filterYear = story.getYearFromDate();
			Log.e("Display story" , "year-" + filterYear);
			if(filterYear == year) {
				filteredStories.add(story);
				filteredTitles.add(story.getTitle());
				Log.i("filtered story" , story.getDate());
			}
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, filteredTitles);
			storiesView.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		}

	}

}
