package entities;

import android.content.Context;

public interface PlayAudio {
	public void createPlayer();
	public void playAudio(String uri);
	public void playAudio(Context context, byte[] audio);
	public void stopAudio();
	public void pauseAudio();
}
