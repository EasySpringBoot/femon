/**
 * hi HttpInvoker.java hi.http.invoke
 */
package com.femon.engine;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.UserTokenHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.testng.log4testng.Logger;

/**
 * @author 一剑 2015年12月20日 下午7:39:17
 */
public class HttpInvoker {

	private static Logger log = Logger.getLogger(HttpInvoker.class);

	/**
	 * 根据登录Cookie获取资源 一切异常均未处理，需要酌情检查异常
	 * 
	 * @throws Exception
	 */
	public static CookieStore getLoginCookies(String urlLogin, String username, String password) throws Exception {

		DefaultHttpClient client = new DefaultHttpClient(new PoolingClientConnectionManager());

		/**
		 * 第一次请求登录页面 获得cookie 相当于在登录页面点击登录，此处在URL中 构造参数，
		 * 如果参数列表相当多的话可以使用HttpClient的方式构造参数 此处不赘述
		 */
		HttpPost post = new HttpPost(urlLogin);
		HttpResponse response = client.execute(post);
		HttpEntity entity = response.getEntity();
		return client.getCookieStore();

	}

	/**
	 * post請求，參數Map
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws UnsupportedOperationException
	 * @throws IOException
	 */
	public static String postReturnBody(String url, Map<String, String> params, String cookie)
			throws UnsupportedOperationException, IOException {
		String body = null;
		DefaultHttpClient httpclient = new DefaultHttpClient();
		//log.info("create http post:" + url);
		HttpPost post = postForm(url, params, cookie);
		HttpResponse response = sendRequest(httpclient, post);
		InputStream InputStream = response.getEntity().getContent();
		String responseBody = IOUtil.inputStream2String(InputStream);
		return responseBody;
	}

	public static HttpResponse postReturnResponse(String url, Map<String, String> params, String cookie) {

		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpPost post = postForm(url, params, cookie);
		HttpResponse response = sendRequest(httpclient, post);
		httpclient.getConnectionManager().shutdown();
		return response;
	}

	/**
	 * 帶參數& Cookie的接口Post執行引擎
	 * 
	 * @param url
	 * @param params
	 * @param cookie
	 * @return
	 */
	public static String postWithParamsAndCookieReturnBody(String url, Map<String, String> params, String cookie) {
		HttpResponse httpResponse = postReturnResponse(url, params, cookie);
		InputStream ins;
		String body = "";
		try {
			ins = httpResponse.getEntity().getContent();
			body = IOUtil.inputStream2String(ins);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return body;
	}

	public static HttpResponse postWithHeadersReturnBody(String url, Header[] headers) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpPost httpost = new HttpPost(url);
		if (null != headers) {
			for (Header header : headers) {
				httpost.addHeader(header);
			}
		}
		HttpResponse response = sendRequest(httpclient, httpost);
		httpclient.getConnectionManager().shutdown();
		return response;
	}

	public static String get(String url) {
		String body = null;
		DefaultHttpClient httpclient = new DefaultHttpClient();
		//log.info("create http post:" + url);
		HttpGet get = new HttpGet(url);
		body = invoke(httpclient, get);

		httpclient.getConnectionManager().shutdown();
		return body;
	}

	public static String get(String url, CookieStore cookieStore) {
		String body = null;
		DefaultHttpClient httpclient = new DefaultHttpClient();
		httpclient.setCookieStore(cookieStore);

		httpclient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);

		//log.info("create http post:" + url);
		HttpGet get = new HttpGet(url);
		body = invoke(httpclient, get);
		httpclient.getConnectionManager().shutdown();
		return body;
	}

	public static String get(String url, String cookie) {
		String body = null;
		DefaultHttpClient httpclient = new DefaultHttpClient();
		UserTokenHandler handler = new UserTokenHandler() {
			public Object getUserToken(HttpContext HttpContext) {
				return HttpContext.getAttribute("access_token");
			}
		};
		httpclient.setUserTokenHandler(handler);

		HttpGet get = new HttpGet(url);
		body = invoke(httpclient, get);
		httpclient.getConnectionManager().shutdown();
		return body;
	}

	private static String invoke(DefaultHttpClient httpclient, HttpUriRequest httpost) {
		HttpResponse response = sendRequest(httpclient, httpost);
		String body = paseResponse(response);
		httpclient.getConnectionManager().shutdown();
		return body;
	}

	private static String paseResponse(HttpResponse response) {
		//log.info("get response from http server..");
		HttpEntity entity = response.getEntity();
		//log.info("response status: " + response.getStatusLine());
		String charset = EntityUtils.getContentCharSet(entity);
		log.info(charset);

		String body = null;
		try {
			body = EntityUtils.toString(entity);
			log.info(body);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return body;
	}

	private static HttpResponse sendRequest(DefaultHttpClient httpclient, HttpUriRequest httpost) {
		//log.info("execute post...");
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

	private static HttpPost postForm(String url, Map<String, String> params, String cookie) {

		HttpPost httpost = new HttpPost(url);
		httpost.addHeader("Cookie", cookie);
		httpost.addHeader("Content-Type", "application/x-www-form-urlencoded");
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();

		Set<String> keySet = params.keySet();
		for (String key : keySet) {
			nvps.add(new BasicNameValuePair(key, params.get(key)));
		}

		try {
			//log.info("set utf-8 form entity to httppost");
			httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return httpost;
	}

	/**
	 * @param apiUrl
	 * @param headers
	 * @return
	 */
	public static HttpResponse getWithHeader(String apiUrl, Header[] headers) {
		HttpGet httpGet = new HttpGet(apiUrl);
		if (null != headers) {
			for (Header header : headers) {
				httpGet.addHeader(header);
			}
		}

		DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
		HttpResponse CloseableHttpResponse = null;
		try {
			CloseableHttpResponse = defaultHttpClient.execute(httpGet);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		defaultHttpClient.getConnectionManager().shutdown();
		return CloseableHttpResponse;
	}

}
