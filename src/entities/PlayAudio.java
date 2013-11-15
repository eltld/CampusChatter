package entities;

public interface PlayAudio {
	public void createPlayer();
	public void playAudio(String uri);
	public void stopAudio();
	public void pauseAudio();
}
