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
    private String location;
    private String address1;
    private String address2;
    private String address3;
    private String selfIntro;
    private double longitude;
    private double latitude;
    private int point;
    private int rawHouseType;
    private boolean hasPet;
    public enum HouseType {
        HOUSE(1), APART(2),OFFICE_TEL(3), Etc(4);

        private final int value;
        HouseType(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
    }


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
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    public String getSelfIntro() {
        return selfIntro;
    }

    public void setSelfIntro(String selfIntro) {
        this.selfIntro = selfIntro;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getRawHouseType() {
        return rawHouseType;
    }

    public void setRawHouseType(int rawHouseType) {
        this.rawHouseType = rawHouseType;
    }

    public HouseType getHouseType() {
        return HouseType.values()[this.rawHouseType];
    }

    public void setHouseType(HouseType houseType) {
        this.rawHouseType = houseType.getValue();
    }

    public boolean isHasPet() {
        return hasPet;
    }

    public void setHasPet(boolean hasPet) {
        this.hasPet = hasPet;
    }

    public double getLongitude() { return this.longitude; }

    public void setLongitude(double longitude) { this.longitude = longitude; }

    public double getLatitude() { return this.latitude; }

    public void setLatitude(double latitude) { this.latitude = latitude; }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public static AniCareUser rand() {
        return rand(false);
    }
    public static AniCareUser rand(boolean hasId) {

        AniCareUser user = new AniCareUser();
        if (hasId)
            user.setId(RandomUtil.getId());

        user.setName(RandomUtil.getName());
        user.setPlatformId(RandomUtil.getId());
        user.setRegistrationId(RandomUtil.getString(10));
        user.setLocation("Seoul");
        user.setSelfIntro("Hello, my name is " + user.getName());
        user.setPoint(RandomUtil.getInt(20));
        user.setRawHouseType(RandomUtil.getInt(3));
        user.setHasPet(RandomUtil.getBoolean());
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
