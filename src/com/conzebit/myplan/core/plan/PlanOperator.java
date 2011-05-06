package com.conzebit.myplan.core.plan;

import java.util.Collection;
import java.util.HashMap;

public class PlanOperator implements Comparable<PlanOperator> {

	private String name;
	
	private HashMap<String, PlanSummary> planSummaries;
	
	public PlanOperator(String name) {
		this.name = name;
		this.planSummaries = new HashMap<String, PlanSummary>();
	}
	
	public String getName() {
		return this.name;
	}
	
	public void addPlanSummary(PlanSummary summary) {
		this.planSummaries.put(summary.getPlan().getPlanName(), summary);
	}
	
	public PlanSummary getPlanSummary(String name) {
		if (!this.planSummaries.containsKey(name)) {
			this.planSummaries.put(name, new PlanSummary(new PlanImpl(this.name, name)));
		}
		return this.planSummaries.get(name);
	}
	
	public Collection<PlanSummary> getPlanSummaryList() {
		return this.planSummaries.values();
	}

	public int compareTo(PlanOperator o) {
		return this.getName().compareToIgnoreCase(o.getName());
	}
}
