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
package com.conzebit.myplan.ext.es.yoigo.particulares;

import java.util.Map;

import com.conzebit.myplan.core.call.Call;
import com.conzebit.myplan.core.msisdn.MsisdnType;
import com.conzebit.myplan.core.plan.PlanChargeable.Type;
import com.conzebit.myplan.core.sms.Sms;
import com.conzebit.myplan.ext.es.yoigo.ESYoigo;
import com.conzebit.util.Formatter;


/**
 * Yoigo La del 0.
 * http://www.yoigo.com/tarifas/index.php
 * @author sanz
 */
public class ESYoigoLaDel0 extends ESYoigo {
	
	private final String ACCUMULATED_DATA_DATE_CALL_YOIGO = "DATE_CALL_YOIGO";
	private final String ACCUMULATED_DATA_YOIGO_SECONDS = "YOIGO_SECONDS";
    
	private double minimumMonthFee = 6.0;
	private double initialPrice = 0.15;
	private double pricePerSecond = 0.12 / 60;
	private double smsPrice = 0.10;
	private long maxYoigoSeconds = 60 * 60;
    
	public String getPlanName() {
		return "La del 0";
	}
	
	public String getPlanURL() {
		return "http://www.yoigo.com/tarifas/index.php";
	}
	
	public Double getMinimumMonthFee() {
		return minimumMonthFee;
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
			String dateCallYoigo = (String) accumulatedData.get(ACCUMULATED_DATA_DATE_CALL_YOIGO);
			if (dateCallYoigo == null) {
				dateCallYoigo = "";
			}
			Long yoigoSeconds = (Long) accumulatedData.get(ACCUMULATED_DATA_YOIGO_SECONDS);
			if (yoigoSeconds == null) {
				yoigoSeconds = new Long(0);
			}

			if (call.getContact().getMsisdnType() == MsisdnType.ES_YOIGO) {
				String formattedDate = Formatter.formatDate(call.getDate());
				if (dateCallYoigo.equals(formattedDate)) {
					yoigoSeconds = yoigoSeconds + call.getDuration();
				} else {
					dateCallYoigo = formattedDate;
					accumulatedData.put(ACCUMULATED_DATA_DATE_CALL_YOIGO, dateCallYoigo);
					yoigoSeconds = call.getDuration();
				}
				accumulatedData.put(ACCUMULATED_DATA_YOIGO_SECONDS, yoigoSeconds);
				
				if (yoigoSeconds <= maxYoigoSeconds) {
					ret.price = initialPrice;
					ret.type = Type.INSIDE_PLAN;
				} else {
					long duration = call.getDuration();
					if (yoigoSeconds - call.getDuration() < maxYoigoSeconds) {
						duration = yoigoSeconds - call.getDuration();
					}
					ret.price = initialPrice + (duration * pricePerSecond);
					ret.type = Type.OUTSIDE_PLAN;
				}
			} else {
				ret.price = initialPrice + (call.getDuration() * pricePerSecond);
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