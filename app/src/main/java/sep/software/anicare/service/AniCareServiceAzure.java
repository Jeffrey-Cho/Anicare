package sep.software.anicare.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import sep.software.anicare.AniCareApp;
import sep.software.anicare.AniCareProtocol;
import sep.software.anicare.interfaces.ListCallback;
import sep.software.anicare.AniCareException;
import sep.software.anicare.model.AniCareMessage;
import sep.software.anicare.interfaces.EntityCallback;
import sep.software.anicare.model.AniCarePet;
import sep.software.anicare.model.AniCareUser;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.microsoft.windowsazure.mobileservices.ApiJsonOperationCallback;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import de.greenrobot.event.EventBus;
import sep.software.anicare.model.CareHistory;


public class AniCareServiceAzure implements AniCareService {

    private MobileServiceClient mobileClient;
    private final String AZURE_URL = "https://ani-care.azure-mobile.net/";
    private final String AZURE_KEY = "yHhHAGwAeqdZDFgMZkXYWVZEgQucFr12";
    private BlobStorageService mBlobStorageService;


    public AniCareServiceAzure(Context context) {

        try {
            mobileClient = new MobileServiceClient(
                    AZURE_URL,
                    AZURE_KEY,
                    context);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mBlobStorageService = new BlobStorageService();
    }

    public void login(AniCareUser user, final EntityCallback<AniCareUser> callback) {
        Gson gson = new Gson();
        JsonObject userJson = gson.fromJson(gson.toJson(user), JsonObject.class);
        JsonObject jo = new JsonObject();
        jo.add("user", userJson);

        mobileClient.invokeApi("login", jo, new ApiJsonOperationCallback() {

            @Override
            public void onCompleted(JsonElement arg0, Exception arg1,
                                    ServiceFilterResponse arg2) {
                // TODO Auto-generated method stub
                AniCareUser user = new Gson().fromJson(arg0, AniCareUser.class);
                AniCareApp.getAppContext().getObjectPreference().putClass(user);
                callback.onCompleted(user);
            }
        });
    }

    public boolean isLoggedIn() {
        AniCareUser user = AniCareApp.getAppContext().getObjectPreference().getClass(AniCareUser.class);
        return user != null && user.getId() != null && !user.getId().equals("");
    }

    public void logout() {
        AniCareApp.getAppContext().getObjectPreference().removeClass(AniCareUser.class);
    }


    @Override
    public void putUser(AniCareUser user, EntityCallback<AniCareUser> callback) {

    }

    @Override
    public boolean isUserSet() {
        return false;
    }

    public AniCareUser getCurrentUser() {
        return null;
    }

    @Override
    public void putPet(AniCarePet pet, EntityCallback<AniCarePet> callback) {

    }

    @Override
    public boolean isPetSet() {
        return false;
    }

    public AniCarePet getCurrentPet() {
        return null;
    }

    @Override
    public void removePetSetting() {

    }

    @Override
    public void listPet(int mode, String userId, ListCallback<AniCarePet> callback) {

    }


    @Override
    public void makeFriend(AniCarePet pet, EntityCallback<AniCarePet> callback) {

    }

    public void getGcmRegistrationId(final EntityCallback<String> callback) {
        (new AniCareAsyncTask<GoogleCloudMessaging, Void, String>() {

            @Override
            protected String doInBackground(GoogleCloudMessaging... params) {
                GoogleCloudMessaging gcm = params[0];
                try {
                    return gcm.register(AniCareProtocol.GCM_SENDER_ID);
                } catch (IOException e) {
                    EventBus.getDefault().post(new AniCareException(AniCareException.TYPE.SERVER_ERROR));
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                callback.onCompleted(result);
            }
        }).execute(GoogleCloudMessaging.getInstance(AniCareApp.getAppContext()));
    }

    @Override
    public void addMessage(AniCareMessage msg) {
//        mDbService.addMessage(msg);
    }

    public void sendMessage(AniCareMessage message, final EntityCallback<AniCareMessage> callback){

        mobileClient.invokeApi("send_message", new Gson().fromJson(message.toString(), JsonElement.class), new ApiJsonOperationCallback() {

            @Override
            public void onCompleted(JsonElement arg0, Exception arg1,
                                    ServiceFilterResponse arg2) {
                // TODO Auto-generated method stub
                if (arg1 == null) {
                    callback.onCompleted(new Gson().fromJson(arg0, AniCareMessage.class));
                } else {
                    EventBus.getDefault().post(new AniCareException(AniCareException.TYPE.SERVER_ERROR));
                }

            }
        });
    }

    @Override
    public List<AniCareMessage> listMessage() {
        return null;
    }

    @Override
    public List<CareHistory> listHistory() {
        return null;
    }

    @Override
    public void uploadUserImage(String id, Bitmap image, final EntityCallback<String> callback) {
        mBlobStorageService.uploadBitmapAsync(BlobStorageService.CONTAINER_USER_PROFILE, id, image,
                new EntityCallback<String>() {

            @Override
            public void onCompleted(String entity) {
                callback.onCompleted(entity);
            }
        });
    }

    @Override
    public void uploadPetImage(String id, Bitmap image, final EntityCallback<String> callback) {
        mBlobStorageService.uploadBitmapAsync(BlobStorageService.CONTAINER_IMAGE, id, image,
                new EntityCallback<String>() {

            @Override
            public void onCompleted(String entity) {
                callback.onCompleted(entity);
            }
        });
    }

    @Override
    public void setUserImageInto(String userId, ImageView view) {

    }

    @Override
    public void setPetImageInto(AniCarePet pet, ImageView view) {

    }

    @Override
    public String getUserImageUrl(String id) {
        return null;
    }

    @Override
    public String getPetImageUrl(String id) {
        return null;
    }
}
