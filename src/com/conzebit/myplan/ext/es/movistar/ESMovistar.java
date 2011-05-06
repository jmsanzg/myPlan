package com.conzebit.myplan.ext.es.movistar;

import com.conzebit.myplan.core.msisdn.MsisdnType;
import com.conzebit.myplan.ext.es.ESPlan;

public abstract class ESMovistar extends ESPlan {

	public String getOperator() {
		return MsisdnType.ES_MOVISTAR.toString();
	}

}
