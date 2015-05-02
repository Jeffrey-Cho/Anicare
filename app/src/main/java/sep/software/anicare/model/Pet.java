package sep.software.anicare.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

/**
 * Created by hongkunyoo on 15. 5. 2..
 */
public class Pet extends AniCareModel {
    private String id;
    private String name;




    /*
	 * Parcelable
	 */
    public static final Parcelable.Creator<Pet> CREATOR = new Creator<Pet>(){
        public Pet createFromParcel(Parcel in){
            return toClass(in, Pet.class);
        }
        public Pet[] newArray(int size){
            return new Pet[size];
        }
    };


}
