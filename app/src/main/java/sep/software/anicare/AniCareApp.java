package sep.software.anicare;

import sep.software.anicare.service.AniCareService;
import sep.software.anicare.service.BlobStorageService;
import sep.software.anicare.util.ObjectPreferenceUtil;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;

public class AniCareApp extends Application {
	private static volatile AniCareApp instance = null;
	private ProgressDialog progressDialog;
	
	public static synchronized AniCareApp getAppContext() {
		return instance;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		// initialize AniCareService
		AniCareService.init(this);
		// initialize BlobStorageService
		BlobStorageService.init();
		// initialize ObjectPreferenceUtil
		ObjectPreferenceUtil.init(this);
		instance = this;
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
}
