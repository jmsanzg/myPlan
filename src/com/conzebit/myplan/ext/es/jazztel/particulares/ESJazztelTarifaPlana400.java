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
package com.conzebit.myplan.ext.es.jazztel.particulares;

import java.util.Map;

import com.conzebit.myplan.core.call.Call;
import com.conzebit.myplan.core.msisdn.MsisdnType;
import com.conzebit.myplan.core.plan.PlanChargeable.Type;
import com.conzebit.myplan.core.sms.Sms;
import com.conzebit.myplan.ext.es.jazztel.ESJazztel;


/**
 * Jazztel Tarifa plana 400.
 *
 * @author jmsanzg@gmail.com
 */
public class ESJazztelTarifaPlana400 extends ESJazztel {
	
	private final String ACCUMULATED_DATA_SECONDS = "SECONDS";
    
	private double monthFee = 47.95;
	private double initialPrice = 0.15;
	private double pricePerSecond = 0.10 / 60;
	private double smsPrice = 0.12;
	private int maxSecondsMonth = 400 * 60;
    
	public String getPlanName() {
		return "Tarifa Plana 400";
	}
	
	public String getPlanURL() {
		return "http://www.jazztelsemueve.com/telefonia_tarifas.html";
	}
	
	public Double getMonthFee() {
		return monthFee;
	}
	
	public ProcessResult processCall(Call call, Map<String, Object> accumulatedData) {
		if (call.getType() != Call.CALL_TYPE_SENT) {
			return null;
		}
		
		Long secondsTotal = (Long) accumulatedData.get(ACCUMULATED_DATA_SECONDS);
		if (secondsTotal == null) {
			secondsTotal = new Long(0);
		}
		secondsTotal += call.getDuration();
		accumulatedData.put(ACCUMULATED_DATA_SECONDS, secondsTotal);
		
		ProcessResult ret = new ProcessResult();
		if (call.getContact().getMsisdnType() == MsisdnType.ES_SPECIAL_ZER0) {
			ret.price = 0.0;
			ret.type = Type.ZERO;
		} else {
			boolean insidePlan =  secondsTotal <= maxSecondsMonth; 
			if (insidePlan) {
				ret.price = 0.0;
				ret.type = Type.INSIDE_PLAN;
			} else {
				long duration = (secondsTotal > maxSecondsMonth) && (secondsTotal - call.getDuration() <= maxSecondsMonth)? secondsTotal - maxSecondsMonth : call.getDuration();  
				ret.price = initialPrice + (duration * pricePerSecond);
				ret.type = Type.OUTSIDE_PLAN;
			}
		}
		return ret;
	}

	public ProcessResult processSms(Sms sms, Map<String, Object> accumulatedData) {
		if (sms.getType() != Sms.SMS_TYPE_SENT) {
			return null;
		}
		ProcessResult ret = new ProcessResult();
		ret.price = smsPrice;
		ret.type = Type.INSIDE_PLAN;
		return ret;
	}
}