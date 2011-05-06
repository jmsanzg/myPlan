package com.conzebit.myplan.core.message;

import com.conzebit.myplan.core.Chargeable;

public class ChargeableMessage extends Chargeable {

	public final static String MESSAGE_MONTH_FEE = "plan_month_fee";
	public final static String MESSAGE_MINIMUM_MONTH_FEE = "plan_minimum_month_fee";
	
	private String message;
	
	public ChargeableMessage(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}

	@Override
    public int getChargeableType() {
	    return CHARGEABLE_TYPE_MESSAGE;
    }

}
