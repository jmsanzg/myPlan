package com.conzebit.util;

import java.util.HashMap;

@SuppressWarnings("serial")
public class MsisdnUtil {

	private static HashMap<String, String> countryToISO = new HashMap<String, String>() {{
		put("+34", "ES");
	}};
	
	public static String getCountryCodeFromMsisdn(String msisdn, String defaultValue) {
		if (msisdn == null) {
			return defaultValue;
		}
		
		if (msisdn.startsWith("00")) {
			msisdn = "+" + msisdn.substring(2);
		}
		
		// I know, I know, this is WRONG...
		for (String countryCode : countryToISO.keySet()) {
			if (msisdn.startsWith(countryCode)) {
				return countryToISO.get(countryCode);
			}
		}
		return defaultValue;
	}
	
}
