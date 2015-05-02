package sep.software.anicare.service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.message.BasicNameValuePair;

import sep.software.anicare.event.AniCareException;
import sep.software.anicare.callback.EntityCallback;
import sep.software.anicare.callback.JsonCallback;
import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;

import de.greenrobot.event.EventBus;

public class RestClient {
	
	public static enum TYPE {
		POST, GET;
	}
	
	private String mHost;
	private String mApiName;
	private TYPE mMethodType = TYPE.GET;
	List<NameValuePair> params = new ArrayList<NameValuePair>();
	private Class<?> mClazz;
	
	private RequestTask requsetTask;
	private static RestClient instance = null;
	private static String DEFAULT_HOST = "";
	private static RequestTask DEFAULT_REQUEST_TASK = new HttpRequestTask();
	
	private static RequestBuildStrategy GET_STRATEGY;
	private static RequestBuildStrategy POST_STRATEGY;
	private static Map<RestClient.TYPE, RequestBuildStrategy> methodStrategy = new HashMap<RestClient.TYPE, RestClient.RequestBuildStrategy>();
	
	static {
		GET_STRATEGY = new RequestBuildStrategy() {
			
			@Override
			public HttpRequestBase buildRequest(Uri.Builder uriBuilder, List<NameValuePair> params) {
				// TODO Auto-generated method stub
				if (params != null && params.size() > 0) {
					for (NameValuePair parameter : params) {
						uriBuilder.appendQueryParameter(parameter.getName(), parameter.getValue());
					}
				}
				return new HttpGet(uriBuilder.build().toString());
			}
		};
		
		POST_STRATEGY = new RequestBuildStrategy() {
			@Override
			public HttpRequestBase buildRequest(Uri.Builder uriBuilder, List<NameValuePair> params) {
				// TODO Auto-generated method stub
				HttpPost post = new HttpPost(uriBuilder.build().toString());
				if (params != null && params.size() > 0) {
					try {
			        	new UrlEncodedFormEntity(params);
						post.setEntity(new UrlEncodedFormEntity(params));
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				return post;
			}
		};
		
		methodStrategy.put(RestClient.TYPE.GET, GET_STRATEGY);
		methodStrategy.put(RestClient.TYPE.POST, POST_STRATEGY);
	}
	
	
	private RestClient() {
		
	}
	public static void init() {
		init(DEFAULT_HOST, DEFAULT_REQUEST_TASK);
	}
	public static void init(String host) {
		init(host, DEFAULT_REQUEST_TASK);
	}
	public static void init(String host, RequestTask requestTask) {
		instance = new RestClient();
		instance.mHost = host;
		instance.requsetTask = requestTask;
	}
	public static RestClient requestTo(String apiName) {
		if (instance == null) {
			init(DEFAULT_HOST, DEFAULT_REQUEST_TASK);
		}
		instance.mApiName = apiName;
		return instance;
	}
	public RestClient onMethod(TYPE type) {
		this.mMethodType = type;
		return this;
	}
	public <T> RestClient withParam(T obj) {
		this.params.add(new BasicNameValuePair(obj.getClass().getSimpleName(), new Gson().toJson(obj)));
		return this;
	}
	
	public <T> RestClient getAs(Class<T> clazz) {
		mClazz = clazz;
		return this;
	}
	
	public <E> void async(final EntityCallback<E> callback) {
		
		asyncInternal(new JsonCallback() {
			
			@Override
			public void onCompleted(JsonElement entity) {
				// TODO Auto-generated method stub
				try {
					callback.onCompleted((E)new Gson().fromJson(entity, mClazz.newInstance().getClass()));
				} catch (JsonSyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					EventBus.getDefault().post(new AniCareException(AniCareException.TYPE.INTERNAL_ERROR));
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					EventBus.getDefault().post(new AniCareException(AniCareException.TYPE.INTERNAL_ERROR));
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					EventBus.getDefault().post(new AniCareException(AniCareException.TYPE.INTERNAL_ERROR));
				}
				
			}
		});
	}
	
	public <E> void asyncList(final EntityCallback<List<E>> callback) {
		asyncInternal(new JsonCallback() {
			
			@Override
			public void onCompleted(JsonElement entity) {
				// TODO Auto-generated method stub
				try {
					JsonArray arr = entity.getAsJsonArray();
					List<E> list = new ArrayList<E>();
					for (int i = 0 ; i < arr.size() ; i++) {
						list.add((E)new Gson().fromJson(arr.get(i), mClazz.newInstance().getClass()));
					}
					callback.onCompleted(list);
				} catch (JsonSyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					EventBus.getDefault().post(new AniCareException(AniCareException.TYPE.INTERNAL_ERROR));
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					EventBus.getDefault().post(new AniCareException(AniCareException.TYPE.INTERNAL_ERROR));
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					EventBus.getDefault().post(new AniCareException(AniCareException.TYPE.INTERNAL_ERROR));
				}
				
			}
		});
	}
	
	private void asyncInternal(final JsonCallback callback) {
		
		if (mApiName == null || mApiName.trim().equals("")) {
			EventBus.getDefault().post(new AniCareException(AniCareException.TYPE.ILLEGAL_ARGUMENT, "mApiName cannot be null"));
			return;
		}
		
		if (mMethodType == null) {
			EventBus.getDefault().post(new AniCareException(AniCareException.TYPE.ILLEGAL_ARGUMENT, "mMethodType cannot be null"));
			return;
		}
		
		Uri.Builder uriBuilder = Uri.parse(mHost).buildUpon();
		uriBuilder.path(mApiName);
		HttpRequestBase request = methodStrategy.get(mMethodType).buildRequest(uriBuilder, params);
		
		requsetTask.executeAsync(request, callback);
	}
	
	private interface RequestBuildStrategy {
		public HttpRequestBase buildRequest(Uri.Builder uriBuilder, List<NameValuePair> params);
	}
}
