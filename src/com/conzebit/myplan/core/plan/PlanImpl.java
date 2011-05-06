package com.conzebit.myplan.core.plan;

import java.util.ArrayList;

import com.conzebit.myplan.core.Chargeable;

public class PlanImpl implements IPlan {

	String operator;
	
	String planName;
	
	public PlanImpl(String operator, String planName) {
		this.operator = operator;
		this.planName = planName;
	}
	
	
	public String getCountryISO() {
		return "";
	}
	
	public String getCountry() {
		return "";
	}
	
	public String getOperator() {
		return this.operator;
	}
	
	public String getCurrency() {
		return "";
	}
	
	public String getPlanName() {
		return this.planName;
	}

	public String getScreenPlanName() {
		return this.getPlanName();
	}
	
	public String getPlanURL() {
		return null;
	}
	
	public PlanSummary process(ArrayList<Chargeable> data) {
		return null;
	}

	public PlanConfig getPlanConfig() {
		return null;
	}
}
