package local;

public interface User {
	public User SignIn();
	public User SignUp();
	public void Logout();
	
	public void refresh();
	
	public void EmergentCall();
	public void EmergentReport();
	
	
	public void post();
	
	// check in, return the location address
	public String checkIn();
	
	// choose a photo from gallery to upload, return the title of the photo
	public String photoChoose();
	
	// choose a video from gallery to upload, return the title of the video
	public String videoChoose();
	
	// choose a voice record from phone to upload, return the title of the record
	public String voiceChoose();
}
