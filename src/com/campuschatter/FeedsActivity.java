package com.campuschatter;

import com.example.campuschatter.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class FeedsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.campuschatter_feeds);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.feeds, menu);
		return true;
	}

}
