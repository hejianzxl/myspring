package com.july.beans.utils;

public class StringUtils {
	
	/**
	 * 非空
	 * @param s
	 * @return
	 */
	public static boolean isEmpty(String s) {
		return null == s || "".equals(s);
	}
	
	
	public static boolean isNotEmpty(String s) {
		return null != s && !"".equals(s);
	}
}
