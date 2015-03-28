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

public class DraftAdapter extends ArrayAdapter<Story> {

	private Context mContext;
	private List<Story> stories;
	 ViewHolder holder;
	Story story;
	DatabaseHandler db;

	public DraftAdapter(Context context, List<Story> objects) {
		super(context, R.layout.draft_item, objects);
		// TODO Auto-generated constructor stub
		mContext = context;
		stories = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.draft_item, null);
			holder = new ViewHolder();
			holder.titleView = (TextView) convertView
					.findViewById(R.id.titleView);
			holder.catView = (TextView) convertView
					.findViewById(R.id.catView);
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
		Log.i("drats adapter", holder.catView.getText() + "");
		holder.catView.setText(story.getCategory());
		holder.dateView.setText(story.getDate());
		
		Typeface type = Typeface.createFromAsset(mContext.getAssets(),"fonts/RAVEN___.TTF");
		holder.catView.setTypeface(type);
		Typeface type2 = Typeface.createFromAsset(mContext.getAssets(),"fonts/veterantypewriter.ttf");
		holder.titleView.setTypeface(type2);
		
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
		TextView catView;
		TextView dateView;
		//TextView interestingView;
		//Button intButton;
		// TextView frndLabel;
	}

	

}
