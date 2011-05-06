package com.conzebit.myplan.ext.es.pepephone;

import com.conzebit.myplan.core.msisdn.MsisdnType;
import com.conzebit.myplan.ext.es.ESPlan;

public abstract class ESPepePhone extends ESPlan {

	public String getOperator() {
		return MsisdnType.ES_PEPEPHONE.toString();
	}
}
