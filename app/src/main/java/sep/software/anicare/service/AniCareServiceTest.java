package sep.software.anicare.service;

import android.graphics.Bitmap;

import sep.software.anicare.callback.EntityCallback;
import sep.software.anicare.callback.ListCallback;
import sep.software.anicare.event.AniCareMessage;
import sep.software.anicare.model.AniCarePet;
import sep.software.anicare.model.AniCareUser;
import sep.software.anicare.model.CareHistory;

/**
 * Created by hongkunyoo on 15. 5. 2..
 */
public class AniCareServiceTest implements AniCareService {
    @Override
    public void login(AniCareUser user, EntityCallback<AniCareUser> callback) {

    }

    @Override
    public void logout() {

    }

    @Override
    public boolean isLoggedIn() {
        return false;
    }

    @Override
    public void putUser(AniCareUser user, EntityCallback<AniCareUser> callback) {

    }

    @Override
    public boolean isUserSet() {
        return false;
    }

    @Override
    public void putPet(AniCarePet pet, EntityCallback<AniCarePet> callback) {

    }

    @Override
    public boolean isPetSet() {
        return false;
    }

    @Override
    public void listPet(int page, String userId, ListCallback<AniCarePet> callback) {

    }

    @Override
    public void makeFriend(AniCarePet pet, EntityCallback<AniCarePet> callback) {

    }

    @Override
    public void getGcmRegistrationId(EntityCallback<String> callback) {

    }

    @Override
    public void sendMessage(AniCareMessage message, EntityCallback<AniCareMessage> callback) {

    }

    @Override
    public void listHistory(String userId, ListCallback<CareHistory> callback) {

    }

    @Override
    public void uploadUserImage(String id, Bitmap image, EntityCallback<String> callback) {

    }

    @Override
    public void uploadPetImage(String id, Bitmap image, EntityCallback<String> callback) {

    }


}
