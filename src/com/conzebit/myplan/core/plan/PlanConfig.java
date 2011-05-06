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
