package sep.software.anicare.activity;

import sep.software.anicare.event.AniCareException;
import sep.software.anicare.interfaces.DialogCallback;
import sep.software.anicare.util.AniCareLogger;
import sep.software.anicare.view.AniCareAlertDialog;
import de.greenrobot.event.EventBus;
import android.app.Activity;
import android.os.Bundle;

public class AniCareActivity extends Activity {
	
	protected Activity thisActivity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
		thisActivity = this;
		
	}
	
	public void onEvent(AniCareException exception){
		AniCareLogger.log(exception);
		
		AniCareAlertDialog
			.newInstance(exception.getType().toString(), "OK", "Cancel", true)
			.setCallback(new DialogCallback() {
				
				@Override
				public void doPositive(Bundle bundle) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void doNegative(Bundle bundle) {
					// TODO Auto-generated method stub
					
				}
			}).show();
	}
}
