package sep.software.anicare.service;

import org.apache.http.client.methods.HttpRequestBase;

import sep.software.anicare.callback.JsonCallback;


interface RequestTask {
	public void executeAsync(final HttpRequestBase request, final JsonCallback callback);
}
