package com.conzebit.myplan.android.store;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.provider.Contacts.People;
import android.provider.Contacts.Phones;

import com.conzebit.myplan.android.util.Settings;
import com.conzebit.myplan.core.contact.Contact;
import com.conzebit.myplan.core.msisdn.MsisdnType;
import com.conzebit.myplan.core.msisdn.MsisdnTypeService;

public class ContactStore {

	private static ContactStore instance=null;
	
	public static ContactStore getInstance(){
		if(instance==null){
			instance=new ContactStore();
		}
		return instance;
	}
	
	public ArrayList<Contact> getContacts(Context context){
		ArrayList<Contact> todosContactos= new ArrayList<Contact>();
		if(Settings.isTestMode(context)){
			MsisdnTypeService msisdnTypeService = MsisdnTypeService.getInstance();
			Contact alice = new Contact("601000000", "Alicia", msisdnTypeService.getMsisdnType("601000000", "ES"));
			Contact bob = new Contact("602000000", "Bob", msisdnTypeService.getMsisdnType("602000000", "ES"));
			Contact charly = new Contact("603000000", "Charly", msisdnTypeService.getMsisdnType("603000000", "ES"));
			Contact daniel = new Contact("604000000", "Daniel", msisdnTypeService.getMsisdnType("604000000", "ES"));
			Contact ebano = new Contact("605000000", "Esteban", msisdnTypeService.getMsisdnType("605000000", "ES"));
			Contact free = new Contact("123", null, msisdnTypeService.getMsisdnType("123", "ES"));
			Contact land = new Contact("910000000", null, msisdnTypeService.getMsisdnType("910000000", "ES"));
			todosContactos.add(alice);
			todosContactos.add(bob);
			todosContactos.add(charly);
			todosContactos.add(daniel);
			todosContactos.add(ebano);
			todosContactos.add(free);
			todosContactos.add(land);
		}else{
			final String[] PHONE_PROJECTION = new String[] {Phones.PERSON_ID, People.NAME, Phones.NUMBER};
			Cursor phoneCursor = context.getContentResolver().query(Phones.CONTENT_URI, PHONE_PROJECTION, null, null, null);
			try {
				if (phoneCursor.moveToFirst()) {
					do {
						int indexNumber = phoneCursor.getColumnIndex(Phones.NUMBER);
			            String number = phoneCursor.getString(indexNumber);
			        	int indexName = phoneCursor.getColumnIndex(People.NAME);
			            String name = phoneCursor.getString(indexName);
						Contact contact = new Contact(number, name, MsisdnTypeService.getInstance().getMsisdnType(number, "ES"));
						todosContactos.add(contact);
					} while (phoneCursor.moveToNext());
				}
			} finally {
				phoneCursor.close();
			}
		}
		return todosContactos;
		}
}
