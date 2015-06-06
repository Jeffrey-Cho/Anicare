package sep.software.anicare;

import sep.software.anicare.activity.MainActivity;
import sep.software.anicare.model.AniCareMessage;
import sep.software.anicare.model.AniCareUser;
import sep.software.anicare.service.AniCareService;
import sep.software.anicare.util.AniCareLogger;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class AniCareIntentService extends IntentService {

	public static final int NOTIFICATION_ID = 1;

	private Context mAppContext;
    private GsonBuilder mGb = new GsonBuilder();

	public AniCareIntentService() {
		this("AniCareIntentService");
	}
	public AniCareIntentService(String name) {
		super(name);
	}


	public void onHandleIntent(Intent intent) {
        mAppContext = AniCareApp.getAppContext();
		String unRegistered = intent.getStringExtra("unregistered");
		if (unRegistered != null && unRegistered.equals(AniCareProtocol.GOOGLE_PLAY_APP_ID)){
			return;	
		}
		String dataFromServer = intent.getExtras().getString("message");
		alertNotification(dataFromServer);
	}


	private void alertNotification(String dataFromServerStr){
		final JsonObject dataFromServer = mGb.create().fromJson(dataFromServerStr, JsonObject.class);
//		final PendingIntent pendingIntent = getPendingIntent(message);
        if (dataFromServer == null) {
            AniCareLogger.log("message is NULL");
            return;
        }
        JsonObject msgObj = dataFromServer.get("message").getAsJsonObject();
        JsonObject userObj = dataFromServer.get("user").getAsJsonObject();

        AniCareMessage msg = mGb.create().fromJson(msgObj, AniCareMessage.class);
        AniCareUser user = mGb.create().fromJson(userObj, AniCareUser.class);

        AniCareApp appContext = AniCareApp.getAppContext();
        AniCareService aniCareService = appContext.getAniCareService();
        AniCareLogger.log("in intent : "+msg);
        aniCareService.addMessageDB(msg);

        notifyMessage(msg);

//        if(AudioManager.RINGER_MODE_SILENT != audioManager.getRingerMode()){
//            ((Vibrator)getSystemService(Context.VIBRATOR_SERVICE)).vibrate(500);
//        }

//		AsyncChainer.asyncChain(mThis, new Chainable(){
//
//			@Override
//			public void doNext(Object obj, Object... params) {
//				if(!noti.getType().equals(ItNotification.TYPE.ProductTag.toString())){
//					getLargeIcon(obj, BlobStorageHelper.getUserProfileImgUrl(noti.getWhoMadeId()));
//				} else {
//					Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.noti_label_img);
//					AsyncChainer.notifyNext(obj, largeIcon);
//				}
//			}
//		}, new Chainable(){
//
//			@Override
//			public void doNext(Object obj, Object... params) {
//				Bitmap largeIcon = (Bitmap)params[0];
//				Notification notification = getNotification(noti, pendingIntent, largeIcon);
//
//				// Notify
//				NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//				mNotificationManager.notify(NOTIFICATION_ID, notification);
//
//				// Add Noti Number preference
//				int notiNumber = mApp.getPrefHelper().getInt(ItConstant.NOTIFICATION_NUMBER_KEY);
//				mApp.getPrefHelper().put(ItConstant.NOTIFICATION_NUMBER_KEY, ++notiNumber);
//				EventBus.getDefault().post(new NotificationEvent(noti));
//
//				// For Vibration
//				AudioManager audioManager = (AudioManager) mThis.getSystemService(Context.AUDIO_SERVICE);
//				if(AudioManager.RINGER_MODE_SILENT != audioManager.getRingerMode()){
//					((Vibrator)getSystemService(Context.VIBRATOR_SERVICE)).vibrate(500);
//				}
//			}
//		});
	}


	private PendingIntent getPendingIntent(AniCareMessage msg){
		// The stack builder object will contain an artificial back stack for the started Activity.
		// This ensures that navigating backward from the Activity leads out of your application to the Home screen.
		// Adds the Intent that starts the Activity to the top of the stack
//		Intent resultIntent = new Intent(mThis, ItemActivity.class);
//		resultIntent.putExtra(Item.INTENT_KEY, noti.makeItem());
//
//		TaskStackBuilder stackBuilder = TaskStackBuilder.create(mThis);
//		stackBuilder.addParentStack(ItemActivity.class);
//		stackBuilder.addNextIntent(resultIntent);
//
//		return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
		return null;
	}


//	private void getLargeIcon(final Object obj, final String url){
//		(new AsyncTask<Void,Void,Bitmap>(){
//
//			@Override
//			protected Bitmap doInBackground(Void... params) {
//				Bitmap bitmap = null;
//				try {
//					bitmap = mApp.getPicasso().load(url).get();
//				} catch (IOException e) {
//					EventBus.getDefault().post(new ItException("getNotification", ItException.TYPE.INTERNAL_ERROR));
//				}
//				return bitmap;
//			}
//
//			@Override
//			protected void onPostExecute(Bitmap result) {
//				AsyncChainer.notifyNext(obj, result);
//			};
//		}).execute();
//	}


	private Notification getNotification(AniCareMessage message, PendingIntent resultPendingIntent, Bitmap largeIcon){
		// Set Notification
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mAppContext)
		.setSmallIcon(R.drawable.ic_launcher)
//		.setLargeIcon(largeIcon)
		.setContentTitle(message.getSender())
		.setContentText(message.getContent())
		.setAutoCancel(true)
		.setContentIntent(resultPendingIntent);
		return mBuilder.build();
	}


    private void notifyMessage(AniCareMessage message){
		/*
		 * Creates an explicit intent for an Activity in your app
		 */
        Intent resultIntent = new Intent(this, MainActivity.class);
        String title = message.getSender();
        String content = message.getContent();


        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mAppContext);

        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);

        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);

        // Set intent and bitmap
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
		/*
		 * Set Notification
		 */
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mAppContext)
                .setSmallIcon(R.drawable.ic_launcher)
//                .setLargeIcon(bitmap)
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true);
        mBuilder.setContentIntent(resultPendingIntent);

        // Notify!
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());

        // For Vibration
        AudioManager audioManager = (AudioManager) mAppContext.getSystemService(Context.AUDIO_SERVICE);
        if(AudioManager.RINGER_MODE_SILENT != audioManager.getRingerMode()){
            ((Vibrator)getSystemService(Context.VIBRATOR_SERVICE)).vibrate(800);
        }
    }
}
