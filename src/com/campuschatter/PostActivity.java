package com.campuschatter;

import java.io.ByteArrayOutputStream;

import DBLayout.Story;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.campuschatter.R;
import com.parse.ParseFile;
import com.parse.ParseUser;

public class PostActivity extends Activity {
	private Story story;
	private byte[] mediaData;
	
	private final int IMAGE_QUALITY = 50;
	
	private static final int CAMERA_PIC_REQUEST = 1337;
	private static final int TAKE_VIDEO_REQUEST = 3;
	private static final int RECORD_AUDIO_REQUEST = 3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		story = new Story();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.campuschatter_post);
		
		ImageView cameraIcon = (ImageView)findViewById(R.id.camera_icon);
		ImageView videoIcon = (ImageView)findViewById(R.id.video_icon);
		ImageView audioIcon = (ImageView)findViewById(R.id.microphone_icon);
		
		cameraIcon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				capturePhoto();
			}
		});
		
		videoIcon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				captureVideo();
			}
		});
		
		audioIcon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				captureAudio();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.post, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_post:
	        	postStory();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	
	private void postStory() {
		storeBytes();
		Intent intent = new Intent();
		setResult(RESULT_OK, intent);
		finish();
	}

	private void storeBytes() {
		// TODO: Check for inputed text fields (is valid)
		EditText vTitle = (EditText)findViewById(R.id.story_title);
		EditText vDesc = (EditText)findViewById(R.id.story_description);
		story.setAuthor(ParseUser.getCurrentUser());
		story.setTitle(vTitle.getText().toString());
		story.setDescription(vDesc.getText().toString());
		story.setUpvotes(0);
		story.setDownvotes(0);
		
		if (mediaData == null) {
			//just store text inputs
			return;
		}
		
		String filename;
		switch(story.getMediaType()) {
		case Story.IMAGE_TYPE:
			filename = "media.jpg";
			break;
		case Story.VIDEO_TYPE:
			filename = "media.mp4";
			break;
		case Story.AUDIO_TYPE:
			filename = "media.mp3";
			break;
		default:
			filename = "";
		}
		ParseFile file = new ParseFile(filename, mediaData);
		file.saveInBackground();
		story.setMediaFile(file);
		story.saveInBackground();
	}
	
	private void capturePhoto() {
		Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
	}
	
	private void captureVideo() {
		Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		startActivityForResult(videoIntent, TAKE_VIDEO_REQUEST);
	}
	
	private void captureAudio() {
		// TODO!
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			Toast.makeText(getApplicationContext(), "Unable to store media", Toast.LENGTH_SHORT).show();
			return;
		}
		if (requestCode == CAMERA_PIC_REQUEST) {
			Bitmap bmp = (Bitmap) data.getExtras().get("data");
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bmp.compress(Bitmap.CompressFormat.JPEG, IMAGE_QUALITY, stream);
			mediaData = stream.toByteArray();
			story.setMediaType(Story.IMAGE_TYPE);
			((ImageView)findViewById(R.id.tmpImagePreview)).setImageBitmap(bmp);
		} else if (requestCode == TAKE_VIDEO_REQUEST) {
			// TODO: Convert data to byte array
			
			
			story.setMediaType(Story.VIDEO_TYPE);
		} else if (requestCode == RECORD_AUDIO_REQUEST) {
			// TODO: Convert data to byte array
			
			

			story.setMediaType(Story.AUDIO_TYPE);
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
}
