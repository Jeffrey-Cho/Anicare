package sep.software.anicare.service;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.facebook.Session;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import sep.software.anicare.AniCareApp;
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

    private List<AniCarePet> petList = new ArrayList<AniCarePet>();
    private List<CareHistory> historyList = new ArrayList<CareHistory>();

    public AniCareServiceTest() {
        mDbService = new AniCareDBServicePreference();

        for (int i = 0 ; i < 10 ; i++) {
            petList.add(AniCarePet.rand(true));
        }

        for (int i = 0 ; i < 10 ; i++) {
            historyList.add(CareHistory.rand(true));
        }

    }

    private void delay() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void login(AniCareUser user, EntityCallback<AniCareUser> callback) {
        delay();
        user.setId(RandomUtil.getId());
        mObjectPreference.put("user", user);
        callback.onCompleted(user);
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
        AniCareLogger.log("isLoggedIn : "+ user != null && user.getId() != null && !user.getId().equals(""));
        return user != null && user.getId() != null && !user.getId().equals("");
    }

    @Override
    public void putUser(AniCareUser user, EntityCallback<AniCareUser> callback) {
        delay();
        mObjectPreference.put("user", user);
        callback.onCompleted(user);
    }

    @Override
    public boolean isUserSet() {
        AniCareUser user = mObjectPreference.get("user", AniCareUser.class);
        AniCareLogger.log("isUserSet : "+ user);
        if (user != null) {
            if (user.getId() != null && !user.getId().equals("") &&
                    user.getLocation() != null && !"".equals(user.getLocation())) return true;
        }
        return false;
    }

    @Override
    public AniCareUser getCurrentUser() {
        AniCareUser user = mObjectPreference.get("user", AniCareUser.class);
        AniCareLogger.log("getCurrentUser : "+user);
        return user;
    }

    @Override
    public void putPet(AniCarePet pet, EntityCallback<AniCarePet> callback) {
        delay();
        pet.setId(RandomUtil.getId());
        mObjectPreference.put("pet",pet);
        callback.onCompleted(pet);
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
    public void listPet(int page, String userId, ListCallback<AniCarePet> callback) {
        delay();
        callback.onCompleted(petList, petList.size());
    }

    @Override
    public void makeFriend(AniCarePet pet, EntityCallback<AniCarePet> callback) {
        pet.setId(RandomUtil.getId());
        petList.add(pet);
        callback.onCompleted(pet);
    }

    @Override
    public void getGcmRegistrationId(EntityCallback<String> callback) {
        callback.onCompleted(RandomUtil.getId());
    }

    @Override
    public void sendMessage(AniCareMessage message, EntityCallback<AniCareMessage> callback) {
        delay();
        message.setId(RandomUtil.getId());
        callback.onCompleted(message);
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
    public void uploadUserImage(String id, Bitmap image, EntityCallback<String> callback) {
        delay();
        callback.onCompleted(RandomUtil.getId());
    }

    @Override
    public void uploadPetImage(String id, Bitmap image, EntityCallback<String> callback) {
        delay();
        callback.onCompleted(RandomUtil.getId());
    }

    @Override
    public void setUserImageIntro(String userId, ImageView view){
        Picasso.with(AniCareApp.getAppContext()).load(getUserImageUrl(userId)).into(view);
    }

    @Override
    public void setPetImageIntro(String petId, ImageView view){
        Picasso.with(AniCareApp.getAppContext()).load(getPetImageUrl(petId)).into(view);
    }

    @Override
    public String getUserImageUrl(String id){
        return "http://thecontentwrangler.com/wp-content/uploads/2011/08/User.png";
    }

    @Override
    public String getPetImageUrl(String id) {
        return "http://images5.fanpop.com/image/photos/27300000/Doggy-dogs-27378007-400-300.jpg";
    }


}
