package entities;

import java.io.File;
import java.util.ArrayList;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;

public class BluetoothAPI implements Bluetooth{
	
	private BluetoothAdapter mBluetoothAdapter;
	private ArrayList<String> devices = new ArrayList<String>();
	// Create a BroadcastReceiver for ACTION_FOUND
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			// When discovery finds a device
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				// Get the BluetoothDevice object from the Intent
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				devices.add(device.getName() + "\n" + device.getAddress());
			}
		}
	};

	public int setup() {
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			// Device does not support Bluetooth
			return -1;
		}
		if (!mBluetoothAdapter.isEnabled()) {
			return -2;
		}
		return 1;
	}

	public void discoverableDevices(Context context) {
		// Register the BroadcastReceiver
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		context.registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
	}

	public void enableDiscoverable(Context context) {
		Intent discoverableIntent = new
				Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
		context.startActivity(discoverableIntent);
	}
	
	public Intent sendFile(String filename,Context context) {
		Intent sharingIntent = new Intent(Intent.ACTION_SEND);
		File f = new File(filename);

		sharingIntent.setType("image/jpeg");
		sharingIntent.setPackage("com.android.bluetooth");
		sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));
		return sharingIntent;
	}
}