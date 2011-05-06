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