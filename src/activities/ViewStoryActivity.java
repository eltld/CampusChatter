package activities;

import DBLayout.Story;
import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.campuschatter.R;

import entities.PlayAudioAPI;
import entities.PlayVideo;
import entities.PlayVideoAPI;

public class ViewStoryActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_story);
		
		String title = (String) getIntent().getExtras().get("title");
		String author = (String) getIntent().getExtras().get("author");
		String description = (String) getIntent().getExtras().get("description");
		final byte[] media = (byte[]) getIntent().getExtras().get("media");
		int mediaType = (Integer) getIntent().getExtras().get("mediaType");

		((TextView)findViewById(R.id.story_title_view)).setText(title);
		((TextView)findViewById(R.id.story_author_view)).setText("by " + author);		
		((TextView)findViewById(R.id.story_description_view)).setText(description);	
		FrameLayout fl = (FrameLayout)findViewById(R.id.story_media_wrapper);
		
		if (mediaType == Story.NO_MEDIA) {
			return;
		} else if (mediaType == Story.IMAGE_TYPE) {
			ImageView iv = (ImageView)LayoutInflater.from(
					getApplicationContext()).inflate(R.layout.image_story, null);
			iv.setImageBitmap(BitmapFactory.decodeByteArray(media, 0, media.length));
			fl.addView(iv);
		} else if (mediaType == Story.VIDEO_TYPE) {
			VideoView vv = (VideoView)LayoutInflater.from(
					getApplicationContext()).inflate(R.layout.video_story, null);
			
			PlayVideo pv = new PlayVideoAPI();
			pv.playVideo(media, vv, this);
			fl.addView(vv);
		} else if (mediaType == Story.AUDIO_TYPE) {
			FrameLayout afl = (FrameLayout)LayoutInflater.from(
					getApplicationContext()).inflate(R.layout.audio_story, null);
			final ImageView ivPlay = (ImageView)afl.findViewById(R.id.play_audio);
			final PlayAudioAPI api = new PlayAudioAPI();
			ivPlay.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					api.playAudio(getApplicationContext(), media);	
				}
			});
			fl.addView(afl);
			
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_story, menu);
		return true;
	}

}
