package com.conzebit.myplan.ext.es.movistar.particulares;

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
import com.conzebit.myplan.ext.es.movistar.ESMovistar;

public class ESMovistarMiGente extends ESMovistar{

	private double initialPrice = 0.15;
	
	private double minimumMonthFee = 9;
	
	private double yoigoExtraCharge=0.03/60;
	
	private double normalSMSPrice=0.15;
	
	private double normalPricePerSecond=0.25/60;
	
	private double favouriteSMSPrice=0.1;
	
	private double favouritePricePerSecond=0.05/60;
	
	private PlanConfig planConfig;
	
	private final static String CONFIG_KEY ="es.movistar.migente.favourites";
	
	public ESMovistarMiGente(){
		this.planConfig= new PlanConfig();
		PlanConfigElement<String> pce = new PlanConfigElement<String>(CONFIG_KEY, "", "", "", String.class);
		this.planConfig.addPlanConfigElement(pce);
	}
	
	@Override
	public String getPlanName() {
		return "Movistar Mi Gente";
	}

	@Override
	public String getPlanURL() {
		return "http://www.tarifas.movistar.es/particulares/voz/contratomigente";
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
	
	@Override
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
	
				if (call.getContact().getMsisdnType() == MsisdnType.ES_SPECIAL_ZER0) {
					callPrice = 0;
				} else {
					double pricePerSecond = this.isFavouriteContact(call.getContact())?this.favouritePricePerSecond:this.normalPricePerSecond;
					double extraCharge=call.getContact().getMsisdnType()==MsisdnType.ES_YOIGO?this.yoigoExtraCharge:0;
					callPrice = initialPrice + (call.getDuration() * (pricePerSecond + extraCharge ));
				}
				globalPrice += callPrice;
				ret.addPlanCall(new PlanChargeable(call, callPrice, this.getCurrency()));
			} else if (chargeable.getChargeableType() == Chargeable.CHARGEABLE_TYPE_SMS) {
				Sms sms = (Sms) chargeable;
				if (sms.getType() == Sms.SMS_TYPE_RECEIVED) {
					continue;
				}
				double smsPrice = this.isFavouriteContact(sms.getContact())?this.favouriteSMSPrice:this.normalSMSPrice;
				globalPrice += smsPrice;
				ret.addPlanCall(new PlanChargeable(chargeable, smsPrice, this.getCurrency()));
			}
		}
		if (globalPrice < minimumMonthFee) {
			ret.addPlanCall(new PlanChargeable(new ChargeableMessage(ChargeableMessage.MESSAGE_MINIMUM_MONTH_FEE), minimumMonthFee - globalPrice, this.getCurrency()));
		}
		return ret;
	}

	@Override
	public PlanConfig getPlanConfig() {
		return this.planConfig;
	}
	
	
}
