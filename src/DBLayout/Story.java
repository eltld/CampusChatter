package DBLayout;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Story")
public class Story extends ParseObject{
	public final static int IMAGE_TYPE = 0;
	public final static int AUDIO_TYPE = 1;
	public final static int VIDEO_TYPE = 2;
	public final static int NO_MEDIA = 3;
	
	public Story() {
	}
	
	public String getTitle() {
		return getString("title");
	}
	
	public void setTitle(String title) {
		put("title", title);
	}
	
	public String getDescription() {
		return getString("description");
	}
	
	public void setDescription(String description) {
		put("description", description);
	}
	
	public ParseUser getAuthor() {
		return getParseUser("author");
	}
	
	public void setAuthor(ParseUser user) {
		put("author", user);
	}
	
	public ParseFile getMediaFile() {
		return getParseFile("media");
	}
	
	public void setMediaFile(ParseFile file) {
		put("media", file);
	}
	
	public int getMediaType() {
		return getInt("mediaType");
	}
	
	public void setMediaType(int mediaType) {
		put("mediaType", mediaType);
	}
	
	public int getUpvotes() {
		return getInt("upvotes");
	}
	
	public void setUpvotes(int upvotes) {
		put("upvotes", upvotes);
	}
	
	public int getDownvotes() {
		return getInt("downvotes");
	}
	
	public void setDownvotes(int downvotes) {
		put("downvotes", downvotes);
	}
}
