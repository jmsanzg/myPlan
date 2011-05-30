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
package com.conzebit.myplan.ext.es.orange.particulares;

import java.util.ArrayList;
import java.util.Calendar;

import com.conzebit.myplan.core.Chargeable;
import com.conzebit.myplan.core.call.Call;
import com.conzebit.myplan.core.message.ChargeableMessage;
import com.conzebit.myplan.core.msisdn.MsisdnType;
import com.conzebit.myplan.core.plan.PlanChargeable;
import com.conzebit.myplan.core.plan.PlanConfig;
import com.conzebit.myplan.core.plan.PlanConfigElement;
import com.conzebit.myplan.core.plan.PlanSummary;
import com.conzebit.myplan.core.sms.Sms;
import com.conzebit.myplan.ext.es.orange.ESOrange;


/**
 * Ardilla 9.
 * 
 * @author sanz
 */
public class ESOrangeArdilla9 extends ESOrange {
    
	private double minimumMonthFee = 9;
	private double initialPrice = 0.15;
	private double pricePerSecond = 0.07 / 60;
	private double pricePerSecondOutside = 0.30 / 60;
	private double smsPrice = 0.15;
	
	private PlanConfig planConfig;
	private final static String CONFIG_KEY = "es.orange.ardilla9.hour";
	
	public ESOrangeArdilla9() {
		this.planConfig = new PlanConfig();
		PlanConfigElement<Integer> pce = new PlanConfigElement<Integer>(CONFIG_KEY, 8, 8, 17, Integer.class);
		this.planConfig.addPlanConfigElement(pce);
	}

	public String getPlanName() {
		return "Ardilla 9";
	}

	public String getScreenPlanName() {
		int selectedHour = (Integer) this.planConfig.getPlanConfigElement(CONFIG_KEY).getValue();
		StringBuffer ret = new StringBuffer("Ardilla 9 (")
			.append(selectedHour)
			.append("h-")
			.append(selectedHour + 5)
			.append("h)");
		return ret.toString();
	}
	
	public String getPlanURL() {
		return "http://movil.orange.es/tarifas-y-ahorro/contrato/ardilla-9/";
	}
	
	public PlanConfig getPlanConfig() {
		return planConfig;
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
				
				if (call.getContact().getMsisdnType() == MsisdnType.ES_SPECIAL_ZER0) {
					callPrice = 0;
				} else {
	
					int dayOfWeek = call.getDate().get(Calendar.DAY_OF_WEEK);
					int hourOfDay = call.getDate().get(Calendar.HOUR_OF_DAY);
					int selectedHour = (Integer) this.planConfig.getPlanConfigElement(CONFIG_KEY).getValue();
					boolean insideDay = dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY || hourOfDay >= 22 || hourOfDay < 8 || (hourOfDay >= selectedHour && hourOfDay < selectedHour + 5);
					if (insideDay) {
						callPrice = initialPrice + (call.getDuration() * pricePerSecond);
					} else {
						callPrice = initialPrice + (call.getDuration() * pricePerSecondOutside);
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