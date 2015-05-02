package sep.software.anicare.callback;

import com.google.gson.JsonElement;

public interface JsonCallback {
	public void onCompleted (JsonElement entity);
}
