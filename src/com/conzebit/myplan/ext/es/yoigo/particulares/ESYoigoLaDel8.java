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
import com.conzebit.myplan.core.sms.Sms;
import com.conzebit.myplan.ext.es.yoigo.ESYoigo;


/**
 * Yoigo La del 8.
 * @author sanz
 */
public class ESYoigoLaDel8 extends ESYoigo {
    
	private double minimumMonthFee = 6.0;
	private double initialPrice = 0.15;
	private double pricePerSecond = 0.08 / 60;
	private double smsPrice = 0.08;

    
	public String getPlanName() {
		return "La del 8";
	}

	public String getPlanURL() {
		return "http://www.yoigo.com/tarifas/index.php";
	}
	
	public Double getMinimumMonthFee() {
		return minimumMonthFee;
	}
	
	public Double processCall(Call call, Map<String, Object> accumulatedData) {
		if (call.getType() != Call.CALL_TYPE_SENT) {
			return null;
		}

		return initialPrice + (call.getDuration() * pricePerSecond);
	}

	public Double processSms(Sms sms, Map<String, Object> accumulatedData) {
		if (sms.getType() != Sms.SMS_TYPE_SENT) {
			return null;
		}
		return smsPrice;
	}	
}