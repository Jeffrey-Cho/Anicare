package sep.software.anicare;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

public class AniCareBroadCastReceiver extends WakefulBroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String registrationId = intent.getExtras().getString("registration_id");
		if(registrationId != null && !registrationId.equals("")) {
			// Get registration id
			
		} else {
			// Explicitly specify that GcmIntentService will handle the intent.
			ComponentName comp = new ComponentName(context.getPackageName(), AniCareIntentService.class.getName());

			// Start the service, keeping the device awake while it is launching.
			startWakefulService(context, intent.setComponent(comp));
			setResultCode(Activity.RESULT_OK);
		}
	}
}
