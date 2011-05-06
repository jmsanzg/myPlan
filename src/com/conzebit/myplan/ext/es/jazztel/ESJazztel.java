package com.conzebit.myplan.ext.es.jazztel;

import com.conzebit.myplan.core.msisdn.MsisdnType;
import com.conzebit.myplan.ext.es.ESPlan;

public abstract class ESJazztel extends ESPlan {

	public String getOperator() {
		return MsisdnType.ES_JAZZTEL.toString();
	}
}
