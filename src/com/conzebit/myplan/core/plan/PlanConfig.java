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

public class PlanConfig {

	@SuppressWarnings("unchecked")
    private HashMap<String, PlanConfigElement> data;
	
	@SuppressWarnings("unchecked")
    public PlanConfig() {
		this.data = new HashMap<String, PlanConfigElement>();
	}
	
	@SuppressWarnings("unchecked")
    public void addPlanConfigElement(PlanConfigElement planConfigElement) {
		this.data.put(planConfigElement.getId(), planConfigElement);
	}
	
	@SuppressWarnings("unchecked")
    public PlanConfigElement getPlanConfigElement(String key) {
		return this.data.get(key);
	}
	
	@SuppressWarnings("unchecked")
    public Collection<PlanConfigElement> getPlanConfigElements() {
		return this.data.values();
	}

	@SuppressWarnings("unchecked")
    public void setUserConfig(HashMap<String, Object> allConfig) {
		for (PlanConfigElement pce : this.data.values()) {
			if (allConfig.containsKey(pce.getId())) {
				Object value = allConfig.get(pce.getId());
				String sValue = value.toString();
				pce.setValue(Integer.valueOf(sValue)); // TODO no tiene por qué ser Integer				
			}
		}
    }
}
