package com.conzebit.myplan.core.sms;

import java.util.Calendar;

import com.conzebit.myplan.core.Chargeable;
import com.conzebit.myplan.core.contact.Contact;
import com.conzebit.util.Formatter;

public class Sms extends Chargeable {

	public static final int SMS_TYPE_SENT = 0;
	public static final int SMS_TYPE_RECEIVED = 1;
	
	private int type;
	
	public Sms(int type, Contact contact, Calendar date) {
		this.type = type;
		this.contact = contact;
		this.date = date;
	}
	
	public int getChargeableType() {
		return CHARGEABLE_TYPE_SMS;
	}
	
	public int getType() {
		return this.type;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(Formatter.formatFullDate(this.date)).append(" - ");
		sb.append(this.contact).append(" - ");
		sb.append(this.type);
		return sb.toString();
	}
}
