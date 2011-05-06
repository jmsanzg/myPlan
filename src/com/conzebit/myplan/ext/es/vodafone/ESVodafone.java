package com.conzebit.myplan.ext.es.vodafone;

import com.conzebit.myplan.core.msisdn.MsisdnType;
import com.conzebit.myplan.ext.es.ESPlan;

public abstract class ESVodafone extends ESPlan {

	public String getOperator() {
		return MsisdnType.ES_VODAFONE.toString();
	}
}
