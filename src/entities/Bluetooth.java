package entities;

import android.content.Context;

public interface Bluetooth {
	public void discoverableDevices(Context context);
	public int setup();
	public void enableDiscoverable(Context context);
	public void sendFile(String filepath, Context context);
}
