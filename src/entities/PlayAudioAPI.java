package entities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
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
	
	public void playAudio(Context context, byte[] audio) {
		if(m!=null)
			m.stop();
		m = new MediaPlayer();
		try {
	        // create temp file that will hold byte array
	        File tempMp3 = File.createTempFile("audioFile", ".mp3", context.getCacheDir());
	        tempMp3.deleteOnExit();
	        FileOutputStream fos = new FileOutputStream(tempMp3);
	        fos.write(audio);
	        fos.close();

	        // Tried passing path directly, but kept getting 
	        // "Prepare failed.: status=0x1"
	        // so using file descriptor instead
	        FileInputStream fis = new FileInputStream(tempMp3);
	        if (m.isPlaying()) {
				m.stop();
				m.release();
				m = new MediaPlayer();
			}
	        m.setDataSource(fis.getFD());

	        m.prepare();
	        m.setVolume(1f, 1f);
	        m.start();
	        fis.close();
	    } catch (IOException ex) {
	        ex.printStackTrace();
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
