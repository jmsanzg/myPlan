package com.conzebit.myplan.ext.es.vodafone.particulares;

import java.util.ArrayList;

import com.conzebit.myplan.core.Chargeable;
import com.conzebit.myplan.core.call.Call;
import com.conzebit.myplan.core.message.ChargeableMessage;
import com.conzebit.myplan.core.plan.PlanChargeable;
import com.conzebit.myplan.core.plan.PlanSummary;
import com.conzebit.myplan.core.sms.Sms;
import com.conzebit.myplan.ext.es.vodafone.ESVodafone;


/**
 * Vodafone Aire 90x1 24h.
 *
 * @author sanz
 */
public class ESVodafoneAire90x124h extends ESVodafone {
    
	private double minimumMonthFee = 9;
	private double initialPrice = 0.15;
	private double pricePerMinute = 0.199;
	private double smsPrice = 0.15;
    
	public String getPlanName() {
		return "A mi Aire 90x1 24h";
	}
	
	public String getPlanURL() {
		return "http://www.vodafone.es/particulares/tarifas/telefonia_movil/contrato/ami_aire/descripcion/?tab_ji5d91d961y7w3ac=6uzjbkwwpd0u8y52#iveizo503hjs9bhfq";
	}
	
	public PlanSummary process(ArrayList<Chargeable> data) {
		
		double globalPrice = 0;
		PlanSummary ret = new PlanSummary(this);
		for (Chargeable chargeable : data) {
			if (chargeable.getChargeableType() == Chargeable.CHARGEABLE_TYPE_CALL) {
				Call call = (Call) chargeable;
				if (call.getType() != Call.CALL_TYPE_SENT) {
					continue;
				}
				
				double callPrice = initialPrice + pricePerMinute;
				
				switch (call.getContact().getMsisdnType()) {
					case ES_VODAFONE:
					case ES_LAND_LINE:
						long over90 = call.getDuration() - (90 * 60);
						if (over90 > 0) {
							callPrice += pricePerMinute + (over90 * (pricePerMinute / 60));
						}
						break;
					case ES_SPECIAL_ZER0:
						callPrice = 0;
						break;
					default:
						long duration = call.getDuration() - 60;
						if (duration > 0) {
							callPrice +=  duration * (pricePerMinute / 60);
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