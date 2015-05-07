package sep.software.anicare.service;

import sep.software.anicare.AniCareApp;
import sep.software.anicare.AniCareException;
import de.greenrobot.event.EventBus;
import android.os.AsyncTask;

public abstract class AniCareAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();

        if (!AniCareApp.getAppContext().isInternetAvailable()) {
            EventBus.getDefault().post(new AniCareException(AniCareException.TYPE.NETWORK_UNAVAILABLE));
            cancel(true);
        }
    }

}
