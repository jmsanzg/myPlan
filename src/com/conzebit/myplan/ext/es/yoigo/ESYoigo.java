package com.conzebit.myplan.ext.es.yoigo;

import com.conzebit.myplan.core.msisdn.MsisdnType;
import com.conzebit.myplan.ext.es.ESPlan;

public abstract class ESYoigo extends ESPlan {

	public String getOperator() {
		return MsisdnType.ES_YOIGO.toString();
	}
}
