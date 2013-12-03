package entities;

import android.content.Context;
import android.content.Intent;

public interface Bluetooth {
	public void discoverableDevices(Context context);
	public int setup();
	public void enableDiscoverable(Context context);
	public Intent sendFile(String filepath, Context context);
}
