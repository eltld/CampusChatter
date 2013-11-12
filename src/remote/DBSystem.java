package remote;
import local.User;

public interface DBSystem {
	
	// a row represents one post
	public void addRow();
	public void deleteRow();
	public void fetchRow();
	
	public void addVideo(String videoName);
	public void addText(String status);
	public void addAudio(String audioName);
	public void addPlace(String placeAddress);
	public void addPhoto(String photoTitle);
	public void addEmergentReport(String EmergentTitle);
	
	public void addUser(User user);
	public void setAdministrator(Administrator admin);
	public void setPolice();
	
}
