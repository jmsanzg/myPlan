/*
 * This file is part of myPlan.
 *
 * Plan is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * Plan is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with myPlan.  If not, see <http://www.gnu.org/licenses/>.
 */
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
