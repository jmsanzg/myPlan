/*
 * This file is part of myPlan.
 *
 * Plan is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * Plan is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */
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
