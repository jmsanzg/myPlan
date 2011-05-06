package com.conzebit.myplan.core.plan;

import java.util.ArrayList;

import com.conzebit.myplan.core.Chargeable;

public interface IPlan {
	
	public String getCountryISO();
	
	public String getCountry();
	
	public String getOperator();
	
	public String getCurrency();
	
	public String getPlanName();
	
	public String getScreenPlanName();
	
	public String getPlanURL();
	
	public PlanSummary process(ArrayList<Chargeable> data);
	
	public PlanConfig getPlanConfig();
	
	public boolean hasInternetFee();
}
