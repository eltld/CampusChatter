package entities;

import android.content.Context;

public interface GPS {
	public double getLatitude();
	public double getLongitude();
	public void createGPS(Context context);
}
