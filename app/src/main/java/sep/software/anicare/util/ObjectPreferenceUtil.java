package sep.software.anicare.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class ObjectPreferenceUtil {

    private SharedPreferences mPref;
    private String DEFAULT_VALUE = new JsonObject().toString();

    public ObjectPreferenceUtil(Context context) {
        mPref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public <E> void put(String key, E obj) {
        mPref.edit().putString(key, new Gson().toJson(obj)).commit();
    }

    public <E> E get(String key, Class<E> clazz) {
        String jsonStr = mPref.getString(key, DEFAULT_VALUE);
        return new Gson().fromJson(jsonStr, clazz);
    }

    public <E> void remove(String key) {
        mPref.edit().remove(key).commit();
    }


    public <E> void putClass(E obj) {
        Class<?> clazz = obj.getClass();
        mPref.edit().putString(clazz.getName(), new Gson().toJson(obj)).commit();
    }

    public <E> E getClass(Class<E> clazz) {
        String jsonStr = mPref.getString(clazz.getName(), DEFAULT_VALUE);
        return new Gson().fromJson(jsonStr, clazz);
    }

    public <E> void removeClass(Class<E> clazz) {
        mPref.edit().remove(clazz.getName()).commit();
    }
}
