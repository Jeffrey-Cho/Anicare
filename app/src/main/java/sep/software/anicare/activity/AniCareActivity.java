package sep.software.anicare.activity;

import sep.software.anicare.AniCareApp;
import sep.software.anicare.AniCareException;
import sep.software.anicare.interfaces.DialogCallback;
import sep.software.anicare.model.AniCarePet;
import sep.software.anicare.model.AniCareUser;
import sep.software.anicare.service.AniCareService;
import sep.software.anicare.util.AniCareLogger;
import sep.software.anicare.util.ObjectPreferenceUtil;
import sep.software.anicare.view.AniCareAlertDialog;
import android.app.Activity;
import android.os.Bundle;

import de.greenrobot.event.EventBus;

public class AniCareActivity extends Activity {

    protected Activity mThisActivity;
    protected AniCareApp mAppContext;
    protected AniCareService mAniCareService;
    protected ObjectPreferenceUtil mObjectPreference;
    protected AniCareUser mThisUser;
    protected AniCarePet mThisPet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        mThisActivity = this;

        mAppContext = AniCareApp.getAppContext();
        mAniCareService = mAppContext.getAniCareService();
        mObjectPreference = mAppContext.getObjectPreference();
        mThisUser = mAniCareService.getCurrentUser();
        mThisPet = mAniCareService.getCurrentPet();
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

    protected boolean isInternetAvailable() {
        return mAppContext.isInternetAvailable();
    }

}
