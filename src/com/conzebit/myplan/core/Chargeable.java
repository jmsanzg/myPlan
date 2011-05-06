package com.conzebit.myplan.core;

import java.util.Calendar;

import com.conzebit.myplan.core.contact.Contact;

public abstract class Chargeable implements Comparable<Chargeable> {

	public static final int CHARGEABLE_TYPE_CALL = 0;
	public static final int CHARGEABLE_TYPE_SMS = 1;
	public static final int CHARGEABLE_TYPE_MESSAGE = 2;
	
    public abstract int getChargeableType();
    
	protected Calendar date;
	
	protected Contact contact;
    
	public Calendar getDate() {
		return this.date;
	}
    
	public Contact getContact() {
		return this.contact;
	}
	
    public int compareTo(Chargeable o) {
		if (this.date == null) {
			return 1;
		}
		if (o.date == null) {
			return -1;
		}
		
		return this.date.compareTo(o.date);
    }
}
