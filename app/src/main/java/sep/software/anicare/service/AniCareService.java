package sep.software.anicare.service;

import sep.software.anicare.event.AniCareMessage;
import sep.software.anicare.callback.EntityCallback;
import sep.software.anicare.model.AniCareUser;

/**
 * Created by hongkunyoo on 15. 5. 2..
 */
public interface AniCareService {

    public void login(AniCareUser user, final EntityCallback<AniCareUser> callback) ;

    public void getGcmRegistrationId(final EntityCallback<String> callback) ;

    public void sendMessage(AniCareMessage message, final EntityCallback<AniCareMessage> callback);

    public boolean isLoggedIn() ;

    public void logout() ;
}
