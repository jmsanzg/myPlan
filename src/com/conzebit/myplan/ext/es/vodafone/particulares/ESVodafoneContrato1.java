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
 * Vodafone Contrato1.
 *
 * @author sanz
 */
public class ESVodafoneContrato1 extends ESVodafone {
    
	private double minimumMonthFee = 9;
	private double initialPrice = 0.15;
	private double pricePerSecond = 0.35 / 60;
	private double pricePerSecondVodafone = 0.01 / 60;
	private double smsPrice = 0.15;
    
	public String getPlanName() {
		return "Contrato 1";
	}
	
	public String getPlanURL() {
		return "http://www.vodafone.es/conocenos/sala-prensa/notas-prensa/2007/31-05-2007.jsp";
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
				switch (call.getContact().getMsisdnType()) {
					case ES_SPECIAL_ZER0:
						callPrice = 0;
						break;
					case ES_VODAFONE:
						callPrice = initialPrice + (call.getDuration() * pricePerSecondVodafone);
						break;
					default:
						callPrice = initialPrice + (call.getDuration() * pricePerSecond);
				}
				globalPrice += callPrice;
				ret.addPlanCall(new PlanChargeable(call, callPrice, this.getCurrency()));
			} else if (chargeable.getChargeableType() == Chargeable.CHARGEABLE_TYPE_SMS) {
				Sms sms = (Sms) chargeable;
				if (sms.getType() == Sms.SMS_TYPE_RECEIVED) {
					continue;
				}
				ret.addPlanCall(new PlanChargeable(chargeable, smsPrice, this.getCurrency()));
			}
		}
		if (globalPrice < minimumMonthFee) {
			ret.addPlanCall(new PlanChargeable(new ChargeableMessage(ChargeableMessage.MESSAGE_MINIMUM_MONTH_FEE), minimumMonthFee - globalPrice, this.getCurrency()));
		}
		return ret;
	}
}