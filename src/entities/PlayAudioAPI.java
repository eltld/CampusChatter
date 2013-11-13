package entities;

import java.io.IOException;

import android.media.MediaPlayer;

public class PlayAudioAPI implements PlayAudio{
	private MediaPlayer m;
	
	public void createPlayer() {
		m = new MediaPlayer();
	}
	public void playAudio(String uri){
		try {
			if (m.isPlaying()) {
				m.stop();
				m.release();
				m = new MediaPlayer();
			}
			m.setDataSource(uri);
			m.prepare();
			m.setVolume(1f, 1f);
			m.start();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void stopAudio() {
		if(m!=null)
			m.stop();
	}
	
	public void pauseAudio() {
		if(m!=null)
			m.pause();
	}
}
