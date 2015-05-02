package sep.software.anicare.model;

import sep.software.anicare.util.RandomUtil;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

public class AniCareUser extends AniCareModel {
    private String id;
    private String name;
    private String platformId;
    private String registrationId;
    private String imageUrl;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPlatformId() {
        return platformId;
    }
    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }
    public String getRegistrationId() {
        return registrationId;
    }
    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public static AniCareUser rand() {
        AniCareUser user = new AniCareUser();
        user.setName(RandomUtil.getName());
        user.setPlatformId(RandomUtil.getString(10));
        user.setRegistrationId(RandomUtil.getString(10));
        return user;
    }

    /*
	 * Parcelable
	 */
    public static final Parcelable.Creator<AniCareUser> CREATOR = new Creator<AniCareUser>(){
        public AniCareUser createFromParcel(Parcel in){
            return toClass(in, AniCareUser.class);
        }
        public AniCareUser[] newArray(int size){
            return new AniCareUser[size];
        }
    };

}
