package sep.software.anicare.activity;

import sep.software.anicare.AniCareApp;
import sep.software.anicare.event.AniCareException;
import sep.software.anicare.callback.DialogCallback;
import sep.software.anicare.service.AniCareServiceAzure;
import sep.software.anicare.service.BlobStorageServiceAzure;
import sep.software.anicare.util.AniCareLogger;
import sep.software.anicare.util.ObjectPreferenceUtil;
import sep.software.anicare.view.AniCareAlertDialog;
import android.app.Activity;
import android.os.Bundle;

import de.greenrobot.event.EventBus;

public abstract class AniCareActivity extends Activity {

    protected Activity mThisActivity;
    protected AniCareApp mAppContext;
    protected AniCareServiceAzure mAniCareService;
    protected BlobStorageServiceAzure mBlobStorageService;
    protected ObjectPreferenceUtil mObjectPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        mThisActivity = this;

        mAppContext = AniCareApp.getAppContext();
        mAniCareService = mAppContext.getAniCareService();
        mBlobStorageService = mAppContext.getBlobStorageService();
        mObjectPreference = mAppContext.getObjectPreference();

        bindViews();
        initialize();
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

    protected abstract void bindViews();
    protected abstract void initialize();
}
