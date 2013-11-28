package entities;

import android.content.Context;
import android.telephony.SmsManager;

public class SendEmergencyTextAPI implements SendEmergencyText {

	private String number="4122682323";
	@Override
	public int sendEmergencyText(Context context) {
		SmsManager smsManager = SmsManager.getDefault();
		GPS gps = new GPSAPI();
		gps.createGPS(context);
		String sms = "Help! I am at lat- "+gps.getLatitude()+"long- "+
										gps.getLongitude();
		smsManager.sendTextMessage(number, null, sms, null, null);
		return 0;
	}

}
