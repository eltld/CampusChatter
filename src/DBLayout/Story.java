package DBLayout;

public class Story {
	private int id;
	private int mid;
	private int numUpVotes;
	private int numDownVotes;
	private String timestamp;
	private String location;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getMid() {
		return mid;
	}
	public void setMid(int mid) {
		this.mid = mid;
	}
	public int getNumUpVotes() {
		return numUpVotes;
	}
	public void setNumUpVotes(int numUpVotes) {
		this.numUpVotes = numUpVotes;
	}
	public int getNumDownVotes() {
		return numDownVotes;
	}
	public void setNumDownVotes(int numDownVotes) {
		this.numDownVotes = numDownVotes;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
}
