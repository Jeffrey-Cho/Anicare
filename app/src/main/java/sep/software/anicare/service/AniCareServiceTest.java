package sep.software.anicare.service;

import android.graphics.Bitmap;

import sep.software.anicare.callback.EntityCallback;
import sep.software.anicare.event.AniCareMessage;
import sep.software.anicare.model.AniCareUser;

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
    public void getGcmRegistrationId(EntityCallback<String> callback) {

    }

    @Override
    public void sendMessage(AniCareMessage message, EntityCallback<AniCareMessage> callback) {

    }

    @Override
    public void uploadUserImage(String id, Bitmap image, EntityCallback<String> callback) {

    }

    @Override
    public void uploadPetImage(String id, Bitmap image, EntityCallback<String> callback) {

    }


}
