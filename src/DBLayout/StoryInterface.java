package DBLayout;

import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

public interface StoryInterface{
	
	public String getTitle();
	public void setTitle(String title);

	public String getDescription();
	public void setDescription(String description);

	public ParseUser getAuthor();
	public void setAuthor(ParseUser user);

	public ParseFile getMediaFile();
	public void setMediaFile(ParseFile file);

	public int getMediaType();
	public void setMediaType(int mediaType);

	public int getUpvotes();
	public void setUpvotes(int upvotes);

	public int getDownvotes();
	public void setDownvotes(int downvotes);

	public ParseGeoPoint getLocation();
	public void setLocation(ParseGeoPoint value);
	
	public double getCompass();
	public void setCompass(double compassValue);

	public void post(byte[] mediaData, String title, String desc, ParseGeoPoint myPoint, double compass);
}
