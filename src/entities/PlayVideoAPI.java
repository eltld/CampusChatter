package entities;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
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
		try {
			String loc = "video"+System.currentTimeMillis()+".mp4";
			FileOutputStream fos = context.openFileOutput(loc, Context.MODE_WORLD_READABLE);
			fos.write(video);
			fos.close();
			
			MediaController mediaController= new MediaController(context);
			mediaController.setAnchorView(videoView);

			videoView.setMediaController(mediaController);
			videoView.setVideoPath(context.getFileStreamPath(loc).getAbsolutePath());
			videoView.requestFocus();

			videoView.start();
		} catch (FileNotFoundException e) {
			Log.e("play video", "file not found");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			Log.e("play video", "ioexception");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
