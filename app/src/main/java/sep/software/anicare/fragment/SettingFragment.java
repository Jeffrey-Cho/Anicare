package sep.software.anicare.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import sep.software.anicare.AniCareApp;
import sep.software.anicare.R;
import sep.software.anicare.activity.PetSettingActivity;
import sep.software.anicare.activity.SplashActivity;
import sep.software.anicare.activity.UserSettingActivity;
import sep.software.anicare.model.AniCarePet;
import sep.software.anicare.model.AniCareUser;
import sep.software.anicare.service.AniCareService;
import sep.software.anicare.util.ObjectPreferenceUtil;

public class SettingFragment extends PreferenceFragment {

    private static final String TAG = SettingFragment.class.getCanonicalName();
    private static final String SETTING_FLAG = "FROM_SETTING";

    protected Activity mThisActivity;
    protected Fragment mThisFragment;
    protected AniCareApp mAppContext;
    protected AniCareService mAniCareService;
    protected ObjectPreferenceUtil mObjectPreference;
    protected AniCareUser mThisUser;
    protected AniCarePet mThisPet;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    public SettingFragment() {
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
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preference);

        Preference userPref = findPreference("user_setting_category_key");
        userPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                Fragment usereditfragment = new UserEditFragment();
                String tag = usereditfragment.getClass().getSimpleName();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, usereditfragment, tag).commit();

                return true;
            }
        });

        Preference petPref = findPreference("pet_setting_category_key");
        petPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                Fragment peteditfragment = new PetEditFragment();
                String tag = peteditfragment.getClass().getSimpleName();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, peteditfragment, tag).commit();
                return true;
            }
        });

        Preference logoutPref = findPreference("logout_key");
        logoutPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                mAniCareService.logout();
                Intent intent = new Intent();
                intent.setClass(mThisActivity, SplashActivity.class);
                startActivity(intent);
                mThisActivity.finish();
                return true;
            }
        });

        Preference exitPref = findPreference("exit_key");
        exitPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                //open browser or intent here
                mAniCareService.dropout();
                Intent intent = new Intent();
                intent.setClass(mThisActivity, SplashActivity.class);
                startActivity(intent);
                mThisActivity.finish();
                return true;
            }
        });
    }

}
