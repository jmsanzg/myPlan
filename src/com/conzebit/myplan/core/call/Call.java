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
package com.conzebit.myplan.core.call;

import java.util.Calendar;

import com.conzebit.myplan.core.Chargeable;
import com.conzebit.myplan.core.contact.Contact;
import com.conzebit.util.Formatter;


/**
 * Represents a call made or received by the user
 * @author jmsanzg@gmail.com
 */
public class Call extends Chargeable {

	public static final int CALL_TYPE_RECEIVED = 0;
	public static final int CALL_TYPE_RECEIVED_MISSED = 1;
	public static final int CALL_TYPE_SENT = 2;
	public static final int CALL_TYPE_SENT_MISSED = 3;
	
	private int type;
	
	private long duration;
	
	public Call(int type, Long duration, Contact contact, Calendar date) {
		this.type = type;
		this.duration = duration;
		this.contact = contact;
		this.date = date;
	}
	
	public int getChargeableType() {
		return CHARGEABLE_TYPE_CALL;
	}
	
	public int getType() {
		return this.type;
	}
	
	public long getDuration() {
		return this.duration;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(Formatter.formatFullDate(this.date)).append(" - ");
		sb.append(this.contact).append(" - ");
		sb.append(this.type).append(" - ");
		sb.append(Formatter.formatDuration(this.duration)).append(" - ");
		return sb.toString();
	}
}