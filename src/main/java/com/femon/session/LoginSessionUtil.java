/**
 * wai LoginSessionUtil.java com.wai.session
 */
package com.femon.session;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpResponse;

import com.femon.engine.HttpSimpleEngine;

/**
 * @author 一剑 2015年12月28日 下午4:45:53
 */
public class LoginSessionUtil {

	public static String getH5LoginCookie(String url, String username, String password) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginMedium", username);
		params.put("loginPwd", password);
		HttpResponse httpResponse = HttpSimpleEngine.loginResponse(url, params, "");
		Header head = httpResponse.getFirstHeader("Set-Cookie");
		return head.getValue();
	}

	public static String getWebLoginCookie(String url, String username, String password) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginMedium", username);
		params.put("loginPwd", password);
		HttpResponse httpResponse = HttpSimpleEngine.loginResponse(url, params, "");
		Header head = httpResponse.getFirstHeader("Set-Cookie");
		return head.getValue();
	}

	public static String getAppLoginCookie(String url, String username, String password) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginMedium", username);
		params.put("loginPwd", password);
		HttpResponse httpResponse = HttpSimpleEngine.loginResponse(url, params, "");
		Header head = httpResponse.getFirstHeader("Set-Cookie");
		return head.getValue();
	}

}
