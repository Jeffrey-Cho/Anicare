package sep.software.anicare.activity;

import java.io.IOException;
import java.util.Arrays;

import sep.software.anicare.AniCareApp;
import sep.software.anicare.R;
import sep.software.anicare.event.AniCareMessage;
import sep.software.anicare.interfaces.EntityCallback;
import sep.software.anicare.model.AniCareUser;
import sep.software.anicare.service.AniCareAsyncTask;
import sep.software.anicare.service.AniCareService;
import sep.software.anicare.service.BlobStorageService;
import sep.software.anicare.util.AsyncChainer;
import sep.software.anicare.util.AsyncChainer.Chainable;
import sep.software.anicare.util.AniCareLogger;
import sep.software.anicare.util.ImageUtil;
import sep.software.anicare.util.ObjectPreferenceUtil;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
//import com.facebook.FacebookSdk;

import com.facebook.AppEventsLogger;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

import com.squareup.picasso.Picasso;

public class SplashActivity extends AniCareActivity {
	
	private LoginButton mFacebookButton;
	private UiLifecycleHelper mFacebookUiHelper;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		getActionBar().hide();
		
		mFacebookUiHelper = new UiLifecycleHelper(thisActivity, new StatusCallback() {

			@Override
			public void call(com.facebook.Session session, SessionState state, Exception exception) {
			}
		});
		mFacebookUiHelper.onCreate(savedInstanceState);
		
		setButton();
	}
	
	private void setButton(){
		mFacebookButton = (LoginButton)findViewById(R.id.login_facebook);
		
		if (AniCareService.isLoggedIn()) {
			mFacebookButton.setVisibility(View.GONE);
		}
		
		mFacebookButton.setReadPermissions(Arrays.asList("public_profile"));
//		mFacebookButton.setBackgroundResource(R.drawable.signin_button);
		mFacebookButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		mFacebookButton.setTypeface(mFacebookButton.getTypeface(), Typeface.BOLD);
		mFacebookButton.setText(getResources().getString(R.string.facebook_login));
//		mFacebookButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.font_medium));
//		mFacebookButton.setTextColor(getResources().getColor(R.color.brand_color));
		mFacebookButton.setUserInfoChangedCallback(new com.facebook.widget.LoginButton.UserInfoChangedCallback() {

			@Override
			public void onUserInfoFetched(GraphUser user) {
				com.facebook.Session session = com.facebook.Session.getActiveSession();
				if (session != null && session.isOpened() || user != null) {
					login(session, user);
				}
			}
		});
	}
	
	private void login(com.facebook.Session session, final GraphUser user){
		if (AniCareService.isLoggedIn()) {
			// wait for 2 sec.
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			goToNextActivity();
			return;
		}
		
		final AniCareUser aniCareUser = new AniCareUser();
		aniCareUser.setFbId(user.getId());
		aniCareUser.setName(user.getFirstName());
		final String fbProfileUrl = String.format("https://graph.facebook.com/%s/picture?type=large", aniCareUser.getFbId());
		
		AniCareApp.getAppContext().showProgressDialog(thisActivity);
		
		AsyncChainer.asyncChain(thisActivity, new Chainable(){

			@Override
			public void doNext(final Object obj, Object... params) {
				// TODO Auto-generated method stub
				AniCareService.getGcmRegistrationId(new EntityCallback<String>() {

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
				AniCareService.login(aniCareUser, new EntityCallback<AniCareUser>() {
					
					@Override
					public void onCompleted(AniCareUser entity) {
						// TODO Auto-generated method stub
						ObjectPreferenceUtil.put(entity);
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
							return Picasso.with(thisActivity).load(fbProfileUrl).get();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							return null;
						}
					}
					
					protected void onPostExecute(Bitmap result) {
						AsyncChainer.notifyNext(obj, result);
					};
					
				}).execute();
				
			}
		}, new Chainable(){

			@Override
			public void doNext(final Object obj, Object... params) {
				Bitmap profileImage = (Bitmap) params[0];
				Bitmap profileImageBitmap = ImageUtil.refineSquareImage(profileImage, ImageUtil.PROFILE_IMAGE_SIZE);
				BlobStorageService.uploadBitmapAsync(BlobStorageService.CONTAINER_USER_PROFILE, ObjectPreferenceUtil.get(AniCareUser.class).getId(), 
						profileImageBitmap, new EntityCallback<String>() {

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
		AniCareMessage msg = AniCareMessage.rand();
		AniCareLogger.log(msg);
		
		Intent intent = new Intent();
		intent.setClass(thisActivity, MainActivity.class);
		startActivity(intent);
		finish();
	}
	
	
	@Override
	public void onResume() {
		super.onResume();

		// Facebook
		mFacebookUiHelper.onResume();
		AppEventsLogger.activateApp(thisActivity);
	}

	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		mFacebookUiHelper.onActivityResult(requestCode, resultCode, data);
	}


	@Override
	public void onPause() {
		super.onPause();
		mFacebookUiHelper.onPause();
		AppEventsLogger.deactivateApp(thisActivity);
	}


	@Override
	protected void onStop() {
		super.onStop();
	}
	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		mFacebookUiHelper.onDestroy();
	}


	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mFacebookUiHelper.onSaveInstanceState(outState);
	}
}
