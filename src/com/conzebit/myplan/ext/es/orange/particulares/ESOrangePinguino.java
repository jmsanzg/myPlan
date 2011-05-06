package com.conzebit.myplan.ext.es.orange.particulares;

import java.util.ArrayList;
import java.util.Calendar;

import com.conzebit.myplan.core.Chargeable;
import com.conzebit.myplan.core.call.Call;
import com.conzebit.myplan.core.plan.PlanChargeable;
import com.conzebit.myplan.core.plan.PlanSummary;
import com.conzebit.myplan.core.sms.Sms;
import com.conzebit.myplan.ext.es.orange.ESOrange;


/**
 * Pingüino.
 *
 * @author sanz
 */
public class ESOrangePinguino extends ESOrange {
    
	private double initialPrice = 0.15;
	private double pricePerSecond = 0.14 / 60;
	private double smsPrice = 0.15;
	private int maxSecondsMonth = 1000 * 60;
    
	public String getPlanName() {
		return "Pingüino";
	}
	
	public String getPlanURL() {
		return "http://movil.orange.es/tarifas-y-ahorro/contrato/pinguino/";
	}
	
	public PlanSummary process(ArrayList<Chargeable> data) {
		PlanSummary ret = new PlanSummary(this);
		long totalSeconds = 0;
		for (Chargeable chargeable : data) {
			if (chargeable.getChargeableType() == Chargeable.CHARGEABLE_TYPE_CALL) {
				Call call = (Call) chargeable;
	
				if (call.getType() != Call.CALL_TYPE_SENT) {
					continue;
				}
	
				double callPrice = 0;
				long duration = call.getDuration();
				switch (call.getContact().getMsisdnType()) {
					case ES_SPECIAL_ZER0:
						break;
					case ES_ORANGE:
						int dayOfWeek = call.getDate().get(Calendar.DAY_OF_WEEK);
						int hourOfDay = call.getDate().get(Calendar.HOUR_OF_DAY);
						
						boolean insidePlan = false;
						boolean insideWeekend = (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY);
						boolean insideTimetable = hourOfDay < 8 || hourOfDay >= 18;
						if (insideWeekend || insideTimetable) {
							totalSeconds += call.getDuration();
							insidePlan = totalSeconds <= maxSecondsMonth;
						}
						if (insidePlan) {
							break;
						}
						duration = (totalSeconds > maxSecondsMonth) && (totalSeconds - call.getDuration() <= maxSecondsMonth)? totalSeconds - maxSecondsMonth : call.getDuration();  
					default:
						callPrice = initialPrice + (duration * pricePerSecond);
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