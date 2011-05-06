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
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.conzebit.myplan.core.contact;

import com.conzebit.myplan.core.msisdn.MsisdnType;


/**
 * Represents a contact
 * @author jmsanzg@gmail.com
 */
public class Contact {

	private String msisdn;
	
	private String contactName;
	
	private MsisdnType msisdnType;

	public Contact(String msisdn, String contactName, MsisdnType msisdnType) {
		this.msisdn = msisdn;
		this.contactName = contactName;
		this.msisdnType = msisdnType;

		if (this.contactName == null) {
			this.contactName = this.msisdn;
		}
	}
	
	public String getMsisdn() {
		return this.msisdn;
	}
	
	public String getContactName() {
		return this.contactName;
	}
	
	public MsisdnType getMsisdnType() {
		return this.msisdnType;
	}
	
	public void setMsisdnType(MsisdnType msisdnType) {
		this.msisdnType = msisdnType;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.msisdn).append(" - ");
		sb.append(this.msisdnType).append(" - ");
		sb.append(this.contactName).append(" - ");
		return sb.toString();
	}
	
	public boolean equals(Object another) {
		Contact anotherContact = null;
		if (another instanceof ContactValue) {
			anotherContact = ((ContactValue) another).getContact();
		}
		return this.getMsisdn().equals(anotherContact.getMsisdn());
	}
	
}