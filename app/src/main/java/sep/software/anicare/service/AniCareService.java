package sep.software.anicare.service;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import sep.software.anicare.callback.ListCallback;
import sep.software.anicare.event.AniCareMessage;
import sep.software.anicare.callback.EntityCallback;
import sep.software.anicare.model.AniCarePet;
import sep.software.anicare.model.AniCareUser;
import sep.software.anicare.model.CareHistory;

/**
 * Created by hongkunyoo on 15. 5. 2..
 */
public interface AniCareService {

    /*
        Login API (Splash Activity)
     */
    public void login(AniCareUser user, final EntityCallback<AniCareUser> callback) ;
    public void logout() ;
    public boolean isLoggedIn() ;

    /*
        UserSetting API
     */
    public void putUser(AniCareUser user, final EntityCallback<AniCareUser> callback);
    public boolean isUserSet();
    public AniCareUser getCurrentUser();

    /*
        PetSetting API
     */
    public void putPet(AniCarePet pet, final EntityCallback<AniCarePet> callback);
    public boolean isPetSet();

    /*
        ListFriend API
     */
    public void listPet(int page, String userId, final ListCallback<AniCarePet> callback);

    /*
        MakeFriend API
     */
    public void makeFriend(AniCarePet pet, final EntityCallback<AniCarePet> callback);

    /*
        Message API (MessageBox Fragment)
     */
    // This method is used in SplashActivity
    public void getGcmRegistrationId(final EntityCallback<String> callback) ;
    public void sendMessage(AniCareMessage message, final EntityCallback<AniCareMessage> callback);

    /*
        CareHistory API
     */
    public void listHistory(String userId,  final ListCallback<CareHistory> callback);

    /*
        BlobStorage API - To upload Bitmap Images
     */
    public void uploadUserImage(String id, Bitmap image, EntityCallback<String> callback);
    public void uploadPetImage(String id, Bitmap image, EntityCallback<String> callback);

    public void setUserImageIntro(String userId, ImageView view);
    public void setPetImageIntro(String petId, ImageView view);
    public String getUserImageUrl(String id);
    public String getPetImageUrl(String id);

}
