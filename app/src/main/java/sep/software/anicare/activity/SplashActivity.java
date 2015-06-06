package sep.software.anicare.activity;

import java.io.IOException;
import java.util.Arrays;

import sep.software.anicare.AniCareApp;
import sep.software.anicare.R;
import sep.software.anicare.AniCareException;
import sep.software.anicare.interfaces.EntityCallback;
import sep.software.anicare.model.AniCareUser;
import sep.software.anicare.service.AniCareAsyncTask;
import sep.software.anicare.util.AniCareLogger;
import sep.software.anicare.util.AsyncChainer;
import sep.software.anicare.util.AsyncChainer.Chainable;
import sep.software.anicare.util.ImageUtil;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;

import com.facebook.AppEventsLogger;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

import com.google.gson.Gson;
import com.kakao.APIErrorResult;
import com.kakao.MeResponseCallback;
import com.kakao.Session;
import com.kakao.SessionCallback;
import com.kakao.UserManagement;
import com.kakao.UserProfile;
import com.kakao.exception.KakaoException;

import com.squareup.picasso.Picasso;

import de.greenrobot.event.EventBus;

public class SplashActivity extends AniCareActivity {

    private final static String TAG = SplashActivity.class.getCanonicalName();

    private LoginButton mFacebookButton;

    private UiLifecycleHelper mFacebookUiHelper;

    private com.kakao.widget.LoginButton mKakaoButton;
    private com.kakao.Session session;
    private final com.kakao.SessionCallback mySessionCallback = new MySessionStatusCallback();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getActionBar().hide();

        NotificationManager notificationManger = (NotificationManager) mThisActivity.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManger.cancel(1);

        initializeFacebook(savedInstanceState);

        initializeKakao();
    }

    private void initializeFacebook(Bundle savedInstanceState) {

        mFacebookButton = (LoginButton) findViewById(R.id.login_facebook);

        mFacebookUiHelper = new UiLifecycleHelper(mThisActivity, new StatusCallback() {

            @Override
            public void call(com.facebook.Session session, SessionState state, Exception exception) {
            }
        });

        mFacebookUiHelper.onCreate(savedInstanceState);

        if (mAniCareService.isLoggedIn()) {
            mFacebookButton.setVisibility(View.GONE);
        }

        mFacebookButton.setReadPermissions(Arrays.asList("public_profile"));
        mFacebookButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        mFacebookButton.setTypeface(mFacebookButton.getTypeface(), Typeface.BOLD);
        mFacebookButton.setText(getResources().getString(R.string.facebook_login));

//		mFacebookButton.setBackgroundResource(R.drawable.signin_button);
//		mFacebookButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.font_medium));
//		mFacebookButton.setTextColor(getResources().getColor(R.color.brand_color));

        mFacebookButton.setUserInfoChangedCallback(new com.facebook.widget.LoginButton.UserInfoChangedCallback() {

            @Override
            public void onUserInfoFetched(GraphUser user) {
                com.facebook.Session session = com.facebook.Session.getActiveSession();
                if (session != null && session.isOpened() || user != null) {
                    AniCareUser aniCareUser = new AniCareUser();
                    aniCareUser.setPlatformId(user.getId());
                    aniCareUser.setName(user.getFirstName());
                    aniCareUser.setImageUrl(String.format("https://graph.facebook.com/%s/picture?type=large", aniCareUser.getPlatformId()));

                    login(aniCareUser);
                }
            }
        });

    }

    private void initializeKakao() {

        com.kakao.Session.initialize(mAppContext);
        mKakaoButton = (com.kakao.widget.LoginButton) findViewById(R.id.com_kakao_login);

        if (mAniCareService.isLoggedIn()) {
            mKakaoButton.setVisibility(View.GONE);
        }


        session = com.kakao.Session.getCurrentSession();
        session.addCallback(mySessionCallback);


    }

    private void login(final AniCareUser aniCareUser){
        if (mAniCareService.isLoggedIn()) {
            // wait for 1 sec.
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            goToNextActivity();
            return;
        }

        final String profileImageUrl = aniCareUser.getImageUrl();

        AniCareApp.getAppContext().showProgressDialog(mThisActivity);

        AsyncChainer.asyncChain(mThisActivity, new Chainable(){

            @Override
            public void doNext(final Object obj, Object... params) {
                // TODO Auto-generated method stub
                mAniCareService.getGcmRegistrationId(new EntityCallback<String>() {

                    @Override
                    public void onCompleted(String registrationId) {
                        // TODO Auto-generated method stub
                        aniCareUser.setRegistrationId(registrationId);
                        AsyncChainer.notifyNext(obj);
                    }
                });
            }

        }, new Chainable(){
            @Override
            public void doNext(final Object object, Object... params) {
                mAniCareService.login(aniCareUser, new EntityCallback<AniCareUser>() {

                    @Override
                    public void onCompleted(AniCareUser entity) {
                        // TODO Auto-generated method stub
                        AniCareLogger.log(entity);
                        AsyncChainer.notifyNext(object);
                    }
                });
            }
        }, new Chainable(){
            @Override
            public void doNext(final Object obj, Object... params) {
                (new AniCareAsyncTask<Void, Void, Bitmap>(){

                    @Override
                    protected Bitmap doInBackground(Void... params) {
                        // TODO Auto-generated method stub
                        try {
                            return Picasso.with(mThisActivity).load(profileImageUrl).get();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            EventBus.getDefault().post(new AniCareException(AniCareException.TYPE.NETWORK_UNAVAILABLE));
                            return null;
                        }
                    }

                    protected void onPostExecute(Bitmap result) {
                        AsyncChainer.notifyNext(obj, result);
                    }

                }).execute();

            }
        }, new Chainable(){

            @Override
            public void doNext(final Object obj, Object... params) {
                Bitmap profileImage = (Bitmap) params[0];
//                Bitmap profileImageBitmap = ImageUtil.refineSquareImage(profileImage, ImageUtil.PROFILE_IMAGE_SIZE);
                String userId = mAniCareService.getCurrentUser().getId();
                mAniCareService.uploadUserImage(userId, profileImage, new EntityCallback<String>() {
                    @Override
                    public void onCompleted(String entity) {
                        AniCareApp.getAppContext().dismissProgressDialog();
                        AsyncChainer.notifyNext(obj);
                    }
                });

            }
        }, new Chainable(){

            @Override
            public void doNext(final Object obj, Object... params) {
                // go to next Activity
                goToNextActivity();
            }
        });
    }

    private void goToNextActivity() {
        // TODO Auto-generated method stub

        Intent intent = new Intent();
        intent.setClass(mThisActivity, MainActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onResume() {
        super.onResume();

        // Facebook
        mFacebookUiHelper.onResume();
        AppEventsLogger.activateApp(mThisActivity);

        // KakaoTalk
        if (session.isClosed()){
//            mKakaoButton.setVisibility(View.VISIBLE);
        }
        // 세션을 가지고 있거나, 갱신할 수 있는 상태로 명시적 오픈을 위한 로그인 버튼을 보여주지 않는다.
        else {
            mKakaoButton.setVisibility(View.GONE);

            // 갱신이 가능한 상태라면 갱신을 시켜준다.
            if (session.isOpenable()) {
                session.implicitOpen();
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // facebook
        mFacebookUiHelper.onActivityResult(requestCode, resultCode, data);

        // kakao
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onPause() {
        super.onPause();
        mFacebookUiHelper.onPause();
        AppEventsLogger.deactivateApp(mThisActivity);
    }


    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mFacebookUiHelper.onDestroy();
        session.removeCallback(mySessionCallback);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mFacebookUiHelper.onSaveInstanceState(outState);
    }

    private class MySessionStatusCallback implements com.kakao.SessionCallback {
        @Override
        public void onSessionOpened() {

            AniCareLogger.log("SessionOpend");

            UserManagement.requestMe(new MeResponseCallback() {

                @Override
                protected void onSuccess(final UserProfile userProfile) {
                    String profileImageURL = userProfile.getProfileImagePath();

                    AniCareUser aniCareUser = new AniCareUser();
                    aniCareUser.setPlatformId(String.valueOf(userProfile.getId()));
                    aniCareUser.setName(userProfile.getNickname());
                    aniCareUser.setImageUrl(profileImageURL);

                    login(aniCareUser);
                }

                @Override
                protected void onNotSignedUp() {

                }

                @Override
                protected void onSessionClosedFailure(final APIErrorResult errorResult) {

                }

                @Override
                protected void onFailure(final APIErrorResult errorResult) {

                }
            });
        }

        @Override
        public void onSessionClosed(final KakaoException exception) {

//            mKakaoButton.setVisibility(View.VISIBLE);
        }

        @Override
        public void onSessionOpening() {

        }
    }
}
