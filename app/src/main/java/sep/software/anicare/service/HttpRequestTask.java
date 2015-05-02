package sep.software.anicare.service;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import sep.software.anicare.interfaces.JsonCallback;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

class HttpRequestTask implements RequestTask {
	public void executeAsync(final HttpRequestBase request, final JsonCallback callback) {
		new AniCareAsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				return executeSync(request);
			}
			
			protected void onPostExecute(String result) {
				callback.onCompleted(new Gson().fromJson(result, JsonElement.class));
			};
		}.execute();
	}
	
	public String executeSync(HttpRequestBase request) {
		String data;
		HttpClient client = new DefaultHttpClient();
		HttpResponse response;
		try {
			response = client.execute(request);
			data = EntityUtils.toString(response.getEntity());
			return data;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
