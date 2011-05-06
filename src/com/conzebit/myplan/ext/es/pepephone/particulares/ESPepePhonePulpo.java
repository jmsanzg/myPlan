package com.conzebit.myplan.ext.es.pepephone.particulares;

import java.util.ArrayList;

import com.conzebit.myplan.core.Chargeable;
import com.conzebit.myplan.core.call.Call;

import com.conzebit.myplan.core.msisdn.MsisdnType;
import com.conzebit.myplan.core.plan.PlanChargeable;
import com.conzebit.myplan.core.plan.PlanSummary;
import com.conzebit.myplan.core.sms.Sms;
import com.conzebit.myplan.ext.es.pepephone.ESPepePhone;


/**
 * PepePhone Pulpo
 * 
 * @author sanz
 */
public class ESPepePhonePulpo extends ESPepePhone {
    
	private double initialPrice = 0.15;
	private double smsPrice = 0.09;
    
	public String getPlanName() {
		return "Tarifa Pulpo Pepe";
	}
	
	public String getPlanURL() {
		return "http://www.pepephone.com/promo/adslzone-pulpopepe/index.html";
	}
	
	public PlanSummary process(ArrayList<Chargeable> data) {
		PlanSummary ret = new PlanSummary(this);

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
					double pricePerSecond = (int) (call.getDuration() / 60);
					pricePerSecond = (pricePerSecond > 6) ? 0.06 : pricePerSecond/100;
					callPrice = initialPrice + (call.getDuration() * (pricePerSecond / 60));
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