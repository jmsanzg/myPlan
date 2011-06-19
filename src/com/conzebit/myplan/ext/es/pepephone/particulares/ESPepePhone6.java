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
import com.conzebit.myplan.core.sms.Sms;
import com.conzebit.myplan.ext.es.pepephone.ESPepePhone;


/**
 * PepePhone 6cent
 * 
 * @author sanz
 */
public class ESPepePhone6 extends ESPepePhone {
    
	private double initialPrice = 0.15;
	private double pricePerSecond = 0.06 / 60;
	private double smsPrice = 0.09;

    
	public String getPlanName() {
		return "Tarifa 6 céntimos";
	}
	
	public String getPlanURL() {
		return "http://www.pepephone.com/ppm_web/ppm_web/1/tarifas/xweb_tarifa.lista_tarifas_nacionales.html";
	}
	
	public Double processCall(Call call, Map<String, Object> accumulatedData) {
		if (call.getType() != Call.CALL_TYPE_SENT) {
			return null;
		}

		return initialPrice + (call.getDuration() * pricePerSecond);
	}

	public Double processSms(Sms sms, Map<String, Object> accumulatedData) {
		if (sms.getType() == Sms.SMS_TYPE_RECEIVED) {
			return null;
		}
		return smsPrice;
	}
}