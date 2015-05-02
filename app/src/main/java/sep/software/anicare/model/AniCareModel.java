package sep.software.anicare.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.GsonBuilder;

/**
 * Created by hongkunyoo on 15. 5. 2..
 */
public class AniCareModel implements Parcelable {

    private static GsonBuilder gb = new GsonBuilder();
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(gb.create().toJson(this));
    }

    @Override
    public String toString() {
        return gb.create().toJson(this);
    }

    public static <E> E toClass(Parcel in, Class<E> clazz) {
        return gb.create().fromJson(in.readString(), clazz);
    }
}
