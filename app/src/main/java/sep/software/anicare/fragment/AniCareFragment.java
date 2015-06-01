package sep.software.anicare.fragment;

import sep.software.anicare.AniCareApp;
import sep.software.anicare.R;
import sep.software.anicare.model.AniCarePet;
import sep.software.anicare.model.AniCareUser;
import sep.software.anicare.service.AniCareService;
import sep.software.anicare.util.ObjectPreferenceUtil;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

public class AniCareFragment extends Fragment {
	protected Activity mThisActivity;
	protected Fragment mThisFragment;
	protected AniCareApp mAppContext;
	protected AniCareService mAniCareService;
	protected ObjectPreferenceUtil mObjectPreference;
    protected AniCareUser mThisUser;
    protected AniCarePet mThisPet;
	
	public AniCareFragment() {
		// TODO Auto-generated constructor stub
		super();
		mThisFragment = this;
		
		mAppContext = AniCareApp.getAppContext();
		mAniCareService = mAppContext.getAniCareService();
		mObjectPreference = mAppContext.getObjectPreference();
        mThisUser = mAniCareService.getCurrentUser();
        mThisPet = mAniCareService.getCurrentPet();

	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mThisActivity = this.getActivity();
    }

}
