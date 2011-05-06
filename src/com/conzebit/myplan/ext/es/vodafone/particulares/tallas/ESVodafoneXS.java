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
 * Vodafone XS.
 *
 * @author sanz
 */
public class ESVodafoneXS extends ESVodafone {
    
	private double minimumMonthFee = 9;
	private double initialPrice = 0.15;
	private double pricePerMinute = 0.199;
	private double smsPrice = 0.15;
    
	public String getPlanName() {
		return "XS";
	}
	
	public String getPlanURL() {
		return "http://www.vodafone.es/particulares/es/moviles-y-fijo/tarifas/telefonia-movil/para-hablar/contrato/";
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
				
				double callPrice = 0;
	
				int dayOfWeek = call.getDate().get(Calendar.DAY_OF_WEEK);
				int hourOfDay = call.getDate().get(Calendar.HOUR_OF_DAY);
				if (call.getContact().getMsisdnType() == MsisdnType.ES_SPECIAL_ZER0) {
					callPrice = 0;
				} else if (dayOfWeek == 1 || dayOfWeek == 7 || hourOfDay < 8 || hourOfDay >= 18) {
					callPrice = initialPrice + pricePerMinute;
					long over90 = call.getDuration() - (90 * 60);
					if (over90 > 0) {
						callPrice += over90 * (pricePerMinute / 60);
					}
				} else {
					callPrice = initialPrice + (call.getDuration() * (pricePerMinute / 60));
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