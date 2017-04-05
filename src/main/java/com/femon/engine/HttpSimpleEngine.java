/**
 * hi HttpSimpleEngine.java hi.http.invoke
 */
package com.femon.engine;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

/**
 * @author 一剑 2015年12月21日 下午5:01:26
 */
public class HttpSimpleEngine {

	/**
	 * 带cookie GET请求api
	 * 
	 * @param url
	 * @param cookie
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String getWithCookieReturnBody(String url, String cookie) {
		HttpResponse httpResponse = HttpSimpleEngine.getWithCookie(url, cookie);
		InputStream ins = null;
		try {
			ins = httpResponse.getEntity().getContent();
		} catch (IllegalStateException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		String body = null;
		try {
			body = IOUtil.inputStream2String(ins);
		} catch (IOException e) {

			e.printStackTrace();
		}
		return body;
	}

	/**
	 * 带Cookie POST请求api
	 * 
	 * @param url
	 * @param cookie
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */

	public static String postWithCookieReturnBody(String url, String cookie) {
		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader("Cookie", cookie);
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpResponse httpResponse = null;
		try {
			httpResponse = httpclient.execute(httpPost);
		} catch (ClientProtocolException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		InputStream ins = null;
		try {
			ins = httpResponse.getEntity().getContent();
		} catch (IllegalStateException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		String body = null;
		try {
			body = IOUtil.inputStream2String(ins);
		} catch (IOException e) {

			e.printStackTrace();
		}
		return body;
	}

	public static HttpResponse loginResponse(String loginUrl, Map<String, String> params, String cookie) {
		return HttpInvoker.postReturnResponse(loginUrl, params, cookie);
	}

	public static HttpResponse get(String url) {
		HttpGet httpGet = new HttpGet(url);
		DefaultHttpClient client = new DefaultHttpClient();
		try {
			return client.execute(httpGet);
		} catch (ClientProtocolException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param api
	 * @return
	 */
	public static String getReturnBody(String api) {
		HttpResponse httpResponse = null;
		String body = null;
		try {
			httpResponse = get(api);
			InputStream ins = httpResponse.getEntity().getContent();
			body = IOUtil.inputStream2String(ins);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return body;
	}

	public static HttpResponse getWithCookie(String url, String cookie) {
		HttpGet httpGet = new HttpGet(url);
		httpGet.addHeader("Cookie", cookie);
		DefaultHttpClient httpclient = new DefaultHttpClient();
		try {
			return httpclient.execute(httpGet);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static HttpResponse post(String url, Map<String, String> params, CookieStore cookieStore) {

		DefaultHttpClient httpclient = new DefaultHttpClient();
		httpclient.setCookieStore(cookieStore);

		HttpPost post = postForm(url, params);
		HttpResponse response = sendRequest(httpclient, post);
		httpclient.getConnectionManager().shutdown();
		return response;
	}

	/**
	 * @param api
	 * @return
	 */
	public static String postReturnBody(String api) {
		HttpPost httpPost = new HttpPost(api);
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpResponse httpResponse;
		String body = null;
		try {
			httpResponse = httpclient.execute(httpPost);
			InputStream ins = httpResponse.getEntity().getContent();
			body = IOUtil.inputStream2String(ins);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return body;
	}

	private static HttpPost postForm(String url, Map<String, String> params) {

		HttpPost httpost = new HttpPost(url);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();

		Set<String> keySet = params.keySet();
		for (String key : keySet) {
			nvps.add(new BasicNameValuePair(key, params.get(key)));
		}

		try {
			httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return httpost;
	}

	private static HttpResponse sendRequest(DefaultHttpClient httpclient, HttpUriRequest httpost) {
		HttpResponse response = null;
		try {
			response = httpclient.execute(httpost);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		httpclient.getConnectionManager().shutdown();
		return response;
	}

}
