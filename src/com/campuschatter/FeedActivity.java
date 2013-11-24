package com.campuschatter;

import java.util.List;

import DBLayout.Story;
import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.campuschatter.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class FeedActivity extends Activity {
	private final int POST_STORY_CODE = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.campuschatter_feed);

		// dynamically load rows from parse
		loadRows();
		
		// switch to post page
		ImageView postLink = (ImageView) findViewById(R.id.post_link);
		postLink.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				postLinkEventHandler(view);
			}
		});
		
		// call the police directly when tap the phone icon
		ImageView callPoliceLink = (ImageView) findViewById(R.id.callPolice);
		callPoliceLink.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(Intent.ACTION_CALL,
						Uri.parse("tel:" + getResources().getString(R.string.campus_police_number)));
				startActivity(intent);
				
			}
		});
	}
	
	@Override
	protected void onResume() {
		Log.d("Reloading", "Called by onResume");
		loadRows();
		super.onResume();
	}
	
	private void loadRows() {
		Log.i("Reloading", "Sending query for updated stories");
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Story");
		query.orderByDescending("updatedAt").include("author");
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> stories, ParseException e) {
				if (e == null) {
					TableLayout tbl = (TableLayout)findViewById(R.id.feedTable);
					tbl.removeAllViews();
					Log.i("Reloading", "Cleared table");
					int i = 0;
					for (ParseObject story : stories) {
						tbl.addView(createRow(story), i++);
					}
					Log.i("Reloading", "Repopulated table");
				} else {
					Toast.makeText(getApplicationContext(),
							"Unable to load from db", Toast.LENGTH_SHORT).show(); 
					return;
				}					
			}
		});
	}
	
	private TableRow createRow(ParseObject story) {
		// Get data from ParseObject story
		String title = story.getString("title");
		String description = story.getString("description");
		String author = story.getParseUser("author").getUsername();
		int mediaType = story.getInt("mediaType");
		byte[] byteArr = null;
		if (mediaType != Story.NO_MEDIA) {
			try {
				byteArr = story.getParseFile("media").getData();
			} catch (ParseException e) {
				// It's okay; just a story without media
			}
		}
		
		// Populate TableRow with media, title, description, and author
		TableRow tr = (TableRow)LayoutInflater.from(
				getApplicationContext()).inflate(R.layout.story_item, null);
		ImageView ivIcon = (ImageView)tr.findViewById(R.id.story_image);
		
		if (mediaType == Story.IMAGE_TYPE) {
			ivIcon.setImageBitmap(BitmapFactory.decodeByteArray(
					byteArr, 0, byteArr.length));
		} else if (mediaType == Story.VIDEO_TYPE) {
			
		} else if (mediaType == Story.AUDIO_TYPE) {
			// default microphone image
		} else {
			// default empty image
			ivIcon.setImageResource(R.drawable.user_icon);
		}
		
		TextView tvTitle = (TextView)tr.findViewById(R.id.story_title);
		tvTitle.setText(title);
		
		TextView tvAuthor = (TextView)tr.findViewById(R.id.story_author);
		tvAuthor.setText("by " + author);
		
		TextView tvDesc = (TextView)tr.findViewById(R.id.story_description);
		tvDesc.setText(description);
		return tr;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.feed, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_logout:
			logout();
			return true;
		case R.id.action_refresh:
			loadRows();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void postLinkEventHandler(View view) {
		Intent intent = new Intent(this, PostActivity.class);
		startActivityForResult(intent, POST_STORY_CODE);
	}

	public void logout() {
		ParseUser.logOut();
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == POST_STORY_CODE) {
			String toastMsg = resultCode == RESULT_OK ? "Posted story to feed"
					: "Did not post story";
			Toast.makeText(getApplicationContext(), toastMsg,
					Toast.LENGTH_LONG).show();
		}
	}
	
	
}
