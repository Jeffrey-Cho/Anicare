package sep.software.anicare.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.plus.model.people.Person;

import sep.software.anicare.util.RandomUtil;

/**
 * Created by hongkunyoo on 15. 5. 2..
 */
public class AniCarePet extends AniCareModel {

    public enum Category {
        DOG(0), CAT(1), BIRD(2), ETC(3);

        private final int value;
        Category(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
    }
    public enum Size {
        SMALL(1), MIDDLE(2), BIG(3);

        private final int value;
        Size(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
    }
    public enum Personality {
        SHY(1), NORMAL(2), BRIGHT(3);

        private final int value;
        Personality(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
    }

    private String id;
    private String name;

    private String userId;
    private String userName;

    private String location;
    private int houseType;
    private String imageURL;

    private int rawCategory;
    private int rawSize;
    private int rawPersonality;
    private boolean isMale;
    private boolean isNeutralized;
    private boolean isPetFood;

    private String start;
    private String end;

    private double longitude;
    private double latitude;

    private boolean isTestData;

    private String selfIntro;
    private String address1;
    private String address2;
    private String address3;



    public String getId(){ return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return  userId; }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setImageURL(String url) {
        this.imageURL = url;
    }

    public String getImageURL() {
        return imageURL;
    }
    public Category getCategory() {
        return Category.values()[this.rawCategory];
    }

    public void setCategory(Category category) {
        this.rawCategory = category.getValue();
    }

    public Size getSize() {
        return Size.values()[this.rawSize-1];
    }

    public void setSize(Size size) {
        this.rawSize = size.getValue();
    }

    public Personality getPersonality() {
        return Personality.values()[this.rawPersonality-1];
    }

    public void setPersonality(Personality personality) {
        this.rawPersonality = personality.getValue();
    }

    public boolean isMale() {
        return isMale;
    }

    public void setMale(boolean isMale) {
        this.isMale = isMale;
    }

    public boolean isNeutralized() {
        return isNeutralized;
    }

    public void setNeutralized(boolean isNeutralized) {
        this.isNeutralized = isNeutralized;
    }

    public boolean isPetFood() {
        return isPetFood;
    }

    public void setPetFood(boolean isPetFood) {
        this.isPetFood = isPetFood;
    }

    public int getRawCategory() {
        return rawCategory;
    }

    public void setRawCategory(int rawCategory) {
        this.rawCategory = rawCategory;
    }

    public int getRawSize() {
        return rawSize;
    }

    public void setRawSize(int rawSize) {
        this.rawSize = rawSize;
    }

    public int getRawPersonality() {
        return rawPersonality;
    }

    public void setRawPersonality(int rawPersonality) {
        this.rawPersonality = rawPersonality;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public AniCareUser.HouseType getHouseType() {
        return AniCareUser.HouseType.values()[this.houseType-1];
    }

    public void setHouseType(AniCareUser.HouseType houseType) {
        this.houseType = houseType.getValue();
    }

    public AniCareDateTime getStartDate() {
        return new AniCareDateTime(start);
    }

    public void setStartDate(AniCareDateTime start) {
        this.start = start.toString();
    }

    public void setStartDate(String start) {
        this.start = start;
    }

    public AniCareDateTime getEndDate() {
        return new AniCareDateTime(end);
    }

    public void setEndDate(AniCareDateTime end) {
        this.end = end.toString();
    }

    public void setEndDate(String end) {
        this.end = end;
    }

    public double getLongitude() { return this.longitude; }

    public void setLongitude(double longitude) { this.longitude = longitude; }

    public double getLatitude() { return this.latitude; }

    public void setLatitude(double latitude) { this.latitude = latitude; }

    public boolean isTestData() { return isTestData; }

    public void setTestData(boolean isTestData) {
        this.isTestData = isTestData;
    }

    public String getSelfIntro() { return this.selfIntro; }

    public void setSelfIntro(String selfIntro) { this.selfIntro = selfIntro; }

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

    public static AniCarePet rand() {
        return rand(false);
    }

    public static AniCarePet rand(boolean hasId) {
        AniCarePet pet = new AniCarePet();
        if (hasId)
            pet.setId(RandomUtil.getId());

        pet.setName(RandomUtil.getName());
        pet.setRawCategory(RandomUtil.getInt(Category.values().length));
        pet.setRawSize(RandomUtil.getInt(Size.values().length));
        pet.setRawPersonality(RandomUtil.getInt(Personality.values().length));
        pet.setMale(RandomUtil.getBoolean());
        pet.setNeutralized(RandomUtil.getBoolean());
        pet.setPetFood(RandomUtil.getBoolean());

        return pet;
    }

    /*
	 * Parcelable
	 */
    public static final Parcelable.Creator<AniCarePet> CREATOR = new Creator<AniCarePet>(){
        public AniCarePet createFromParcel(Parcel in){
            return toClass(in, AniCarePet.class);
        }
        public AniCarePet[] newArray(int size){
            return new AniCarePet[size];
        }
    };


}
