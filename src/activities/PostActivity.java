package activities;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import DBLayout.Story;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
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
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class PostActivity extends Activity {
	private Story story;
	private byte[] mediaData;

	private final int IMAGE_QUALITY = 50;

	private static final int CAMERA_PIC_REQUEST = 1337;
	private static final int TAKE_VIDEO_REQUEST = 3;
	private static final int RECORD_AUDIO_REQUEST = 4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		story = new Story();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.campuschatter_post);

		ImageView cameraIcon = (ImageView) findViewById(R.id.camera_icon);
		ImageView videoIcon = (ImageView) findViewById(R.id.video_icon);
		ImageView audioIcon = (ImageView) findViewById(R.id.microphone_icon);

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
		if (mediaData == null) {
			story.setMediaType(Story.NO_MEDIA);
		}
		// Change UI to show uploading story
		showProgress(true);

		// Transfer inputs into story object
		EditText vTitle = (EditText) findViewById(R.id.story_title);
		EditText vDesc = (EditText) findViewById(R.id.story_description);
		story.setAuthor(ParseUser.getCurrentUser());
		story.setTitle(vTitle.getText().toString());
		story.setDescription(vDesc.getText().toString());
		story.setUpvotes(0);
		story.setDownvotes(0);

		// TODO: Check for inputed text fields (is valid)

		// If no media, just save it
		if (mediaData == null) {
			// just store text inputs
			saveStoryAndReturnToFeed();
			return;
		}

		// Deal with media files
		String filename;
		switch (story.getMediaType()) {
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
		saveStoryAndReturnToFeed();
	}

	private void saveStoryAndReturnToFeed() {
		story.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				showProgress(false);
				Intent intent = new Intent();
				if (getParent() == null) {
					setResult(RESULT_OK, intent);
				} else {
					getParent().setResult(RESULT_OK, intent);
				}
				finish();
			}
		});
	}

	private void capturePhoto() {
		Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
	}

	private void captureVideo() {
		Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		videoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 5);
		videoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
		startActivityForResult(videoIntent, TAKE_VIDEO_REQUEST);
	}

	private void captureAudio() {
		Intent audioIntent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
		audioIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 7);
		startActivityForResult(audioIntent, RECORD_AUDIO_REQUEST);
	}

	/**
	 * Convert data returned from Intent into byte array as mediaData
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			showToast("Did NOT store media");
			return;
		}
		if (requestCode == CAMERA_PIC_REQUEST) {
			Bitmap bmp = (Bitmap) data.getExtras().get("data");
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bmp.compress(Bitmap.CompressFormat.JPEG, IMAGE_QUALITY, stream);
			mediaData = stream.toByteArray();
			story.setMediaType(Story.IMAGE_TYPE);
			((ImageView) findViewById(R.id.tmpImagePreview))
					.setImageBitmap(bmp);
		} else if (requestCode == TAKE_VIDEO_REQUEST) {
			Uri videoUri = data.getData();
			convertUriToByteArray(videoUri);
			story.setMediaType(Story.VIDEO_TYPE);
		} else if (requestCode == RECORD_AUDIO_REQUEST) {
			Uri audioUri = data.getData();
			convertUriToByteArray(audioUri);
			story.setMediaType(Story.AUDIO_TYPE);
		}

		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private void showToast(String str) {
		Toast.makeText(getApplicationContext(), str,
				Toast.LENGTH_SHORT).show();
	}
	
	/* Helper methods to save audio/video files as byte[] (mediaData) */
	private String getRealPathFromURI(Uri contentUri) {
	  Cursor cursor = null;
	  try { 
	    String[] proj = { MediaStore.Images.Media.DATA };
	    cursor = getApplicationContext().getContentResolver().query(contentUri,  proj, null, null, null);
	    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	    cursor.moveToFirst();
	    return cursor.getString(column_index);
	  } finally {
	    if (cursor != null) {
	      cursor.close();
	    }
	  }
	}
	
	private void convertUriToByteArray(Uri uri) {
		if (uri == null) {
			showToast("videoUri is null");
			return;
		}
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		FileInputStream is = null;
		
		try {
			is = new FileInputStream(getRealPathFromURI(uri));
			byte[] buf = new byte[1024];
			int n;
			while (-1 != (n = is.read(buf))) {
				stream.write(buf, 0, n);
			}
			mediaData = stream.toByteArray();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			showToast("FileNotFound uploading video");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			showToast("IO Exception uploading video");
		}
	}
	
	

	/**
	 * Shows the progress UI and hides the post form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		final View mPostStatusView = findViewById(R.id.post_status);
		final View mPostFormView = findViewById(R.id.post_form);

		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mPostStatusView.setVisibility(View.VISIBLE);
			mPostStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mPostStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mPostFormView.setVisibility(View.VISIBLE);
			mPostFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mPostFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mPostStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mPostFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}
}
