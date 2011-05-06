package com.conzebit.util;

public class StringUtil {

	
	public static boolean isEmpty(final String string) {
		return (string == null || "".equals(string));
	}
	
	public static boolean isNotEmpty(final String string) {
		return !isEmpty(string);
	}
}
