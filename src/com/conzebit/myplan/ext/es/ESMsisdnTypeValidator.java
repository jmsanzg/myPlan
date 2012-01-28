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
package com.conzebit.myplan.ext.es;

import java.util.HashSet;

import com.conzebit.myplan.core.msisdn.IMsisdnTypeValidator;
import com.conzebit.myplan.core.msisdn.MsisdnType;


@SuppressWarnings("serial")
public class ESMsisdnTypeValidator implements IMsisdnTypeValidator {
	
	private HashSet<String> specialMsisdn = new HashSet<String>() {{
		this.add("091"); // police
		this.add("112"); // european emergency number
		this.add("065"); // ambulance
	}};

	private HashSet<String> specialZeroMsisdn = new HashSet<String>() {{
		this.add("1565"); // jazztel customer care
		this.add("669"); // movistar customer care
		this.add("470"); // orange customer care
		this.add("1470"); // orange customer care
		this.add("222"); // orange customer care
		this.add("1222"); // orange customer care
		this.add("1414"); // orange customer care
		this.add("1212"); // pepephone customer care
		this.add("121"); // simyo customer care
		this.add("123"); // vodafone customer care personal
		this.add("122"); // vodafone customer care business
		this.add("1443"); // vodafone customer care business
		this.add("607123000"); // vodafone customer care (equals to 122)
		this.add("622"); // yoigo customer care
	}};

    public String getCountryCode() {
	    return "ES";
    }

    public MsisdnType getMsisdnType(String msisdn) {
    	if (msisdn == null) {
    	    return MsisdnType.UNKNOWN;
    	}
	    
	    if ((msisdn.startsWith("+") && !msisdn.startsWith("+34")) ||
	        (msisdn.startsWith("00") && !msisdn.startsWith("0034"))) {
	    	return MsisdnType.ES_INTERNATIONAL;
	    }

	    if (specialZeroMsisdn.contains(msisdn)) {
	    	return MsisdnType.ES_SPECIAL_ZER0;
	    }
	    
	    if (specialMsisdn.contains(msisdn)) {
	    	return MsisdnType.ES_SPECIAL;
	    }
	    

	    if (msisdn.startsWith("+34")) {
	    	msisdn = msisdn.substring(3);
	    } else if (msisdn.startsWith("0034")) {
	    	msisdn = msisdn.substring(4);
	    }

	    // 801, 802, ... -> Special
	    if (msisdn.startsWith("80")) {
	    	return MsisdnType.ES_LAND_LINE_SPECIAL;
	    }

	    // 900 --> free
	    if (msisdn.startsWith("900")) {
	    	return MsisdnType.ES_SPECIAL_ZER0;
	    }
	    
	    // 901, 902, ... -> Special
	    if (msisdn.startsWith("90")) {
	    	return MsisdnType.ES_LAND_LINE_SPECIAL;
	    }

	    // 91, 92 -> National
	    if (msisdn.startsWith("9")) {
	    	return MsisdnType.ES_LAND_LINE;
	    }
	    
	    return MsisdnType.UNKNOWN;
    }
}
