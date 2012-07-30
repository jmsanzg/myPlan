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
package com.conzebit.myplan.ext.es.amena.particulares;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.conzebit.myplan.core.Chargeable;
import com.conzebit.myplan.core.call.Call;
import com.conzebit.myplan.core.message.ChargeableMessage;
import com.conzebit.myplan.core.msisdn.MsisdnType;
import com.conzebit.myplan.core.plan.PlanChargeable;
import com.conzebit.myplan.core.plan.PlanChargeable.Type;
import com.conzebit.myplan.core.plan.PlanSummary;
import com.conzebit.myplan.core.sms.Sms;
import com.conzebit.myplan.ext.es.amena.ESAmena;


/**
 * Tarifa Amena Sin Datos
 * @author aalmenar
 */
public class ESAmenaSinDatos extends ESAmena {
    
	private double monthFee = 19;
	private double initialPrice = 0.15;
	private double pricePerSecond = 0.08 / 60;
	private double smsPrice = 0.15;
	private int maxDistinctNumbersCalls = 150;
    private int maxDistinctNumbersSMS = 100;

	public String getPlanName() {
		return "Amena Sin Datos";
	}
	
	public String getPlanURL() {
		return "http://www.amena.com/tarifas";
	}
	
	public PlanSummary process(ArrayList<Chargeable> data) {
		PlanSummary ret = new PlanSummary(this);
		ret.addPlanCall(new PlanChargeable(new ChargeableMessage(ChargeableMessage.MESSAGE_MONTH_FEE), monthFee, this.getCurrency(), Type.MONTH_FEE));

		Set<String> msisdns = new HashSet<String>();
		for (Chargeable chargeable : data) {
			if (chargeable.getChargeableType() == Chargeable.CHARGEABLE_TYPE_CALL) {
				Call call = (Call) chargeable;
			
				if (call.getType() != Call.CALL_TYPE_SENT) {
					continue;
				}
				
				double callPrice = 0;
				Type type = null;
				if (call.getContact().getMsisdnType() == MsisdnType.ES_SPECIAL_ZER0) {
					callPrice = 0;
					type = Type.ZERO;
				} else {
					msisdns.add(call.getContact().getMsisdn());
					boolean insidePlan = msisdns.size() <= maxDistinctNumbersCalls;
					if (insidePlan) {
						type = Type.INSIDE_PLAN;
					} else {
						callPrice += initialPrice + (call.getDuration() * pricePerSecond);
						type = Type.OUTSIDE_PLAN;
					}
				}
				ret.addPlanCall(new PlanChargeable(call, callPrice, this.getCurrency(), type));
			} else if (chargeable.getChargeableType() == Chargeable.CHARGEABLE_TYPE_SMS) {
				Sms sms = (Sms) chargeable;
				if (sms.getType() == Sms.SMS_TYPE_RECEIVED) {
					continue;
				}
				ret.addPlanCall(new PlanChargeable(chargeable, smsPrice, this.getCurrency(), Type.INSIDE_PLAN));
			}
		}
		return ret;
	}
}