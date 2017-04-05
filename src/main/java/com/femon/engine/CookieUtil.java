/**
 * hi CookieUtil.java hi.cookie
 */
package com.femon.engine;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpResponse;

import com.femon.config.Constant;

/**
 * @author 一剑 2015年12月21日 下午3:52:00
 */
public class CookieUtil {

	public static HttpResponse loginResponse(String loginUrl, Map<String, String> params, String cookie) {
		return HttpInvoker.postReturnResponse(loginUrl, params, cookie);
	}

	/**
	 * @param loginUserName
	 * @param loginPassword
	 * @return
	 */
	public static String getLoginCookie(String loginUserName, String loginPassword, String loginUrl) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginMedium", loginUserName);
		params.put("loginPwd", loginPassword);
		String cookie = "";
		HttpResponse httpResponse = CookieUtil.loginResponse(loginUrl, params, cookie);
		Header head = httpResponse.getFirstHeader("Set-Cookie");
		if(head == null)
			return cookie;
		String loginCookie = head.getValue();
		//System.out.println("[loginCookie] : " + loginCookie);
		return loginCookie;
	}

}
