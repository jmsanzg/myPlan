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

/**
 * León 30.
 * 
 * @author sanz
 */
public class ESOrangeLeon30 extends ESOrange {
    
	private double monthFee = 30;
	private double initialPrice = 0.15;
	private double pricePerSecond = 0.18 / 60;
	private double smsPrice = 0.15;
	private int maxRecipients = 75;
	
	private int maxSecondsMonth = 600 * 60;
	private int maxSecondsMonthMobile = 300 * 60;
	
    
	public String getPlanName() {
		return "León 30";
	}

	public String getPlanURL() {
		return "http://movil.orange.es/tarifas-y-ahorro/contrato/leon-30/";
	}

	public PlanSummary process(ArrayList<Chargeable> data) {
		PlanSummary ret = new PlanSummary(this);
		ret.addPlanCall(new PlanChargeable(new ChargeableMessage(ChargeableMessage.MESSAGE_MONTH_FEE), monthFee, this.getCurrency()));

		int secondsTotal = 0;
		int secondsMobile = 0;
		Set<String> recipients = new HashSet<String>();
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
					recipients.add(call.getContact().getMsisdn());
					secondsTotal += call.getDuration();
					if (call.getContact().getMsisdnType().isMobile()) {
						secondsMobile += call.getDuration();
					}
					boolean insideMinutesMonth = secondsTotal <= maxSecondsMonth;
					boolean insideMinutesMonthMobile = secondsMobile <= maxSecondsMonthMobile;
					if (!insideMinutesMonth) {
						long duration = (secondsTotal > maxSecondsMonth) && (secondsTotal - call.getDuration() <= maxSecondsMonth)? secondsTotal - maxSecondsMonth : call.getDuration();  
						callPrice += initialPrice + (duration * pricePerSecond);
					} else if (!insideMinutesMonthMobile) {
						long duration = (secondsMobile > maxSecondsMonthMobile) && (secondsMobile - call.getDuration() <= maxSecondsMonthMobile)? secondsMobile - maxSecondsMonthMobile : call.getDuration();  
						callPrice += initialPrice + (duration * pricePerSecond);
					} else if (recipients.size() > maxRecipients) {
						callPrice += initialPrice + (call.getDuration() * pricePerSecond);
					}
				}
				ret.addPlanCall(new PlanChargeable(call, callPrice, this.getCurrency()));
			} else if (chargeable.getChargeableType() == Chargeable.CHARGEABLE_TYPE_SMS) {
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