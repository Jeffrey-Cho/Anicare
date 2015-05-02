package sep.software.anicare.service;

import org.apache.http.client.methods.HttpRequestBase;

import sep.software.anicare.interfaces.JsonCallback;


interface RequestTask {
	public void executeAsync(final HttpRequestBase request, final JsonCallback callback);
}
