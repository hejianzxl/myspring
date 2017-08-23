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


	public static boolean hasText(String text) {
		CharSequence str = (CharSequence) text;
		if (!hasLength(str)) {
			return false;
		}
		int strLen = str.length();
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	private static boolean hasLength(CharSequence str) {
		return (str != null && str.length() > 0);
	}


	public static String trimAllWhitespace(String str) {
		if (!hasLength(str)) {
			return str;
		}
		int len = str.length();
		StringBuilder sb = new StringBuilder(str.length());
		for (int i = 0; i < len; i++) {
			char c = str.charAt(i);
			if (!Character.isWhitespace(c)) {
				sb.append(c);
			}
		}
		return sb.toString();
	}


	public static String[] commaDelimitedListToStringArray(String locations) {
		return delimitedListToStringArray(locations, ",");
	}
	
	public static String[] delimitedListToStringArray(String s, String delimiter) {
		if(isEmpty(s)) {
			return null;
		}
		
		if(isNotEmpty(s) && isEmpty(delimiter)) {
			return new String[]{s};
		}
		
		
		return null;
	}

	/**
	 * Convenience method to return a String array as a delimited (e.g. CSV)
	 * String. Useful for toString() implementations
	 * @param arr array to display. Elements may be of any type (toString() will be
	 * called on each element).
	 * @param delim delimiter to use (probably a ,)
	 * @param parentLocations
	 * @param separator
	 * @return
	 */
	public static String arrayToDelimitedString(String[] parentLocations, String separator) {
		StringBuilder stringBuilder = new StringBuilder();
		if(null != parentLocations && parentLocations.length > 0){
			for(String location : parentLocations) {
				stringBuilder.append(location).append(separator);
			}
		}
		return stringBuilder.toString();
	}
}
