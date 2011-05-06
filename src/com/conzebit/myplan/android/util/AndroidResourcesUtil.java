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
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.conzebit.myplan.android.util;

import com.conzebit.myplan.R;
import com.conzebit.myplan.core.msisdn.MsisdnType;

public class AndroidResourcesUtil {

	public static int getMsisdnTypeResourceImage(MsisdnType msisdnType) {
		switch (msisdnType) {
			case ES_JAZZTEL:
				return R.drawable.logo_jazztel;
			case ES_MOVISTAR:
				return R.drawable.logo_movistar;
			case ES_ORANGE:
				return R.drawable.logo_orange;
			case ES_PEPEPHONE:
				return R.drawable.logo_pepephone;
			case ES_SIMYO:
				return R.drawable.logo_simyo;
			case ES_VODAFONE:
				return R.drawable.logo_vodafone;
			case ES_YOIGO:
				return R.drawable.logo_yoigo;
			case ES_LAND_LINE:
				return R.drawable.logo_landline;
			case ES_LAND_LINE_SPECIAL:
				return R.drawable.logo_landlinespecial;
		}
		return R.drawable.logo_unknown;
	}
	
	public static int getOperatorResourceImage(String operatorName) {
		return getMsisdnTypeResourceImage(MsisdnType.fromString(operatorName));
	}
	
}
