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
package com.conzebit.myplan.ext.es.orange.particulares;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.conzebit.myplan.core.Chargeable;
import com.conzebit.myplan.core.call.Call;

import com.conzebit.myplan.core.message.ChargeableMessage;
import com.conzebit.myplan.core.msisdn.MsisdnType;
import com.conzebit.myplan.core.plan.PlanChargeable;
import com.conzebit.myplan.core.plan.PlanSummary;
import com.conzebit.myplan.core.sms.Sms;
import com.conzebit.myplan.ext.es.orange.ESOrange;
import com.conzebit.util.Formatter;


/**
 * Ardilla 12.
 * 
 * @author sanz
 */
public class ESOrangeArdilla12 extends ESOrange {
    
	private double minimumMonthFee = 12;
	private double initialPrice = 0.15;
	private double pricePerSecond = 0.17 / 60;
	private double smsPrice = 0.15;
	private int maxSecondsMonth = 1000 * 60;
	private int maxRecipients = 75;
	
	public String getPlanName() {
		return "Ardilla 12";
	}
	
	public String getPlanURL() {
		return "http://movil.orange.es/tarifas-y-ahorro/contrato/ardilla-12/";
	}
	
	public PlanSummary process(ArrayList<Chargeable> data) {
		PlanSummary ret = new PlanSummary(this);

		Set<String> recipients = new HashSet<String>();
		int secondsTotal = 0;
		double globalPrice = 0;
		String dateCallOrange = "";
		boolean ardillaCall;
		
		for (Chargeable chargeable : data) {
			if (chargeable.getChargeableType() == Chargeable.CHARGEABLE_TYPE_CALL) {
				Call call = (Call) chargeable;
				if (call.getType() != Call.CALL_TYPE_SENT) {
					continue;
				}
				
				double callPrice = 0;
				ardillaCall = false;
				if (call.getContact().getMsisdnType() == MsisdnType.ES_ORANGE) {
					String formattedDate = Formatter.formatDate(call.getDate());
					if (dateCallOrange.equals(formattedDate)) {
						ardillaCall = true;
						secondsTotal += call.getDuration();
						recipients.add(call.getContact().getMsisdn());
					} else {
						dateCallOrange = formattedDate;
					}
				}
				
				if (call.getContact().getMsisdnType() == MsisdnType.ES_SPECIAL_ZER0) {
					callPrice = 0;
				} else {
					recipients.add(call.getContact().getMsisdn());
					boolean insidePlan = ardillaCall && secondsTotal <= maxSecondsMonth && recipients.size() <= maxRecipients; 
					if (insidePlan) {
						callPrice += initialPrice;
					} else {
						long duration = (secondsTotal > maxSecondsMonth) && (secondsTotal - call.getDuration() <= maxSecondsMonth)? secondsTotal - maxSecondsMonth : call.getDuration();  
						callPrice += initialPrice + (duration * pricePerSecond);
					}
				}
				globalPrice += callPrice;
				ret.addPlanCall(new PlanChargeable(call, callPrice, this.getCurrency()));
			} else if (chargeable.getChargeableType() == Chargeable.CHARGEABLE_TYPE_SMS) {
				Sms sms = (Sms) chargeable;
				if (sms.getType() == Sms.SMS_TYPE_RECEIVED) {
					continue;
				}
				globalPrice += smsPrice;
				ret.addPlanCall(new PlanChargeable(chargeable, smsPrice, this.getCurrency()));
			}
		}
		if (globalPrice < minimumMonthFee) {
			ret.addPlanCall(new PlanChargeable(new ChargeableMessage(ChargeableMessage.MESSAGE_MINIMUM_MONTH_FEE), minimumMonthFee - globalPrice, this.getCurrency()));
		}
		return ret;
	}
}