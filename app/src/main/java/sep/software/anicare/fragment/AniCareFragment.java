package sep.software.anicare.fragment;

import sep.software.anicare.AniCareApp;
import sep.software.anicare.service.AniCareServiceAzure;
import sep.software.anicare.service.BlobStorageServiceAzure;
import sep.software.anicare.util.ObjectPreferenceUtil;
import android.app.Activity;
import android.app.Fragment;

public abstract class AniCareFragment extends Fragment {
	protected Activity mThisActivity;
	protected Fragment mThisFragment;
	protected AniCareApp mAppContext;
	protected AniCareServiceAzure mAniCareService;
	protected BlobStorageServiceAzure mBlobStorageService;
	protected ObjectPreferenceUtil mObjectPreference;
	
	public AniCareFragment() {
		// TODO Auto-generated constructor stub
		super();
		mThisActivity = this.getActivity();
		mThisFragment = this;
		
		mAppContext = AniCareApp.getAppContext();
		mAniCareService = mAppContext.getAniCareService();
		mBlobStorageService = mAppContext.getBlobStorageService();
		mObjectPreference = mAppContext.getObjectPreference();

        bindViews();
        initialize();
	}

    protected abstract void bindViews();
    protected abstract void initialize();
}
