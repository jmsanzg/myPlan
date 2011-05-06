package com.conzebit.myplan.ext.es.simyo;

import com.conzebit.myplan.core.msisdn.MsisdnType;
import com.conzebit.myplan.ext.es.ESPlan;

public abstract class ESSimyo extends ESPlan {

	public String getOperator() {
		return MsisdnType.ES_SIMYO.toString();
	}
}
