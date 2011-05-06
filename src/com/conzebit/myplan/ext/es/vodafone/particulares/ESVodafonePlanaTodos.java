package com.conzebit.myplan.ext.es.vodafone.particulares;

import java.util.ArrayList;
import java.util.Calendar;

import com.conzebit.myplan.core.Chargeable;
import com.conzebit.myplan.core.call.Call;

import com.conzebit.myplan.core.message.ChargeableMessage;
import com.conzebit.myplan.core.msisdn.MsisdnType;
import com.conzebit.myplan.core.plan.PlanChargeable;
import com.conzebit.myplan.core.plan.PlanSummary;
import com.conzebit.myplan.core.sms.Sms;
import com.conzebit.myplan.ext.es.vodafone.ESVodafone;


/**
 * Plana a Todos.
 *
 * @author sanz
 */
public class ESVodafonePlanaTodos extends ESVodafone {
    
	private double monthFee = 29.9;
	private double initialPrice = 0.15;
	private double pricePerSecond = 0.19 / 60;
	private double smsPrice = 0.15;
	private int maxSecondsMonth = 1000 * 60;
    
	public String getPlanName() {
		return "Plana a Todos";
	}
	
	public String getPlanURL() {
		return "http://www.vodafone.es/particulares/tarifas/telefonia_movil/contrato/tarifas_planas/descripcion/?tab_ji5d91d961y7w3ac=6uzjbkwwpd0u8y52#iveizo503hjs9bhfq";
	}
	
	public PlanSummary process(ArrayList<Chargeable> data) {
		PlanSummary ret = new PlanSummary(this);
		ret.addPlanCall(new PlanChargeable(new ChargeableMessage(ChargeableMessage.MESSAGE_MONTH_FEE), monthFee, this.getCurrency()));

		long secondsTotal = 0;
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
					secondsTotal += call.getDuration(); // TODO sumar sólo si son esos dias, no siempre
					boolean insidePlan = (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY || hourOfDay < 8 || hourOfDay >= 18) && secondsTotal <= maxSecondsMonth; 
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