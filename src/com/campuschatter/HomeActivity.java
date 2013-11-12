package com.campuschatter;

import com.example.campuschatter.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseObject;

public class HomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Parse.initialize(this, "lKTMKGxVecR5pULQb24OyAJHF8LO5rCJSR514bE9", "xXHvncTYu9dGKt87KGavwa4787DsiUXMCWBy3vKk"); 
		setContentView(R.layout.campuschatter_home);
		
		//ParseAnalytics.trackAppOpened(getIntent());
		
		/*
		 * ParseObject testObject = new ParseObject("TestObject");
		 * testObject.put("foo", "bar");
		 * testObject.saveInBackground();
		*/
		TextView signinLink = (TextView)findViewById(R.id.signin_link);
		TextView signupLink = (TextView)findViewById(R.id.signup_link);
		TextView feedLink = (TextView)findViewById(R.id.feed_link);
		
		signinLink.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				signinLinkEventHandler(view);
			}
		});
		signupLink.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				signupLinkEventHandler(view);
			}
		});
		feedLink.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				feedLinkEventHandler(view);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}
	
	public void signinLinkEventHandler(View view) {
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
	}
	
	public void signupLinkEventHandler(View view) {
		Intent intent = new Intent(this, RegisterActivity.class);
		startActivity(intent);
	}

	public void feedLinkEventHandler(View view) {
		Intent intent = new Intent(this, FeedActivity.class);
		startActivity(intent);
	}
}
