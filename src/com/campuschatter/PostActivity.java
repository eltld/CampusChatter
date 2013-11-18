package com.campuschatter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import com.example.campuschatter.R;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class PostActivity extends Activity {
	private EditText description;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.campuschatter_post);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.post, menu);
		return true;
	}
	
	// post the status to the database
	public void postSth(View button){
		description = (EditText) findViewById(R.id.story_description);
		String content = description.getText().toString();
		ParseUser me = ParseUser.getCurrentUser();
		ParseObject status = new ParseObject("status");
		status.put("user", me);
		status.put("status", content);
		
		status.saveInBackground();
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
	}

}
