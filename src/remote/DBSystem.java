package remote;

public interface DBSystem {
	
	// a row represents one post
	public void addRow();
	public void deleteRow();
	public void fetchRow();
	
	public void addVideo();
	public void addText();
	public void addAudio();
	public void addPlace();
	public void addPhoto();
	public void addEmergentReport();
	
	public void addUser();
	public void setAdministrator();
	public void setPolice();
	
}
