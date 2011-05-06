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
 * PepePhone Movilonia VIP
 * 
 * @author sanz
 */
public class ESPepePhoneMoviloniaVIP extends ESPepePhone {
    
	private double pricePerSecond = 0.12 / 60;
	private double pricePerSecond2 = 0.07 / 60;
	private double smsPrice = 0.09;
	private int maxSeconds = (3 * 60) + 30;
    
	public String getPlanName() {
		return "Tarifa Movilonia VIP";
	}
	
	public String getPlanURL() {
		return "http://www.pepephone.com/tarifamovilonia/";
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
					if (call.getDuration() <= maxSeconds) {
						callPrice = (call.getDuration() * pricePerSecond);
					} else {
						callPrice = (maxSeconds * pricePerSecond) + ((call.getDuration() - maxSeconds) * pricePerSecond2);
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