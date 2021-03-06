package sep.software.anicare.service;

import android.graphics.Bitmap;
import android.widget.ImageView;

import java.util.List;

import sep.software.anicare.interfaces.ListCallback;
import sep.software.anicare.model.AniCareMessage;
import sep.software.anicare.interfaces.EntityCallback;
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
    public void dropout();
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
    public AniCarePet getCurrentPet();
    public void removePetSetting();

    /*
        ListFriend API
     */
    public void listPet(int page, int mode, String userId, final ListCallback<AniCarePet> callback);

    /*
        MakeFriend API
     */
    public void makeFriend(AniCarePet pet, final EntityCallback<AniCarePet> callback);
    public void removeFriend(AniCarePet pet, final EntityCallback<Boolean> callback);

    /*
        Match API
     */
//    public void match(AniCareMessage msg, final EntityCallback<AniCareMessage> callback);


    /*
        Message API (MessageBox Fragment)
     */
    // This method is used in SplashActivity
    public void getGcmRegistrationId(final EntityCallback<String> callback) ;

    public void sendMessage(AniCareMessage message, final EntityCallback<AniCareMessage> callback);
    public List<AniCareMessage> listMessage();
    public void addMessageDB(AniCareMessage msg);
    public void updateMessageDB(String id, AniCareMessage msg);
    public void deleteMessageDB(String id);
    public void deleteAllMessageDB();

    public boolean hasNotResolvedMessage();
    public List<AniCareMessage> listNotResolvedMessage();
    public List<AniCareMessage> listSystemMessage();
    public List<AniCareMessage> listSendedMessage();
    public List<AniCareMessage> listReceivedMessage();
    public List<AniCareMessage> listReadedMessage();

    /*
        CareHistory API
     */
    public List<CareHistory> listHistory();
    public void addHistoryDB(CareHistory history);
    public void updateHistoryDB(String id, CareHistory history);
    public void deleteHistoryDB(String id);
    public void deleteAllHistoryDB();
    public int getPoint();
    public void plsPoint(int point);
    public void minPoint(int point);

    /*
        BlobStorage API - To upload Bitmap Images
     */
    public void uploadUserImage(String id, Bitmap image, EntityCallback<String> callback);
    public void uploadPetImage(String id, Bitmap image, EntityCallback<String> callback);

    public void setUserImageInto(String userId, ImageView view);
    public void setPetImageInto(AniCarePet pet, ImageView view);
    public String getUserImageUrl(String id);
    public String getPetImageUrl(String id);

    public void saveFlag(String id, boolean value);
    public boolean getFlag(String id);

}
