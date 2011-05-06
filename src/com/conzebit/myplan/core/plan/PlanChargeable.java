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

import com.conzebit.myplan.core.Chargeable;

public class PlanChargeable {

	private Chargeable chargeable;
	private double price;
	private String currency;
	
	public PlanChargeable(Chargeable chargeable, double price, String currency) {
		this.chargeable = chargeable;
		this.price = price;
		this.currency = currency;
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
	
}
