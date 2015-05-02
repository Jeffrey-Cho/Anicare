package sep.software.anicare.model;

import sep.software.anicare.util.RandomUtil;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

public class AniCareUser implements Parcelable {
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

    public String toString() {
        return new Gson().toJson(this);
    }

    public static AniCareUser rand() {
        AniCareUser user = new AniCareUser();
        user.setName(RandomUtil.getName());
        user.setPlatformId(RandomUtil.getString(10));
        user.setRegistrationId(RandomUtil.getString(10));
        return user;
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub

    }
}
