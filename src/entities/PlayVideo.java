package entities;

import android.content.Context;
import android.net.Uri;
import android.widget.VideoView;

public interface PlayVideo {
	public void playVideo(Uri uri, VideoView videoView, Context context);
}
