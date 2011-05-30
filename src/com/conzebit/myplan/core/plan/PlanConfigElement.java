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

public class PlanConfigElement<T> {

	private String id;
	private T value;
	private T minValue;
	private T maxValue;
	private Class<T> clazz;
	
	public PlanConfigElement(String id, T value, T minValue, T maxValue, Class<T> clazz) {
		this.id = id;
		this.value = value;
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.clazz = clazz;
	}
	
	public String getId() {
		return this.id;
	}
	
	public T getValue() {
		return this.value;
	}
	
	public void setValue(T value) {
		this.value = value;
	}

	public T getMinValue() {
		return this.minValue;
	}
	
	public T getMaxValue() {
		return this.maxValue;
	}
	
	public String getTName() {
		return this.clazz.getName();
	}
}