package com.conzebit.myplan.ext.es.movistar.particulares;

import java.util.ArrayList;
import java.util.Calendar;

import com.conzebit.myplan.core.Chargeable;
import com.conzebit.myplan.core.call.Call;
import com.conzebit.myplan.core.message.ChargeableMessage;
import com.conzebit.myplan.core.msisdn.MsisdnType;
import com.conzebit.myplan.core.plan.PlanChargeable;
import com.conzebit.myplan.core.plan.PlanSummary;
import com.conzebit.myplan.core.sms.Sms;
import com.conzebit.myplan.ext.es.movistar.ESMovistar;

/**
 * Movistar 8.
 * 
 * @author sanz
 */
public class ESMovistar8 extends ESMovistar {
    
	private double monthFee = 12;
	private double initialPrice = 0.15;
	private double pricePerSecond = 0.08 / 60;
	private double smsPrice = 0.15;
	
	private int maxSecondsMonth = 250 * 60;
	private int maxCallsMonth = 250;
	
    
	public String getPlanName() {
		return "Tarifa 8";
	}

	public String getPlanURL() {
		return "http://www.tarifas.movistar.es/web/movistar/detalle-tarifas-voz-particulares?tipo=tarifas&name=Tarifa%208&impuesto=Sin%20impuestos&idContenido=a07d1402-6956-4df3-9a73-f6c4f983d58a";
	}

	public PlanSummary process(ArrayList<Chargeable> data) {
		PlanSummary ret = new PlanSummary(this);
		ret.addPlanCall(new PlanChargeable(new ChargeableMessage(ChargeableMessage.MESSAGE_MONTH_FEE), monthFee, this.getCurrency()));

		int secondsTotal = 0;
		int callsTotal = 0;
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
					boolean insidePlan = false;
					boolean insideWeekend = (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY);
					if (insideWeekend) {
						secondsTotal += call.getDuration();
						callsTotal++;
						insidePlan = secondsTotal <= maxSecondsMonth && callsTotal <= maxCallsMonth;
					}
					if (!insidePlan) {
						long duration = (secondsTotal > maxSecondsMonth) && (secondsTotal - call.getDuration() <= maxSecondsMonth)? secondsTotal - maxSecondsMonth : call.getDuration();  
						callPrice += initialPrice + (duration * pricePerSecond);
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