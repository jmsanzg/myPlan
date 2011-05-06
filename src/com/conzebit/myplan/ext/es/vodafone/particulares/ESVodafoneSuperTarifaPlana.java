package com.conzebit.myplan.ext.es.vodafone.particulares;

import java.util.ArrayList;

import com.conzebit.myplan.core.Chargeable;
import com.conzebit.myplan.core.call.Call;

import com.conzebit.myplan.core.message.ChargeableMessage;
import com.conzebit.myplan.core.msisdn.MsisdnType;
import com.conzebit.myplan.core.plan.PlanChargeable;
import com.conzebit.myplan.core.plan.PlanSummary;
import com.conzebit.myplan.core.sms.Sms;
import com.conzebit.myplan.ext.es.vodafone.ESVodafone;


/**
 * Vodafone Super Tarifa Plana Mini.
 *
 * @author sanz
 */
public class ESVodafoneSuperTarifaPlana extends ESVodafone {
    
	private double monthFee = 99.9;
	private double initialPrice = 0.15;
	private double pricePerSecond = 0.19 / 60;
	private double smsPrice = 0.15;
	private int maxSecondsMonth = 1000 * 60;
	private int maxSmsFree = 1000;
    
	public String getPlanName() {
		return "Super Tarifa Plana";
	}
	
	public String getPlanURL() {
		return "http://www.vodafone.es/particulares/tarifas/telefonia_movil/contrato/tarifas_planas/descripcion/?tab_ji5d91d961y7w3ac=0xjqim4rfhkq7lae#iveizo503hjs9bhfq";
	}
	
	public PlanSummary process(ArrayList<Chargeable> data) {
		PlanSummary ret = new PlanSummary(this);
		ret.addPlanCall(new PlanChargeable(new ChargeableMessage(ChargeableMessage.MESSAGE_MONTH_FEE), monthFee, this.getCurrency()));
		long globalDuration = 0;
		int smsFreeCount = 0;
		for (Chargeable chargeable : data) {
			if (chargeable.getChargeableType() == Chargeable.CHARGEABLE_TYPE_CALL) {
				Call call = (Call) chargeable;

				if (call.getType() != Call.CALL_TYPE_SENT) {
					continue;
				}
	
				double callPrice = 0;
				long duration = call.getDuration();
				if (call.getContact().getMsisdnType() == MsisdnType.ES_SPECIAL_ZER0) {
					callPrice = 0;
				} else {
					globalDuration += duration;
					if (globalDuration <= maxSecondsMonth) {
						callPrice = 0;
					} else {
						if (globalDuration - call.getDuration() <= maxSecondsMonth) {
							duration = globalDuration - maxSecondsMonth;
						}
						callPrice = initialPrice + (duration * pricePerSecond);
					}
				}
				ret.addPlanCall(new PlanChargeable(call, callPrice, this.getCurrency()));
			} else if (chargeable.getChargeableType() == Chargeable.CHARGEABLE_TYPE_SMS) {
				Sms sms = (Sms) chargeable;
				if (sms.getType() == Sms.SMS_TYPE_RECEIVED) {
					continue;
				}
				double charge = smsPrice;
				if (sms.getContact().getMsisdnType() == MsisdnType.ES_VODAFONE && smsFreeCount < maxSmsFree) {
					smsFreeCount++;
					charge = 0;
				}
				ret.addPlanCall(new PlanChargeable(chargeable, charge, this.getCurrency()));
			}
		}
		return ret;
	}
}