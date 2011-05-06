package com.conzebit.myplan.ext.es.vodafone.particulares.tallas;

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
 * Vodafone @L.
 *
 * @author sanz
 */
public class ESVodafoneAtL extends ESVodafone {
    
	private double monthFee = 59.90;
	private double initialPrice = 0.15;
	private double pricePerSecond = 0.1990 / 60;
	private double smsPrice = 0.15;
	private int maxSecondsMorningMonth = 1000 * 60;
	private int maxSecondsEveningMonth = 350 * 60;
	private int maxFreeSMS = 350;
    
	public String getPlanName() {
		return "@L";
	}
	
	public String getPlanURL() {
		return "http://www.vodafone.es/particulares/es/moviles-y-fijo/tarifas/telefonia-movil/para-hablar-y-navegar/smartphones/";
	}
	
	public PlanSummary process(ArrayList<Chargeable> data) {
		PlanSummary ret = new PlanSummary(this);
		ret.addPlanCall(new PlanChargeable(new ChargeableMessage(ChargeableMessage.MESSAGE_MONTH_FEE), monthFee, this.getCurrency()));
		long morningTotalSeconds = 0;
		long eveningTotalSeconds = 0;
		int smsSent = 0;
		for (Chargeable chargeable : data) {
			if (chargeable.getChargeableType() == Chargeable.CHARGEABLE_TYPE_CALL) {
				Call call = (Call) chargeable;

				if (call.getType() != Call.CALL_TYPE_SENT) {
					continue;
				}
	
				double callPrice = 0;
				if (call.getContact().getMsisdnType() == MsisdnType.ES_SPECIAL_ZER0) {
					callPrice = 0;
				} else if (call.getContact().getMsisdnType() == MsisdnType.ES_VODAFONE) {
					callPrice = 0;
				} else {
					int hourOfDay = call.getDate().get(Calendar.HOUR_OF_DAY);
					if (hourOfDay < 8 || hourOfDay >= 18) {
						eveningTotalSeconds += call.getDuration();
						boolean insidePlan = eveningTotalSeconds <= maxSecondsEveningMonth; 
						if (!insidePlan) {
							long duration = (eveningTotalSeconds > maxSecondsEveningMonth) && (eveningTotalSeconds - call.getDuration() <= maxSecondsEveningMonth)? eveningTotalSeconds - maxSecondsEveningMonth : call.getDuration();  
							callPrice += initialPrice + (duration * pricePerSecond);
						}
					} else {
						morningTotalSeconds += call.getDuration();
						boolean insidePlan = morningTotalSeconds <= maxSecondsEveningMonth; 
						if (!insidePlan) {
							long duration = (morningTotalSeconds > maxSecondsMorningMonth) && (morningTotalSeconds - call.getDuration() <= maxSecondsMorningMonth)? morningTotalSeconds - maxSecondsMorningMonth : call.getDuration();  
							callPrice += initialPrice + (duration * pricePerSecond);
						}
					}
				}
				ret.addPlanCall(new PlanChargeable(call, callPrice, this.getCurrency()));
			} else if (chargeable.getChargeableType() == Chargeable.CHARGEABLE_TYPE_SMS) {
				Sms sms = (Sms) chargeable;
				if (sms.getType() == Sms.SMS_TYPE_RECEIVED) {
					continue;
				}
				smsSent++;
				ret.addPlanCall(new PlanChargeable(chargeable, (smsSent > maxFreeSMS)?smsPrice:0, this.getCurrency()));
			}
		}
		return ret;
	}
}