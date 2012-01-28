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
package com.conzebit.myplan.ext.es.pepephone.particulares;

import java.util.Map;

import com.conzebit.myplan.core.call.Call;
import com.conzebit.myplan.core.msisdn.MsisdnType;
import com.conzebit.myplan.core.plan.PlanChargeable.Type;
import com.conzebit.myplan.core.sms.Sms;
import com.conzebit.myplan.ext.es.pepephone.ESPepePhone;


/**
 * PepePhone Tarifa Menguante
 * 
 * @author sanz
 */
public class ESPepePhoneTarifaMenguante extends ESPepePhone {
    
	private double monthFee = 6.9;
	private double initialPrice = 0.15;
	private double smsPrice = 0.09;
	private double []pricePerSecond = {0.035/60, 0.03/60, 0.025/60, 0.02/60};
	
	@Override
    public Double getMonthFee() {
	    return monthFee;
    }

	public String getPlanName() {
		return "Tarifa Menguante";
	}
	
	public String getPlanURL() {
		return "http://www.pepephone.com/promo/la_increible_tarifa_menguante/";
	}
	
	public ProcessResult processCall(Call call, Map<String, Object> accumulatedData) {
		if (call.getType() != Call.CALL_TYPE_SENT) {
			return null;
		}
		
		ProcessResult ret = new ProcessResult();
		if (call.getContact().getMsisdnType() == MsisdnType.ES_SPECIAL_ZER0) {
			ret.price = 0.0;
			ret.type = Type.ZERO;
		} else {
			Long accumulatedSeconds = (Long) accumulatedData.get("TOTALMIN");
			if (accumulatedSeconds == null) {
				accumulatedSeconds = 0l;
			}
			
			long step = (long) accumulatedSeconds / 3600;
			if (step >= pricePerSecond.length) {
				step = pricePerSecond.length - 1;
			}
			
			accumulatedSeconds = accumulatedSeconds + call.getDuration();
			accumulatedData.put("TOTALMIN", accumulatedSeconds);
			ret.price = initialPrice + (call.getDuration() * pricePerSecond[(int) step]);
			ret.type = Type.INSIDE_PLAN;
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