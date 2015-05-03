package sep.software.anicare.service;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

import sep.software.anicare.AniCareApp;
import sep.software.anicare.callback.EntityCallback;
import sep.software.anicare.callback.ListCallback;
import sep.software.anicare.event.AniCareMessage;
import sep.software.anicare.model.AniCarePet;
import sep.software.anicare.model.AniCareUser;
import sep.software.anicare.model.CareHistory;
import sep.software.anicare.util.RandomUtil;

/**
 * Created by hongkunyoo on 15. 5. 2..
 */
public class AniCareServiceTest implements AniCareService {

    private List<AniCarePet> petList = new ArrayList<AniCarePet>();
    private List<CareHistory> historyList = new ArrayList<CareHistory>();

    public AniCareServiceTest() {
        for (int i = 0 ; i < 20 ; i++) {
            petList.add(AniCarePet.rand(true));
        }

        for (int i = 0 ; i < 20 ; i++) {
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
        AniCareApp.getAppContext().getObjectPreference().put("login", user);
        callback.onCompleted(user);
    }

    @Override
    public void logout() {
        com.facebook.Session.getActiveSession().close();
        AniCareApp.getAppContext().getObjectPreference().remove("login");
    }

    @Override
    public boolean isLoggedIn() {
        AniCareUser user = AniCareApp.getAppContext().getObjectPreference().get("login", AniCareUser.class);
        return user != null && user.getId() != null && !user.getId().equals("");
    }

    @Override
    public void putUser(AniCareUser user, EntityCallback<AniCareUser> callback) {
        delay();
        user.setId(RandomUtil.getId());
        AniCareApp.getAppContext().getObjectPreference().put("userSetting", user);
        callback.onCompleted(user);
    }

    @Override
    public boolean isUserSet() {
        AniCareUser user = AniCareApp.getAppContext().getObjectPreference().get("userSetting", AniCareUser.class);
        return user != null && user.getId() != null && !user.getId().equals("");
    }

    @Override
    public AniCareUser getCurrentUser() {
        return null;
    }

    @Override
    public void putPet(AniCarePet pet, EntityCallback<AniCarePet> callback) {
        delay();
        pet.setId(RandomUtil.getId());
        AniCareApp.getAppContext().getObjectPreference().put("petSetting",pet);
        callback.onCompleted(pet);
    }

    @Override
    public boolean isPetSet() {
        AniCarePet pet = AniCareApp.getAppContext().getObjectPreference().get("petSetting", AniCarePet.class);
        return pet != null && pet.getId() != null && !pet.getId().equals("");
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
    public void listHistory(String userId, ListCallback<CareHistory> callback) {
        delay();
        callback.onCompleted(historyList, historyList.size());
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

    public String getUserImageUrl(String id){
        return "http://thecontentwrangler.com/wp-content/uploads/2011/08/User.png";
    }
    public String getPetImageUrl(String id) {
        return "http://images5.fanpop.com/image/photos/27300000/Doggy-dogs-27378007-400-300.jpg";
    }


}
