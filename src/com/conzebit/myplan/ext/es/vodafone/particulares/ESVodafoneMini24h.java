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
 * Vodafone Clasico.
 *
 * @author sanz
 */
public class ESVodafoneMini24h extends ESVodafone {
    
	private double monthFee = 19.9;
	private double initialPrice = 0.15;
	private double pricePerSecond = 0.19 / 60;
	private double smsPrice = 0.15;
	private int maxSecondsMonth = 350 * 60;
    
	public String getPlanName() {
		return "Plana Mini 24h";
	}
	
	public String getPlanURL() {
		return "http://www.vodafone.es/particulares/tarifas/telefonia_movil/contrato/tarifas_planas/descripcion/?tab_ji5d91d961y7w3ac=vgl1e9n8zxmduiwn#iveizo503hjs9bhfq";
	}
	
	public PlanSummary process(ArrayList<Chargeable> data) {
		PlanSummary ret = new PlanSummary(this);
		ret.addPlanCall(new PlanChargeable(new ChargeableMessage(ChargeableMessage.MESSAGE_MONTH_FEE), monthFee, this.getCurrency()));
		long globalDuration = 0;
		for (Chargeable chargeable : data) {
			if (chargeable.getChargeableType() == Chargeable.CHARGEABLE_TYPE_CALL) {
				Call call = (Call) chargeable;

				if (call.getType() != Call.CALL_TYPE_SENT) {
					continue;
				}
	
				double callPrice = 0;
				long duration = call.getDuration();
				switch (call.getContact().getMsisdnType()) {
					case ES_VODAFONE:
					case ES_LAND_LINE:
						globalDuration += duration;
					case ES_SPECIAL_ZER0:
						if (globalDuration <= maxSecondsMonth) {
							callPrice = 0;
							break;
						} 
						if (globalDuration - call.getDuration() <= maxSecondsMonth) {
							duration = globalDuration - maxSecondsMonth; 
						}
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