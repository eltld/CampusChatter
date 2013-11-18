package com.campuschatter;

import com.example.campuschatter.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseUser;

public class FeedActivity extends Activity {
	private final int POST_STORY_CODE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.campuschatter_feed);

		ImageView postLink = (ImageView)findViewById(R.id.post_link);
		postLink.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				postLinkEventHandler(view);
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
			String toastMsg = resultCode == RESULT_OK
					? "Posted story to feed"
					: "Did not post story";
			Toast.makeText(getApplicationContext(), toastMsg, Toast.LENGTH_SHORT).show();
		}
	}
}
