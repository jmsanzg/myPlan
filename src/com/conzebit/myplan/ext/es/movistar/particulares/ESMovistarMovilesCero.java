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
package com.conzebit.myplan.ext.es.movistar.particulares;

import java.util.ArrayList;

import com.conzebit.myplan.core.Chargeable;
import com.conzebit.myplan.core.call.Call;

import com.conzebit.myplan.core.message.ChargeableMessage;
import com.conzebit.myplan.core.plan.PlanChargeable;
import com.conzebit.myplan.core.plan.PlanSummary;
import com.conzebit.myplan.core.sms.Sms;
import com.conzebit.myplan.ext.es.movistar.ESMovistar;


/**
 * Movistar Contrato móviles cero.
 * 
 * @author sanz
 */
public class ESMovistarMovilesCero extends ESMovistar {
    
	private double minimumMonthFee = 6;
	private double initialPrice = 0.15;
	private double pricePerSecond = 0.12 / 60;
	private double smsPrice = 0.10;
	//private double mmsPrice = 1.0;
    
	public String getPlanName() {
		return "Contrato Móviles Cero";
	}
	
	public String getPlanURL() {
		return "http://www.tarifas.movistar.es/web/movistar/detalle-tarifas-voz-particulares?tipo=tarifas&name=Contrato%20M%C3%B3viles%20Movistar&impuesto=Sin%20impuestos&idContenido=c7003a01-4cdb-4c13-9213-365c4fb7d0ee";
	}
	
	public PlanSummary process(ArrayList<Chargeable> data) {
		PlanSummary ret = new PlanSummary(this);
		double globalPrice = 0;
		for (Chargeable chargeable : data) {
			if (chargeable.getChargeableType() == Chargeable.CHARGEABLE_TYPE_CALL) {
				Call call = (Call) chargeable;
				
				if (call.getType() != Call.CALL_TYPE_SENT) {
					continue;
				}
	
				double callPrice = 0;
				switch (call.getContact().getMsisdnType()) {
					case ES_SPECIAL_ZER0:
						callPrice = 0;
						break;
					case ES_MOVISTAR:
						// TODO terminar, sólo admite 1h al día de teléfonos movistar
					default:
						callPrice = initialPrice + (call.getDuration() * pricePerSecond);	
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