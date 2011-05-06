package com.conzebit.myplan.ext.es.simyo.particulares;

import java.util.ArrayList;

import com.conzebit.myplan.core.Chargeable;
import com.conzebit.myplan.core.call.Call;

import com.conzebit.myplan.core.msisdn.MsisdnType;
import com.conzebit.myplan.core.plan.PlanChargeable;
import com.conzebit.myplan.core.plan.PlanSummary;
import com.conzebit.myplan.core.sms.Sms;
import com.conzebit.myplan.ext.es.simyo.ESSimyo;


/**
 * Simyo 0 y 8 centimos
 * @author sanz
 */
public class ESSimyo0y8centimos extends ESSimyo {
    
	private double initialPrice = 0.15;
	private double pricePerSecond = 0.08 / 60;
	private double pricePerSecondSimyo = 0.09 / 60;
	private int maxFreeSeconds = 10 * 60;
	private double smsPrice = 0.09;
	
    
	public String getPlanName() {
		return "0 y 8 céntimos";
	}
	
	public String getPlanURL() {
		return "http://www.simyo.es/tarifas.html";
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
					if (call.getContact().getMsisdnType() == MsisdnType.ES_SIMYO) {
						if (call.getDuration() <= maxFreeSeconds) {
							callPrice = initialPrice;
						} else {
							callPrice = initialPrice + ((call.getDuration() - maxFreeSeconds) * pricePerSecondSimyo);
						}
					} else  {
						callPrice = initialPrice + (call.getDuration() * pricePerSecond);
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