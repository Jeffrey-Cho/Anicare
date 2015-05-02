package sep.software.anicare;

//import com.kakao.Session;

import sep.software.anicare.service.AniCareService;
import sep.software.anicare.service.AniCareServiceAzure;
import sep.software.anicare.util.ObjectPreferenceUtil;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class AniCareApp extends Application {
    private static volatile AniCareApp instance = null;

    private AniCareService mAniCareService;
    private ObjectPreferenceUtil mObjectPreference;
    private ProgressDialog progressDialog;

    public static synchronized AniCareApp getAppContext() {
        return instance;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

        instance = this;

        // initialize KakaoTalk
//        Session.initialize(this);
        // initialize AniCareService
        mAniCareService = new AniCareServiceAzure(instance);
        // initialize ObjectPreferenceUtil
        mObjectPreference = new ObjectPreferenceUtil(instance);

    }

    public AniCareService getAniCareService() {
        return mAniCareService;
    }

    public ObjectPreferenceUtil getObjectPreference() {
        return mObjectPreference;
    }

    public void showProgressDialog(Context context){
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.show();
        progressDialog.setContentView(R.layout.custom_progress_dialog);
    }

    public void dismissProgressDialog(){
        progressDialog.dismiss();
    }

    public boolean isInternetAvailable() {
        Context context = AniCareApp.getAppContext();
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return (activeNetwork != null && activeNetwork.isConnectedOrConnecting());
    }
}
