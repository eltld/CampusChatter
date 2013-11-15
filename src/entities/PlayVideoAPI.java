package entities;

import android.content.Context;
import android.net.Uri;
import android.widget.MediaController;
import android.widget.VideoView;

public class PlayVideoAPI {
	
	public void playVideo(Uri uri, VideoView videoView, Context context) {
	    MediaController mediaController= new MediaController(context);
	    mediaController.setAnchorView(videoView);
	    
	    videoView.setMediaController(mediaController);
	    videoView.setVideoURI(uri);
	    videoView.requestFocus();

	    videoView.start();
	}
}
