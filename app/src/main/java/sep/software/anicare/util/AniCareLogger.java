package sep.software.anicare.util;

import android.util.Log;

public class AniCareLogger {
	
	public static void log(Object... objs) {
		for (Object obj : objs) {
			Log.e("ERROR", obj.toString());
		}
	}
}
