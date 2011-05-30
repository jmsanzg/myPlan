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
package com.conzebit.myplan.ext.es.vodafone.empresas;

import java.util.ArrayList;
import java.util.Calendar;

import com.conzebit.myplan.core.Chargeable;
import com.conzebit.myplan.core.call.Call;

import com.conzebit.myplan.core.message.ChargeableMessage;
import com.conzebit.myplan.core.msisdn.MsisdnType;
import com.conzebit.myplan.core.plan.PlanChargeable;
import com.conzebit.myplan.core.plan.PlanSummary;
import com.conzebit.myplan.core.sms.Sms;
import com.conzebit.myplan.ext.es.vodafone.ESVodafone;


/**
 * Conecta Voz e Internet.
 *
 * @author sanz
 */
public class ESVodafoneConectaVozInternet extends ESVodafone {
    
	private double monthFee = 19.0;
	private double initialPrice = 0.15;
	private double pricePerSecond = 0.18 / 60;
	private int maxSecondsMonth = 1000 * 60;
	private double smsPrice = 0.15;
    
	public String getPlanName() {
		return "Conecta Voz e Internet";
	}
	
	public String getPlanURL() {
		return "http://www.vodafone.es/empresas/tarifas/voz/plan-conecta-voz-e-internet/";
	}
	
	public PlanSummary process(ArrayList<Chargeable> data) {
		PlanSummary ret = new PlanSummary(this);
		ret.addPlanCall(new PlanChargeable(new ChargeableMessage(ChargeableMessage.MESSAGE_MONTH_FEE), monthFee, this.getCurrency()));

		int secondsTotal = 0;
		for (Chargeable chargeable : data) {
			if (chargeable.getChargeableType() == Chargeable.CHARGEABLE_TYPE_CALL) {
				Call call = (Call) chargeable;
				if (call.getType() != Call.CALL_TYPE_SENT) {
					continue;
				}
				
				double callPrice = 0;
				
				if (call.getContact().getMsisdnType() == MsisdnType.ES_SPECIAL_ZER0) {
					callPrice = 0;
				} else {
					int dayOfWeek = call.getDate().get(Calendar.DAY_OF_WEEK);
					int hourOfDay = call.getDate().get(Calendar.HOUR_OF_DAY);
					secondsTotal += call.getDuration();
					boolean insidePlan = dayOfWeek > 1 && dayOfWeek < 7 && hourOfDay >= 8 && hourOfDay < 20 && secondsTotal <= maxSecondsMonth; 
					if (!insidePlan) {
						long duration = (secondsTotal > maxSecondsMonth) && (secondsTotal - call.getDuration() <= maxSecondsMonth)? secondsTotal - maxSecondsMonth : call.getDuration();  
						callPrice += initialPrice + (duration * pricePerSecond);
					} else {
						callPrice = initialPrice;
					}
				}
				ret.addPlanCall(new PlanChargeable(call, callPrice, this.getCurrency()));
			} else {
				Sms sms = (Sms) chargeable;
				if (sms.getType() == Sms.SMS_TYPE_RECEIVED) {
					continue;
				}
				ret.addPlanCall(new PlanChargeable(chargeable, smsPrice, this.getCurrency()));
			}
		}
		return ret;
	}
}