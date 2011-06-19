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
import java.util.HashMap;
import java.util.Map;

import com.conzebit.myplan.core.Chargeable;
import com.conzebit.myplan.core.bandwidth.Bandwidth;
import com.conzebit.myplan.core.call.Call;
import com.conzebit.myplan.core.message.ChargeableMessage;
import com.conzebit.myplan.core.msisdn.MsisdnType;
import com.conzebit.myplan.core.sms.Sms;

public abstract class AbstractPlan {
	
	public abstract String getCountryISO();
	
	public abstract String getCountry();
	
	public abstract String getOperator();
	
	public abstract String getCurrency();
	
	public abstract String getPlanName();
	
	public abstract String getScreenPlanName();
	
	public abstract String getPlanURL();
	
	public Double processCall(Call call, Map<String, Object> accumulatedData) {
		return null;
	}
	
	public Double processSms(Sms sms, Map<String, Object> accumulatedData) {
		return null;
	}
	
	public Double processBandwidth(Bandwidth bandwidth, Map<String, Object> accumulatedData) {
		return null;
	}
	
	public Double getMonthFee() {
		return null;
	}
	
	public Double getMinimumMonthFee() {
		return null;
	}

	public abstract PlanConfig getPlanConfig();
	
	public abstract boolean hasInternetFee();
	
	public PlanSummary process(ArrayList<Chargeable> data) {
		PlanSummary ret = new PlanSummary(this);
		
		Double monthFee = this.getMonthFee();
		if (monthFee != null) {
			ret.addPlanCall(new PlanChargeable(new ChargeableMessage(ChargeableMessage.MESSAGE_MONTH_FEE), monthFee, this.getCurrency()));
		}
		
		double globalPrice = 0;
		Map<String, Object> accumulatedData = new HashMap<String, Object>();

		for (Chargeable chargeable : data) {
			Double price = null;
			if (chargeable.getChargeableType() == Chargeable.CHARGEABLE_TYPE_CALL) {
				Call call = (Call) chargeable;
				if (call.getContact().getMsisdnType() == MsisdnType.ES_SPECIAL_ZER0) {
					price = 0.0;
				} else {
					price = this.processCall(call, accumulatedData);
				}
			} else if (chargeable.getChargeableType() == Chargeable.CHARGEABLE_TYPE_SMS) {
				Sms sms = (Sms) chargeable;
				price = this.processSms(sms, accumulatedData);
			} else if (chargeable.getChargeableType() == Chargeable.CHARGEABLE_TYPE_BANDWIDTH) {
				Bandwidth bandwidth = (Bandwidth) chargeable;
				price = this.processBandwidth(bandwidth, accumulatedData);
			}
			if (price != null) {
				globalPrice += price;
				ret.addPlanCall(new PlanChargeable(chargeable, price, this.getCurrency()));
			}
		}
		
		Double minimumMonthFee = this.getMinimumMonthFee();
		if (minimumMonthFee != null && globalPrice < minimumMonthFee) {
			ret.addPlanCall(new PlanChargeable(new ChargeableMessage(ChargeableMessage.MESSAGE_MINIMUM_MONTH_FEE), minimumMonthFee - globalPrice, this.getCurrency()));
		}
		return ret;
	}
}
