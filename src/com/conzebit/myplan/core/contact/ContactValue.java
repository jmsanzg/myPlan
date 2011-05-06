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
