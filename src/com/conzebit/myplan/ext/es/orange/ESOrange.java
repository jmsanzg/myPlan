package com.conzebit.myplan.ext.es.orange;

import com.conzebit.myplan.core.msisdn.MsisdnType;
import com.conzebit.myplan.ext.es.ESPlan;

public abstract class ESOrange extends ESPlan {

	public String getOperator() {
		return MsisdnType.ES_ORANGE.toString();
	}
}
