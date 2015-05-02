package sep.software.anicare.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

public class ObjectPreferenceUtil {
	
	private static SharedPreferences mPref;
	private static String DEFAULT_VALUE = "{}";
	
	public static void init(Context context) {
		mPref = PreferenceManager.getDefaultSharedPreferences(context);
	}
	
	public static <E> void put(E obj) {
		Class<?> clazz = obj.getClass();
		mPref.edit().putString(clazz.getName(), new Gson().toJson(obj)).commit();
	}

	public static <E> E get(Class<E> clazz) {
		String jsonStr = mPref.getString(clazz.getName(), DEFAULT_VALUE);
		return new Gson().fromJson(jsonStr, clazz);
	}

	public static <E> void remove(Class<E> clazz) {
		mPref.edit().remove(clazz.getName()).commit();
	}
}
