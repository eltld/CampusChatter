package entities;

public interface Bluetooth {
	public int setupConnection(String address);
	public int sendFile(String filepath);
}
