package com.campuschatter;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.ParseException;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.example.campuschatter.R;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class HomePage extends Activity {
	private TextView myName;
	private TextView myStatus;
	
	private TextView hisName;
	private TextView herName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.campuschatter_home_page);
		
		myName = (TextView) findViewById(R.id.MyName);
		myStatus = (TextView) findViewById(R.id.MyStatus);
		
		hisName = (TextView) findViewById(R.id.HisName);
		herName = (TextView) findViewById(R.id.HerName);
		
		ParseUser me = ParseUser.getCurrentUser();
		myName.setText(me.getUsername());
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("status");
		query.whereEqualTo("user", me);
		query.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> contents, ParseException e) {
		        if (e == null) {
		            myStatus.setText(contents.get(0).getString("status"));
		        } else {
		        	
		        }
		    }

			@Override
			public void done(List<ParseObject> objects,
					com.parse.ParseException e) {
				// TODO Auto-generated method stub
				
			}
		});
	

		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home_page, menu);
		return true;
	}
	
	public void postStatus(View button){
		Intent intent = new Intent(this, PostActivity.class);
		startActivity(intent);
	}

}
