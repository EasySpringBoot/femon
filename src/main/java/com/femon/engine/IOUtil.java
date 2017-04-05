/**
 * hi IOUtil.java hi.io
 */
package com.femon.engine;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author 一剑 2015年12月21日 下午3:19:12
 */
public class IOUtil {
	public static String inputStream2String(InputStream in) throws IOException {
		StringBuffer out = new StringBuffer();
		byte[] b = new byte[4096];
		for (int n; (n = in.read(b)) != -1;) {
			out.append(new String(b, 0, n));
		}
		return out.toString();
	}
}
