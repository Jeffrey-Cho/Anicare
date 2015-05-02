package sep.software.anicare.service;

import org.apache.http.client.methods.HttpRequestBase;

import sep.software.anicare.interfaces.JsonCallback;
import sep.software.anicare.model.AniCareUser;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

class TestRequestTask implements RequestTask {

	@Override
	public void executeAsync(HttpRequestBase request, final JsonCallback callback) {
		// TODO Auto-generated method stub
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				return null;
			}
			
			protected void onPostExecute(Void result) {
				callback.onCompleted(new Gson().fromJson(AniCareUser.rand().toString(), JsonElement.class));
			};
		}.execute();
	}

}
