package sep.software.anicare;

import sep.software.anicare.event.AniCareMessage;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat;

import com.google.gson.Gson;

public class AniCareIntentService extends IntentService {

	public static final int NOTIFICATION_ID = 1;

	private Context mThis;

	public AniCareIntentService() {
		this("AniCareIntentService");
	}
	public AniCareIntentService(String name) {
		super(name);
	}


	public void onHandleIntent(Intent intent) {
		String unRegisterd = intent.getStringExtra("unregistered");
		if (unRegisterd != null && unRegisterd.equals(AniCareProtocol.GOOGLE_PLAY_APP_ID)){
			return;	
		}

		String message = intent.getExtras().getString("message");
		alertNotification(message);
	}


	private void alertNotification(String messageStr){
		final AniCareMessage message = new Gson().fromJson(messageStr, AniCareMessage.class);
		final PendingIntent pendingIntent = getPendingIntent(message);

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
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mThis)
//		.setSmallIcon(R.drawable.ic_stat_notify)
		.setLargeIcon(largeIcon)
//		.setContentTitle(noti.getWhoMade())
//		.setContentText(noti.notiContent())
		.setAutoCancel(true)
		.setContentIntent(resultPendingIntent);
		return mBuilder.build();
	}
}
