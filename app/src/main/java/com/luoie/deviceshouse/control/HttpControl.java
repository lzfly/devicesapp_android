package com.luoie.deviceshouse.control;

import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.luoie.deviceshouse.utils.ParamsUtils;

import android.util.Log;

public class HttpControl {

	public HttpClient httpClient;
	
	public HttpControl() {
		HttpParams httpParams = new BasicHttpParams();
		//HttpConnectionParams.setConnectionTimeout(httpParams, ParamsUtils.CONNECTION_TIMEOUT);
		//HttpConnectionParams.setSoTimeout(httpParams, 10*1000); 
		//HttpConnectionParams.setSocketBufferSize(httpParams, 8192);  
		httpClient = new DefaultHttpClient(httpParams);
		httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,5000);
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,5000);//数据传输时间
	}

	public HttpClient getHttpClient(){
		return httpClient;
	}

}
