package sep.software.anicare.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hongkunyoo on 15. 5. 2..
 */
public class AniCarePet extends AniCareModel {
    private String id;
    private String name;




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
