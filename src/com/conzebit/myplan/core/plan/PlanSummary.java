package com.conzebit.myplan.core.plan;

import java.util.ArrayList;

public class PlanSummary implements Comparable<PlanSummary> {
	
	private double total = 0;
	private IPlan plan = null;
	
	private ArrayList<PlanChargeable> planCalls;
	
	public PlanSummary(IPlan plan) {
		this.plan = plan;
		this.planCalls = new ArrayList<PlanChargeable>();
	}
	
	public IPlan getPlan() {
		return this.plan;
	}

	public void addPlanCall(PlanChargeable planCall) {
		this.total += planCall.getPrice();
		this.planCalls.add(planCall);
	}
	
	public ArrayList<PlanChargeable> getPlanCalls() {
		return this.planCalls;
	}

	public double getTotalPrice() {
		return this.total;
	}
	
	public int compareTo(PlanSummary o) {
		return this.plan.getPlanName().compareToIgnoreCase(o.plan.getPlanName());
	}
}
