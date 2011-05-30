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
package com.conzebit.myplan.core.message;

import com.conzebit.myplan.core.Chargeable;

public class ChargeableMessage extends Chargeable {

	public final static String MESSAGE_MONTH_FEE = "plan_month_fee";
	public final static String MESSAGE_MINIMUM_MONTH_FEE = "plan_minimum_month_fee";
	
	private String message;
	
	public ChargeableMessage(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}

	@Override
    public int getChargeableType() {
	    return CHARGEABLE_TYPE_MESSAGE;
    }

}
