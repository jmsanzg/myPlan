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

import com.conzebit.myplan.core.Chargeable;

public class PlanChargeable {

	public enum Type {
		INSIDE_PLAN, OUTSIDE_PLAN, ZERO, MONTH_FEE, MINIMUM_MONTH_FEE;
	}
	
	private Chargeable chargeable;
	private double price;
	private String currency;
	private Type type = null; 

	public PlanChargeable(Chargeable chargeable, double price, String currency) {
		this(chargeable, price, currency, null);
	}

	public PlanChargeable(Chargeable chargeable, double price, String currency, Type type) {
		this.chargeable = chargeable;
		this.price = price;
		this.currency = currency;
		this.type = type;
	}
	
	public Chargeable getChargeable() {
		return this.chargeable;
	}
	
	public double getPrice() {
		return this.price;
	}
	
	public String getCurrency() {
		return this.currency;
	}
	
	public Type getType() {
		return this.type;
	}
	
}
