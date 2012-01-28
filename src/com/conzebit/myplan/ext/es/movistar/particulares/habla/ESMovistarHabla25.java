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
package com.conzebit.myplan.ext.es.movistar.particulares.habla;

import java.util.ArrayList;

import com.conzebit.myplan.core.Chargeable;
import com.conzebit.myplan.core.call.Call;
import com.conzebit.myplan.core.message.ChargeableMessage;
import com.conzebit.myplan.core.msisdn.MsisdnType;
import com.conzebit.myplan.core.plan.PlanChargeable;
import com.conzebit.myplan.core.plan.PlanSummary;
import com.conzebit.myplan.core.plan.PlanChargeable.Type;
import com.conzebit.myplan.core.sms.Sms;
import com.conzebit.myplan.ext.es.movistar.ESMovistar;


/**
 * Habla 25.
 * 
 * @author sanz
 */
public class ESMovistarHabla25 extends ESMovistar {
    
	private double monthFee = 25;
	private double initialPrice = 0.15;
	private double pricePerSecond = 0.18 / 60;
	private double smsPrice = 0.15;
	private int maxSecondsMonth = 300 * 60;
    
	public String getPlanName() {
		return "Habla 25";
	}
	
	public String getPlanURL() {
		return "http://www.tarifas.movistar.es/web/movistar/detalle-tarifas-voz-particulares?tipo=tarifas&name=Tarifa%20Habla%2025&impuesto=Sin%20impuestos&idContenido=33f4a8d6-f003-4bec-8385-cd3b5304d3e2";
	}
	
	public PlanSummary process(ArrayList<Chargeable> data) {
		PlanSummary ret = new PlanSummary(this);
		ret.addPlanCall(new PlanChargeable(new ChargeableMessage(ChargeableMessage.MESSAGE_MONTH_FEE), monthFee, this.getCurrency(), Type.MONTH_FEE));

		int secondsTotal = 0;
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
					secondsTotal += call.getDuration();
					boolean insidePlan = secondsTotal <= maxSecondsMonth;
					if (insidePlan) {
						type = Type.INSIDE_PLAN;
					} else {
						long duration = (secondsTotal > maxSecondsMonth) && (secondsTotal - call.getDuration() <= maxSecondsMonth)? secondsTotal - maxSecondsMonth : call.getDuration();  
						callPrice += initialPrice + (duration * pricePerSecond);
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