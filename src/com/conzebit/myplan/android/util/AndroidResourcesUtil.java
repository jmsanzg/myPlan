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
