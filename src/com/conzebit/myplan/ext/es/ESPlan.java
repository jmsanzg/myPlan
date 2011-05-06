package com.conzebit.myplan.ext.es;

import com.conzebit.myplan.core.plan.IPlan;
import com.conzebit.myplan.core.plan.PlanConfig;

public abstract class ESPlan implements IPlan {

	public String getCountryISO() {
		return "ES";
	}
	
	public String getCountry() {
		return "España";
	}
	
	public String getCurrency() {
		return "EUR";
	}
	
	public PlanConfig getPlanConfig() {
		return null;
	}
	
	public String getScreenPlanName() {
		return getPlanName();
	}
	
	public boolean hasInternetFee() {
		return false;
	}
}