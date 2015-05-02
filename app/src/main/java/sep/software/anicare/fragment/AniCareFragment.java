package sep.software.anicare.fragment;

import sep.software.anicare.AniCareApp;
import sep.software.anicare.service.AniCareService;
import sep.software.anicare.util.ObjectPreferenceUtil;
import android.app.Activity;
import android.app.Fragment;

public class AniCareFragment extends Fragment {
	protected Activity mThisActivity;
	protected Fragment mThisFragment;
	protected AniCareApp mAppContext;
	protected AniCareService mAniCareService;
	protected ObjectPreferenceUtil mObjectPreference;
	
	public AniCareFragment() {
		// TODO Auto-generated constructor stub
		super();
		mThisActivity = this.getActivity();
		mThisFragment = this;
		
		mAppContext = AniCareApp.getAppContext();
		mAniCareService = mAppContext.getAniCareService();
		mObjectPreference = mAppContext.getObjectPreference();

	}

}
