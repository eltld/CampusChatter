package entities;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.widget.MediaController;
import android.widget.VideoView;

public class PlayVideoAPI implements PlayVideo{

	public void playVideo(Uri uri, VideoView videoView, Context context) {
		MediaController mediaController= new MediaController(context);
		mediaController.setAnchorView(videoView);

		videoView.setMediaController(mediaController);
		videoView.setVideoURI(uri);
		videoView.requestFocus();

		videoView.start();
	}

	public void playVideo(byte[] video, VideoView videoView, Context context) {
		FileOutputStream out = null;
		try {
			String loc = Environment.getExternalStorageDirectory().toString() + "/video/";
			loc += System.currentTimeMillis() + ".mp4";
			out = new FileOutputStream(loc);
			out.write(video);
			out.close();
			MediaController mediaController= new MediaController(context);
			mediaController.setAnchorView(videoView);

			videoView.setMediaController(mediaController);
			videoView.setVideoPath(loc);
			videoView.requestFocus();

			videoView.start();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
