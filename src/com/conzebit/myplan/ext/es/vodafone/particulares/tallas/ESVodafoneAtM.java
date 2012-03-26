/*
 * This file is part of myPlan.
 *
 * Plan is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * Plan is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with myPlan.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.conzebit.myplan.ext.es.vodafone.particulares.tallas;

import java.util.ArrayList;

import com.conzebit.myplan.core.Chargeable;
import com.conzebit.myplan.core.call.Call;
import com.conzebit.myplan.core.contact.Contact;
import com.conzebit.myplan.core.message.ChargeableMessage;
import com.conzebit.myplan.core.msisdn.MsisdnType;
import com.conzebit.myplan.core.plan.PlanChargeable;
import com.conzebit.myplan.core.plan.PlanConfig;
import com.conzebit.myplan.core.plan.PlanConfigElement;
import com.conzebit.myplan.core.plan.PlanSummary;
import com.conzebit.myplan.core.sms.Sms;
import com.conzebit.myplan.ext.es.vodafone.ESVodafone;


/**
 * Vodafone @M.
 *
 * @author sanz
 */
public class ESVodafoneAtM extends ESVodafone {
    
	private double monthFee = 40;
	private double initialPrice = 0.15;
	private double pricePerSecond = 0.20 / 60;
	private double smsPrice = 0.15;
	private int maxSecondsMonth = 350 * 60;
	private int maxFreeSMS = 350;
	// 500MB data
	
	private PlanConfig planConfig;
	private final static String CONFIG_KEY ="es.vodafone.tallas.vip";
	
	public ESVodafoneAtM() {
		this.planConfig = new PlanConfig();
		// 2 números VIP
		PlanConfigElement<String> pce = new PlanConfigElement<String>(CONFIG_KEY, "", "", "", String.class);
		this.planConfig.addPlanConfigElement(pce);
	}
    
	public String getPlanName() {
		return "@M";
	}
	
	public String getPlanURL() {
		return "http://www.vodafone.es/particulares/es/moviles-y-fijo/tarifas/telefonia-movil/para-hablar-y-navegar/smartphones/";
	}
	
	/**
	 * Evaluate that contact is on my favourite list
	 * @param contact
	 * @return
	 */
	private boolean isFavouriteContact(Contact contact){
		PlanConfigElement<String> value = this.planConfig.getPlanConfigElement(CONFIG_KEY);
		return (value.getValue().indexOf(contact.getMsisdn())>=0);
		//TODO: Optimizar evaluación
	}
	
	public PlanSummary process(ArrayList<Chargeable> data) {
		PlanSummary ret = new PlanSummary(this);
		ret.addPlanCall(new PlanChargeable(new ChargeableMessage(ChargeableMessage.MESSAGE_MONTH_FEE), monthFee, this.getCurrency()));

		long secondsTotal = 0;
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
				} else if (this.isFavouriteContact(call.getContact())) {
					callPrice = 0;
				} else {
					secondsTotal += call.getDuration();
					if (secondsTotal > maxSecondsMonth) {
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
				smsSent++;
				ret.addPlanCall(new PlanChargeable(chargeable, (smsSent > maxFreeSMS)?smsPrice:0, this.getCurrency()));
			}
		}
		return ret;
	}
	
	@Override
	public PlanConfig getPlanConfig() {
		return this.planConfig;
	}
}