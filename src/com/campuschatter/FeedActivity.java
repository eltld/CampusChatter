package com.campuschatter;

import java.util.List;

import com.example.campuschatter.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class FeedActivity extends Activity {
	private final int POST_STORY_CODE = 1;

	private TextView myName;
	private TextView myStatus;

	private TextView hisName;
	private TextView herName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.campuschatter_feed);

		// these three are just demo, need to change afterwards
		setTextView();
		getMeWithStatus();

		ParseUser me = ParseUser.getCurrentUser();
		myName.setText(me.getUsername());

		ImageView postLink = (ImageView) findViewById(R.id.post_link);
		postLink.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				postLinkEventHandler(view);
			}
		});
	}

	private void setTextView() {
		myName = (TextView) findViewById(R.id.MyName);
		myStatus = (TextView) findViewById(R.id.MyStatus);

		hisName = (TextView) findViewById(R.id.HisName);
		herName = (TextView) findViewById(R.id.HerName);
	}

	// this is used for testing parse, need to change afterwards
	private void getMeWithStatus() {
		ParseUser me = ParseUser.getCurrentUser();
		myName.setText(me.getUsername());

		ParseQuery<ParseObject> query = ParseQuery.getQuery("status");
		query.whereEqualTo("user", me);
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> contents, ParseException e) {
				if (e == null) {
					myStatus.setText(contents.get(0).getString("status"));
				} else {

				}
			}
		});
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
					Toast.LENGTH_SHORT).show();
		}
	}
}
