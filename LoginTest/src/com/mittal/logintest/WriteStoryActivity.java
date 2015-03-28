package com.mittal.logintest;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class WriteStoryActivity extends Activity {

	private EditText dateEditor;
	private EditText titleEditor;
	private Spinner categorySpinner;
	private Button continueButton;
	public int month;
	public int year;
	private String body = "";
	boolean flag = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_write_story);
		dateEditor = (EditText) findViewById(R.id.datePicker);
		titleEditor = (EditText) findViewById(R.id.titleTextView);
		categorySpinner = (Spinner) findViewById(R.id.categorySpinner);
		continueButton = (Button) findViewById(R.id.continueButton);
		handleSpinner(categorySpinner);
		
		Log.e("flag1", "" + flag);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			dateEditor.setText(extras.getString("date"));
			titleEditor.setText(extras.getString("title"));
			int position = 0;
			if (extras.getString("cat") == Category.ACHIEVEMENT) {
				position = 0;
			} else if (extras.getString("cat") == Category.CHUTZPAH) {
				position = 1;
			} else if (extras.getString("cat") == Category.CLUB) {
				position = 2;
			} else if (extras.getString("cat") == Category.EXPERIENCE) {
				position = 3;
			} else if (extras.getString("cat") == Category.HAPPENING) {
				position = 4;
			} else if (extras.getString("cat") == Category.LOVE) {
				position = 5;
			} else if (extras.getString("cat") == Category.PLACEMENT) {
				position = 6;
			} else if (extras.getString("cat") == Category.SAD) {
				position = 7;
			}
			categorySpinner.setSelection(position);
			body = extras.getString("body");
			flag = false;
			Log.e("flag2", "" + flag);
		}
		final SimpleDateFormat dateFormatter = new SimpleDateFormat(
				"dd-MM-yyyy", Locale.US);

		final SimpleDateFormat dateFormatter1 = new SimpleDateFormat("MM",
				Locale.US);
		final SimpleDateFormat dateFormatter2 = new SimpleDateFormat("yyyy",
				Locale.US);

		Calendar newCalendar = Calendar.getInstance();
		final DatePickerDialog dialog = new DatePickerDialog(this,
				new OnDateSetListener() {

					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						Calendar newDate = Calendar.getInstance();
						newDate.set(year, monthOfYear, dayOfMonth);
						dateEditor.setText(dateFormatter.format(newDate
								.getTime()));
						month = Integer.parseInt(dateFormatter1.format(newDate
								.getTime()));
						year = Integer.parseInt(dateFormatter2.format(newDate
								.getTime()));
						Log.e("continue ", " exts" + month + " " + year);
					}

				}, newCalendar.get(Calendar.YEAR),
				newCalendar.get(Calendar.MONTH),
				newCalendar.get(Calendar.DAY_OF_MONTH));

		dateEditor.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.show();

			}
		});
		dateEditor.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				// TODO Auto-generated method stub
				dialog.show();
				return false;
			}
		});
		continueButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String title = null;
				String date = null;
				String category = null;
				View focusView = dateEditor;
				boolean not_error;
				if (!titleEditor.equals("")) {
					title = titleEditor.getText().toString();
					Log.e("write story", title);
					not_error = true;
				} else {
					titleEditor.setError("choose title");
					focusView = titleEditor;
					not_error = false;
				}
				if (!dateEditor.equals("")) {
					date = dateEditor.getText().toString();
					not_error = true;
				} else {
					dateEditor.setError("choose Date");
					focusView = dateEditor;
					not_error = false;
				}
				if (!categorySpinner.getSelectedItem().equals("")) {
					category = (String) categorySpinner.getSelectedItem();
					not_error = true;
				} else {

					focusView = categorySpinner;
					not_error = false;
				}
				if (not_error) {
					Story story = new Story(title, null, null, date, category,
							null);
					Log.e("write story", story.getCategory() + " , title -"
							+ story.getTitle());
					DatabaseHandler db = new DatabaseHandler(
							WriteStoryActivity.this);
					// handle autosave in future impppppp
					if (flag) {
						if (db.addStory(story)) {
							Intent intent = new Intent(WriteStoryActivity.this,
									ContinueStoryActivity.class);
							intent.putExtra("title", story.getTitle());
							intent.putExtra("month", month);
							intent.putExtra("year", year);
							intent.putExtra("body", body);
							startActivity(intent);
						} else {
							titleEditor.setError("enter unique title");
							focusView = titleEditor;
							focusView.requestFocus();
						}
					} else {
						Intent intent = new Intent(WriteStoryActivity.this,
								ContinueStoryActivity.class);
						intent.putExtra("title", story.getTitle());
						intent.putExtra("month", month);
						intent.putExtra("year", year);
						intent.putExtra("body", body);
						startActivity(intent);
					}

				} else {
					focusView.requestFocus();
					Toast.makeText(WriteStoryActivity.this,
							"you cant leave empty fields", Toast.LENGTH_SHORT)
							.show();
					;
				}
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.write_story, menu);
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

	public void handleSpinner(Spinner spinner) {

		String[] categories = { Category.ACHIEVEMENT, Category.CHUTZPAH,
				Category.CLUB, Category.EXPERIENCE, Category.HAPPENING,
				Category.LOVE, Category.PLACEMENT, Category.SAD };
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, categories);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
	}
}
