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
package com.conzebit.myplan.core.contact;


public class ContactValue implements Comparable<ContactValue> {

	private Contact contact;
	private Long value;
	
	public ContactValue(Contact contact, Long value) {
		this.contact = contact;
		this.value = value;
	}
	
	public int compareTo(ContactValue another) {
        return this.value.compareTo(another.value) * -1;
    }
	
	public boolean equals(Object another) {
		Contact anotherContact = null;
		if (another instanceof Contact) {
			anotherContact = (Contact) another;
		}
		return this.contact.getMsisdn().equals(anotherContact.getMsisdn());
	}
	
	public void incValue(Long inc) {
		this.value = this.value + inc;
	}
	
	public Long getValue() {
		return this.value;
	}
	
	public Contact getContact() {
		return this.contact;
	}
}
