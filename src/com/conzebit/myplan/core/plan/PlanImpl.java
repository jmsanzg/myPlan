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
 * along with myPlan.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.conzebit.myplan.core.plan;

import java.util.ArrayList;

import com.conzebit.myplan.core.Chargeable;

public class PlanImpl extends AbstractPlan {

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

	public boolean hasInternetFee() {
	    return false;
    }
}
