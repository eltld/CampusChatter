package activities;

import java.util.List;

import DBLayout.Story;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.campuschatter.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import entities.ActivitiesHelper;
import entities.BluetoothAPI;

public class FeedActivity extends Activity {
	private final int POST_STORY_REQUEST = 1;
	private final int BLUETOOTH_REQUEST = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.campuschatter_feed);

		// dynamically load rows from parse
		loadRows();

		ImageView reportLink = (ImageView) findViewById(R.id.reportPolice);
		reportLink.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				reportLinkEventHandler(view);
			}
		});

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
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
						+ getResources().getString(
								R.string.campus_police_number)));
				startActivity(intent);

			}
		});

		// Bluetooth - take picture then share with friend
		ImageView bluetoothLink = (ImageView) findViewById(R.id.bluetooth);
		bluetoothLink.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent cameraIntent = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(cameraIntent, BLUETOOTH_REQUEST);
			}
		});
	}

	@Override
	protected void onResume() {
		Log.d("Reloading", "Called by onResume");
		// clearRows();
		// loadRows();
		super.onResume();
	}

	private void clearRows() {
		TableLayout tbl = (TableLayout) findViewById(R.id.feedTable);
		tbl.removeAllViews();
	}

	private void loadRows() {
		Log.i("Reloading", "Sending query for updated stories");
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Story");
		query.orderByDescending("updatedAt").include("author");
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> stories, ParseException e) {
				if (e == null) {
					TableLayout tbl = (TableLayout) findViewById(R.id.feedTable);
					int i = 0;
					for (ParseObject story : stories) {
						tbl.addView(createRow(story), i++);
					}
					Log.i("Reloading", "Repopulated table");
				} else {
					Toast.makeText(getApplicationContext(),
							"Unable to load from db", Toast.LENGTH_SHORT)
							.show();
					return;
				}
			}
		});
	}

	private TableRow createRow(ParseObject story) {
		// Get data from ParseObject story
		final String title = story.getString("title");
		final String description = story.getString("description");
		final String author = story.getParseUser("author").getUsername();
		final ParseGeoPoint location = story.getParseGeoPoint("location");
		final double compassValue = story.getDouble("compass");
		final int mediaType = story.getInt("mediaType");

		// Populate TableRow with media, title, description, and author
		TableRow tr = (TableRow) LayoutInflater.from(getApplicationContext())
				.inflate(R.layout.story_item, null);

		TextView tvTitle = (TextView) tr.findViewById(R.id.story_title);
		tvTitle.setText(title);

		TextView tvAuthor = (TextView) tr.findViewById(R.id.story_author);
		tvAuthor.setText("by " + author);

		TextView tvDesc = (TextView) tr.findViewById(R.id.story_description);
		tvDesc.setText(description);

		TextView tvLoc = (TextView) tr.findViewById(R.id.story_gps);
		if (location != null) {
			tvLoc.setText("GPS " + String.valueOf((int) location.getLatitude())
					+ ", " + String.valueOf((int) location.getLongitude())
					+ " Compass " + String.valueOf(compassValue));
		}

		if (mediaType == Story.NO_MEDIA) {
			tr.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					storyOnClickHandler(title, author, description, null,
							mediaType);
				}
			});
			return tr;
		}

		final byte[] byteArr;
		try {
			byteArr = story.getParseFile("media").getData();
		} catch (ParseException e) {
			return tr;
		}

		ImageView ivIcon = (ImageView) tr.findViewById(R.id.story_image);
		ImageView overlay = (ImageView) tr.findViewById(R.id.video_overlay);
		if (mediaType == Story.IMAGE_TYPE) {
			ivIcon.setImageBitmap(BitmapFactory.decodeByteArray(byteArr, 0,
					byteArr.length));
		} else if (mediaType == Story.VIDEO_TYPE) {
			// TODO: set ivIcon to be a part of video
			ivIcon.setImageResource(R.drawable.video_icon);
			ivIcon.setScaleType(ScaleType.FIT_XY);

			overlay.setVisibility(View.VISIBLE);
		} else if (mediaType == Story.AUDIO_TYPE) {
			ivIcon.setImageResource(R.drawable.play_audio);
			overlay.setVisibility(View.VISIBLE);
		}

		tr.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				storyOnClickHandler(title, author, description, byteArr,
						mediaType);
			}
		});
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
			clearRows();
			loadRows();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void postLinkEventHandler(View view) {
		Intent intent = new Intent(this, PostActivity.class);
		startActivityForResult(intent, POST_STORY_REQUEST);
	}

	public void reportLinkEventHandler(View view) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("plain/text");
		intent.putExtra(Intent.EXTRA_EMAIL,
				new String[] { "campuspd@andrew.cmu.edu" });
		intent.putExtra(Intent.EXTRA_SUBJECT, "Report from CampusChatter");
		intent.putExtra(Intent.EXTRA_TEXT, "Detail Description");
		try {
			startActivity(Intent.createChooser(intent, "Send mail..."));
		} catch (ActivityNotFoundException e) {
			Toast.makeText(FeedActivity.this, "Email Client Error!",
					Toast.LENGTH_SHORT).show();
		}
	}

	public void logout() {
		ParseUser.logOut();
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == POST_STORY_REQUEST) {
			String toastMsg = resultCode == RESULT_OK ? "Posted story to feed"
					: "Did not post story";
			Toast.makeText(getApplicationContext(), toastMsg, Toast.LENGTH_LONG)
					.show();
		} else if (requestCode == BLUETOOTH_REQUEST) {
			Uri uri = data.getData();
			String path = ActivitiesHelper.getRealPathFromURI(
					getApplicationContext(), uri);
			BluetoothAPI bluetooth = new BluetoothAPI();
			bluetooth.sendFile(path, getApplicationContext());
		}
	}

	private void storyOnClickHandler(String title, String author,
			String description, byte[] byteArr, int mediaType) {
		Intent intent = new Intent(getApplicationContext(),
				ViewStoryActivity.class);
		intent.putExtra("title", title);
		intent.putExtra("author", author);
		intent.putExtra("description", description);
		intent.putExtra("media", byteArr);
		intent.putExtra("mediaType", mediaType);
		startActivity(intent);
	}

}
