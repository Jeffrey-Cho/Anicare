package sep.software.anicare.service;

import android.graphics.Bitmap;

import sep.software.anicare.event.AniCareMessage;
import sep.software.anicare.callback.EntityCallback;
import sep.software.anicare.model.AniCareUser;

/**
 * Created by hongkunyoo on 15. 5. 2..
 */
public interface AniCareService {

    public void login(AniCareUser user, final EntityCallback<AniCareUser> callback) ;

    public void logout() ;

    public boolean isLoggedIn() ;

    public void getGcmRegistrationId(final EntityCallback<String> callback) ;

    public void sendMessage(AniCareMessage message, final EntityCallback<AniCareMessage> callback);

    public void uploadUserImage(String id, Bitmap image, EntityCallback<String> callback);

    public void uploadPetImage(String id, Bitmap image, EntityCallback<String> callback);


}
