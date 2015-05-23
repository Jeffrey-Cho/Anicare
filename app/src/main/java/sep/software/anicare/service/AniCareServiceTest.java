package sep.software.anicare.service;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.facebook.Session;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.microsoft.windowsazure.mobileservices.ApiJsonOperationCallback;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import sep.software.anicare.AniCareApp;
import sep.software.anicare.AniCareException;
import sep.software.anicare.AniCareProtocol;
import sep.software.anicare.interfaces.EntityCallback;
import sep.software.anicare.interfaces.ListCallback;
import sep.software.anicare.model.AniCareMessage;
import sep.software.anicare.model.AniCarePet;
import sep.software.anicare.model.AniCareUser;
import sep.software.anicare.model.CareHistory;
import sep.software.anicare.util.AniCareLogger;
import sep.software.anicare.util.ObjectPreferenceUtil;
import sep.software.anicare.util.RandomUtil;

/**
 * Created by hongkunyoo on 15. 5. 2..
 */
public class AniCareServiceTest implements AniCareService {

    private ObjectPreferenceUtil mObjectPreference = AniCareApp.getAppContext().getObjectPreference();
    private AniCareDBService mDbService;

    private MobileServiceClient mMobileClient;
    private final String AZURE_URL = "https://ani-care.azure-mobile.net/";
    private final String AZURE_KEY = "yHhHAGwAeqdZDFgMZkXYWVZEgQucFr12";
    private BlobStorageService mBlobStorageService;
    private GsonBuilder mGb;

    private String BASE_IMAGE_URL = "https://portalvhdsj2ksq9qld7v06.blob.core.windows.net";

    private List<AniCarePet> petList = new ArrayList<AniCarePet>();
    private List<CareHistory> historyList = new ArrayList<CareHistory>();

    public AniCareServiceTest(Context context) {
        mDbService = new AniCareDBServicePreference();

        for (int i = 0 ; i < 10 ; i++) {
            petList.add(AniCarePet.rand(true));
        }

        for (int i = 0 ; i < 10 ; i++) {
            historyList.add(CareHistory.rand(true));
        }

        try {
            mMobileClient = new MobileServiceClient(
                    AZURE_URL,
                    AZURE_KEY,
                    context);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        mBlobStorageService = new BlobStorageService();
        mGb = new GsonBuilder();
    }

    private void delay() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean internetAvailable() {
        return AniCareApp.getAppContext().isInternetAvailable();
    }
    @Override
    public void login(AniCareUser user, final EntityCallback<AniCareUser> callback) {

        if (!internetAvailable()) {
            EventBus.getDefault().post(new AniCareException(AniCareException.TYPE.NETWORK_UNAVAILABLE));
            return;
        }

        JsonObject userJson = mGb.create().fromJson(mGb.create().toJson(user), JsonObject.class);
        JsonObject jo = new JsonObject();
        jo.add("user", userJson);

        mMobileClient.invokeApi("login", jo, new ApiJsonOperationCallback() {

            @Override
            public void onCompleted(JsonElement arg0, Exception arg1,
                                    ServiceFilterResponse arg2) {
                // TODO Auto-generated method stub
                if (arg1 == null) {
                    AniCareUser savedUser = mGb.create().fromJson(arg0, AniCareUser.class);
                    mObjectPreference.put("user", savedUser);
                    callback.onCompleted(savedUser);
                } else {
                    EventBus.getDefault().post(new AniCareException(AniCareException.TYPE.SERVER_ERROR));
                }

            }
        });
    }

    @Override
    public void logout() {
        Session session = com.facebook.Session.getActiveSession();
        if (session != null) session.close();

        mObjectPreference.remove("user");
        mObjectPreference.remove("pet");
    }

    @Override
    public boolean isLoggedIn() {
        AniCareUser user = mObjectPreference.get("user", AniCareUser.class);
        if (user == null) return false;
        return user != null && user.getId() != null && !user.getId().equals("");
    }

    @Override
    public void putUser(AniCareUser user, final EntityCallback<AniCareUser> callback) {
        this.login(user, callback);
    }

    @Override
    public boolean isUserSet() {
        AniCareUser user = mObjectPreference.get("user", AniCareUser.class);
        if (user != null) {
            if (user.getId() != null && !user.getId().equals("") &&
                    user.getLocation() != null && !"".equals(user.getLocation())) return true;
        }
        return false;
    }

    @Override
    public AniCareUser getCurrentUser() {
        return mObjectPreference.get("user", AniCareUser.class);
    }

    @Override
    public void putPet(AniCarePet pet, final EntityCallback<AniCarePet> callback) {
        if (!internetAvailable()) {
            EventBus.getDefault().post(new AniCareException(AniCareException.TYPE.NETWORK_UNAVAILABLE));
            return;
        }

        JsonObject json = mGb.create().fromJson(mGb.create().toJson(pet), JsonObject.class);
        JsonObject jo = new JsonObject();
        jo.add("pet", json);

        mMobileClient.invokeApi("put_pet", jo, new ApiJsonOperationCallback() {

            @Override
            public void onCompleted(JsonElement arg0, Exception arg1,
                                    ServiceFilterResponse arg2) {
                // TODO Auto-generated method stub
                AniCareLogger.log("onCompleted");
                if (arg1 == null) {
                    AniCarePet savedPet = mGb.create().fromJson(arg0, AniCarePet.class);
                    mObjectPreference.put("pet", savedPet);
                    callback.onCompleted(savedPet);
                } else {
                    EventBus.getDefault().post(new AniCareException(AniCareException.TYPE.SERVER_ERROR));
                }
            }
        });
    }

    @Override
    public boolean isPetSet() {
        AniCarePet pet = mObjectPreference.get("pet", AniCarePet.class);
        return pet != null && pet.getId() != null && !pet.getId().equals("");
    }

    @Override
    public AniCarePet getCurrentPet() {
        return mObjectPreference.get("pet", AniCarePet.class);
    }

    @Override
    public void removePetSetting() {
        mObjectPreference.remove("pet");
    }

    @Override
    public void listPet(String userId, final ListCallback<AniCarePet> callback) {

        if (true) {
            callback.onCompleted(petList, petList.size());
        }

        if (!internetAvailable()) {
            EventBus.getDefault().post(new AniCareException(AniCareException.TYPE.NETWORK_UNAVAILABLE));
            return;
        }

        if (true) return;

        JsonObject jo = new JsonObject();
        jo.addProperty("userId", userId);

        mMobileClient.invokeApi("list_pet", jo, new ApiJsonOperationCallback() {

            @Override
            public void onCompleted(JsonElement arg0, Exception arg1,
                                    ServiceFilterResponse arg2) {
                // TODO Auto-generated method stub
                if (arg1 == null) {
                    JsonElement json = arg0.getAsJsonArray();
                    List<AniCarePet> list = new Gson().fromJson(json, new TypeToken<List<AniCarePet>>() {}.getType());
                    callback.onCompleted(list, list.size());
                } else {
                    EventBus.getDefault().post(new AniCareException(AniCareException.TYPE.SERVER_ERROR));
                }
            }
        });
    }

    @Override
    public void makeFriend(AniCarePet pet, final EntityCallback<AniCarePet> callback) {

        petList.add(pet);
        callback.onCompleted(pet);
        if (true) return;

        if (!internetAvailable()) {
            EventBus.getDefault().post(new AniCareException(AniCareException.TYPE.NETWORK_UNAVAILABLE));
            return;
        }

        JsonObject jo = new JsonObject();
        jo.add("pet", mGb.create().fromJson(mGb.create().toJson(pet), JsonElement.class));

        mMobileClient.invokeApi("make_friend", jo, new ApiJsonOperationCallback() {

            @Override
            public void onCompleted(JsonElement arg0, Exception arg1,
                                    ServiceFilterResponse arg2) {
                // TODO Auto-generated method stub
                if (arg1 == null) {
                    AniCarePet _pet = mGb.create().fromJson(arg0, AniCarePet.class);
                    callback.onCompleted(_pet);
                } else {
                    EventBus.getDefault().post(new AniCareException(AniCareException.TYPE.SERVER_ERROR));
                }
            }
        });

    }

    @Override
    public void getGcmRegistrationId(final EntityCallback<String> callback) {

        if (!internetAvailable()) {
            EventBus.getDefault().post(new AniCareException(AniCareException.TYPE.NETWORK_UNAVAILABLE));
            return;
        }

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
    public void sendMessage(AniCareMessage message, final EntityCallback<AniCareMessage> callback) {
        if (!internetAvailable()) {
            EventBus.getDefault().post(new AniCareException(AniCareException.TYPE.NETWORK_UNAVAILABLE));
            return;
        }

        JsonObject jo = new JsonObject();
        jo.add("message", mGb.create().fromJson(mGb.create().toJson(message), JsonElement.class));

        mMobileClient.invokeApi("send_message", jo, new ApiJsonOperationCallback() {

            @Override
            public void onCompleted(JsonElement arg0, Exception arg1,
                                    ServiceFilterResponse arg2) {
                // TODO Auto-generated method stub
                if (arg1 == null) {
                    AniCareMessage msg = mGb.create().fromJson(arg0, AniCareMessage.class);
                    mDbService.addMessage(msg);
                    callback.onCompleted(msg);
                } else {
                    EventBus.getDefault().post(new AniCareException(AniCareException.TYPE.SERVER_ERROR));
                }
            }
        });
    }

    @Override
    public List<AniCareMessage> listMessage() {
        return mDbService.listMessage();
    }

    @Override
    public List<CareHistory> listHistory() {
        return mDbService.listHistory();
    }

    @Override
    public void uploadUserImage(String id, Bitmap image, final EntityCallback<String> callback) {
        if (!internetAvailable()) {
            EventBus.getDefault().post(new AniCareException(AniCareException.TYPE.NETWORK_UNAVAILABLE));
            return;
        }

        mBlobStorageService.uploadBitmapAsync(BlobStorageService.CONTAINER_USER_PROFILE, id, image, new EntityCallback<String>() {
            @Override
            public void onCompleted(String entity) {
                callback.onCompleted(entity);
            }
        });
    }

    @Override
    public void uploadPetImage(String id, Bitmap image, final EntityCallback<String> callback) {
        if (!internetAvailable()) {
            EventBus.getDefault().post(new AniCareException(AniCareException.TYPE.NETWORK_UNAVAILABLE));
            return;
        }

        mBlobStorageService.uploadBitmapAsync(BlobStorageService.CONTAINER_IMAGE, id, image, new EntityCallback<String>() {
            @Override
            public void onCompleted(String entity) {
                callback.onCompleted(entity);
            }
        });
    }

    @Override
    public void setUserImageInto(String userId, ImageView view){
        Picasso.with(AniCareApp.getAppContext()).load(getUserImageUrl(userId)).into(view);
    }

    @Override
    public void setPetImageInto(String petId, ImageView view){
        Picasso.with(AniCareApp.getAppContext()).load(getPetImageUrl(petId)).into(view);
    }

    @Override
    public String getUserImageUrl(String id){
        return BASE_IMAGE_URL + "/" + BlobStorageService.CONTAINER_USER_PROFILE + "/" + id;
    }

    @Override
    public String getPetImageUrl(String id) {
        return BASE_IMAGE_URL + "/" + BlobStorageService.CONTAINER_IMAGE + "/" + id;
    }


}
