package com.mittal.logintest;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;
import bolts.Continuation;
import bolts.Task;

import com.ibm.mobile.services.data.IBMDataObject;
import com.ibm.mobile.services.data.IBMQuery;

public class StoryAdapter extends ArrayAdapter<Story> {

	private Context mContext;
	private List<Story> stories;
	 ViewHolder holder;
	Story story;
	DatabaseHandler db;

	public StoryAdapter(Context context, List<Story> objects) {
		super(context, R.layout.story_item, objects);
		// TODO Auto-generated constructor stub
		mContext = context;
		stories = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.story_item, null);
			holder = new ViewHolder();
			holder.titleView = (TextView) convertView
					.findViewById(R.id.titleView);
			holder.authorView = (TextView) convertView
					.findViewById(R.id.authorView);
			holder.dateView = (TextView) convertView
					.findViewById(R.id.dateView);
//			holder.interestingView = (TextView) convertView
//					.findViewById(R.id.interestingView);
//			holder.intButton = (Button) convertView
//					.findViewById(R.id.intButton);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		db = new DatabaseHandler(mContext);
		story = stories.get(position);
		holder.titleView.setText(story.getTitle());
		Typeface type = Typeface.createFromAsset(mContext.getAssets(),"fonts/RAVEN___.TTF");
		holder.authorView.setTypeface(type);
		holder.authorView.setText("by: " + story.getAuthor());
		Typeface type2 = Typeface.createFromAsset(mContext.getAssets(),"fonts/veterantypewriter.ttf");
		holder.titleView.setTypeface(type2);
		holder.dateView.setText(story.getDate());
//		Log.e("adapter" , "" + db.isAlreadyIntrestedIn(story.getObjectId()));
//		if(db.isAlreadyIntrestedIn(story.getObjectId())) {
//			holder.interestingView.setVisibility(View.VISIBLE);
//			holder.intButton.setVisibility(View.INVISIBLE);
//		} else {
//			holder.interestingView.setVisibility(View.INVISIBLE);
//			holder.intButton.setVisibility(View.VISIBLE);
//			holder.intButton.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					displayDialog();
//				}
//			});
//		}
//		
		return convertView;
	}

	private static class ViewHolder {
		TextView titleView;
		TextView authorView;
		TextView dateView;
		//TextView interestingView;
		//Button intButton;
		// TextView frndLabel;
	}

//	private void displayDialog() {
//
//		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//		builder.setTitle("Choose Please");
//		builder.setMessage("how you want to show interest in story?");
//		builder.setPositiveButton("Anonymously",
//				new DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						
//						Log.e("adapter", "anon button clicked" );
//						// TODO Auto-generated method stub
//						final Interest interest = new Interest();
//						interest.setAs(CSConstants.ANONYMOUS);
//						interest.setIn(story.getObjectId());
//						 Log.e("story aapter" , interest.getAs() +" "+ interest.getIn() + " " +story.getObjectId());
//						
//						Log.e("adapter",  db.getUser().getUsername());
//						
//						IBMQuery<Story> query = IBMQuery.queryForObjectId(story.getObjectId());
//						Log.e("adapter", "story object id" + story.getObjectId());
//						query.find().continueWith(new Continuation<List<Story>, Void>() {
//
//							@Override
//							public Void then(Task<List<Story>> task) throws Exception {
//								// TODO Auto-generated method stub
//								Interested intrsted = new Interested();
//						
//								intrsted.setAs(CSConstants.ANONYMOUS);
//								intrsted.setBy(db.getUser().getUsername());
//								if(task.isFaulted()) {
//									Log.e("adapter", "error updating story");
//									return null;
//								}
//								Log.e("adapter", " updating story" + task.getResult().size() );
//								story = task.getResult().get(0);
//								story.addInterested(intrsted);
//								Log.e("adapter", " updating story" + story.getInterested().size() );
//								Log.e("adapter", " ankur" + story.getInterested().size() );
//								story.save().continueWith(new Continuation<IBMDataObject, Void>() {
//									
//
//									@Override
//									public Void then(Task<IBMDataObject> task2)
//											throws Exception {
//										// TODO Auto-generated method stub
//										Log.e("adapter", " saved called");
//										if(task2.isFaulted()) {
//											Log.e("adapter", "error saving story adding interest");
//											return null;
//										}
//										Log.e("adapter", " saved story adding interest");
//										
//										db.addInterest(interest);
//										Log.e("adapter", " database saved story adding interest");
//										holder.interestingView.setVisibility(View.VISIBLE);
//										holder.intButton.setVisibility(View.INVISIBLE);
//										return null;
//									}
//								});
//								
//								return null;
//							}
//						},Task.UI_THREAD_EXECUTOR);
//						
//						
//					}
//				});
//		builder.setNegativeButton("Non-Anonymously",
//				new DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						// TODO Auto-generated method stub
//						
//						// TODO Auto-generated method stub
//						final Interest interest = new Interest();
//						interest.setAs(CSConstants.NON_ANONYMOUS);
//						interest.setIn(story.getObjectId());
//						final Interested intrsted = new Interested();
//						intrsted.as = CSConstants.NON_ANONYMOUS;
//						intrsted.by = db.getUser().getUsername();
//						IBMQuery<Story> query = IBMQuery.queryForClass("Story");
//						query.whereKeyEqualsTo("objectId", story.getObjectId());
//						query.find().continueWith(new Continuation<List<Story>, Void>() {
//
//							@Override
//							public Void then(Task<List<Story>> task) throws Exception {
//								// TODO Auto-generated method stub
//								if(task.isFaulted()) {
//									Log.e("adapter", "error updating story");
//									return null;
//								}
//								Log.e("adapter", " updating story" + task.getResult().size() );
//								story = task.getResult().get(0);
//								
//								story.addInterested(intrsted);
//								story.save().continueWith(new Continuation<IBMDataObject, Void>() {
//
//									@Override
//									public Void then(Task<IBMDataObject> task)
//											throws Exception {
//										// TODO Auto-generated method stub
//										if(task.isFaulted()) {
//											Log.e("adapter", "error saving story adding interest");
//											return null;
//										}
//										Log.e("adapter", " saved story adding interest");
//										
//										db.addInterest(interest);
//										holder.interestingView.setVisibility(View.VISIBLE);
//										holder.intButton.setVisibility(View.INVISIBLE);
//										return null;
//									}
//								},Task.UI_THREAD_EXECUTOR);
//								
//								return null;
//							}
//						});
//						
//						
//					}
//				});
//		AlertDialog dialog = builder.create();
//		dialog.show();
//
//	}
	

}
