package com.conzebit.myplan.core.plan;

import com.conzebit.myplan.core.Chargeable;

public class PlanChargeable {

	private Chargeable chargeable;
	private double price;
	private String currency;
	
	public PlanChargeable(Chargeable chargeable, double price, String currency) {
		this.chargeable = chargeable;
		this.price = price;
		this.currency = currency;
	}
	
	public Chargeable getChargeable() {
		return this.chargeable;
	}
	
	public double getPrice() {
		return this.price;
	}
	
	public String getCurrency() {
		return this.currency;
	}
	
}
