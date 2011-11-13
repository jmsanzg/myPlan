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
package com.conzebit.myplan.android.store;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;

import com.conzebit.myplan.android.util.Settings;
import com.conzebit.myplan.core.Chargeable;
import com.conzebit.myplan.core.call.Call;
import com.conzebit.myplan.core.contact.Contact;
import com.conzebit.myplan.core.msisdn.MsisdnTypeService;

class CallLogStore {
	
	/**
	 * Obtain all calls since last invoice
	 * 
	 * @param activity
	 *            caller activity
	 * @param startBillingDay
	 *            starting billing day
	 * @return ArrayList with all calls made
	 */
	public ArrayList<Chargeable> getCalls(Context context, Date startBillingDate, Date endBillingDate) {

		MsisdnTypeService msisdnTypeService = MsisdnTypeService.getInstance();
		
		if (Settings.isTestMode(context)) {
			return getTestCalls(msisdnTypeService);
		}

		ArrayList<Chargeable> calls = new ArrayList<Chargeable>();

		String[] projection = {
				CallLog.Calls.TYPE,
				CallLog.Calls.NUMBER,
		        CallLog.Calls.CACHED_NAME,
		        CallLog.Calls.DURATION,
		        CallLog.Calls.DATE};
		
		String selection = null;
		if (startBillingDate != null) {
			selection = CallLog.Calls.DATE + " > " + startBillingDate.getTime();
			if (endBillingDate != null) {
				selection = selection + " AND " + CallLog.Calls.DATE + " < " + endBillingDate.getTime();
			}
		}
		
		Cursor cursor = context.getContentResolver().query(
		        CallLog.Calls.CONTENT_URI, projection, selection, null,
		        CallLog.Calls.DATE + " DESC");

		int typeColumn = cursor.getColumnIndex(CallLog.Calls.TYPE);
		int numberColumn = cursor.getColumnIndex(CallLog.Calls.NUMBER);
		int contactColumn = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
		int durationColumn = cursor.getColumnIndex(CallLog.Calls.DURATION);
		int dateColumn = cursor.getColumnIndex(CallLog.Calls.DATE);

		try {
			if (cursor.moveToFirst()) {
				do {
					int callType;
					switch (cursor.getInt(typeColumn)) {
					case CallLog.Calls.OUTGOING_TYPE:
						callType = Call.CALL_TYPE_SENT;
						break;
					case CallLog.Calls.INCOMING_TYPE:
						callType = Call.CALL_TYPE_RECEIVED;
						break;
					case CallLog.Calls.MISSED_TYPE:
						callType = Call.CALL_TYPE_RECEIVED_MISSED;
						break;
					default:
						continue;
					}
					String contactName = cursor.getString(contactColumn);
					String callNumber = cursor.getString(numberColumn);
					Long callDuration = cursor.getLong(durationColumn);
					if (callDuration == 0 && callType == Call.CALL_TYPE_SENT) {
						callType = Call.CALL_TYPE_SENT_MISSED;
					}
					Calendar callCal = Calendar.getInstance();
					callCal.setTimeInMillis(cursor.getLong(dateColumn));
					
					Contact contact = new Contact(callNumber, contactName, msisdnTypeService.getMsisdnType(callNumber, "ES"));
					Call call = new Call(callType,
							callDuration,
							contact,
					        callCal);
					calls.add(call);
	
				} while (cursor.moveToNext());
			}
		} finally {
			cursor.close();
		}
		return calls;
	}

	/**
	 * Obtain last call since last invoice
	 * 
	 * @param call
	 *            caller context
	 * @return ArrayList with all calls made
	 */
	public Call getLastCall(Context context) {

		MsisdnTypeService msisdnTypeService = MsisdnTypeService.getInstance();
		
		String[] projection = { CallLog.Calls.TYPE, CallLog.Calls.NUMBER,
		        CallLog.Calls.CACHED_NAME, CallLog.Calls.DURATION,
		        CallLog.Calls.DATE };

		Cursor cursor = context.getContentResolver().query(
		        CallLog.Calls.CONTENT_URI, projection, null, null,
		        CallLog.Calls.DATE + " DESC");

		int typeColumn = cursor.getColumnIndex(CallLog.Calls.TYPE);
		int numberColumn = cursor.getColumnIndex(CallLog.Calls.NUMBER);
		int contactColumn = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
		int durationColumn = cursor.getColumnIndex(CallLog.Calls.DURATION);
		int dateColumn = cursor.getColumnIndex(CallLog.Calls.DATE);

		Call call = null;
		try {
			if (cursor.moveToFirst()) {
				int callType;
				switch (cursor.getInt(typeColumn)) {
				case CallLog.Calls.OUTGOING_TYPE:
					callType = Call.CALL_TYPE_SENT;
					break;
				case CallLog.Calls.INCOMING_TYPE:
					callType = Call.CALL_TYPE_RECEIVED;
					break;
				case CallLog.Calls.MISSED_TYPE:
					callType = Call.CALL_TYPE_RECEIVED_MISSED;
					break;
				default:
					return null;
				}
				String contactName = cursor.getString(contactColumn);
				String callNumber = cursor.getString(numberColumn);
				Long callDuration = cursor.getLong(durationColumn);
				if (callDuration == 0 && callType == Call.CALL_TYPE_SENT) {
					callType = Call.CALL_TYPE_SENT_MISSED;
				}
				Calendar callCal = Calendar.getInstance();
				callCal.setTimeInMillis(cursor.getLong(dateColumn));
				
				Contact contact = new Contact(callNumber, contactName, msisdnTypeService.getMsisdnType(callNumber, "ES"));
				call = new Call(callType,
						callDuration,
						contact,
				        callCal);
			}
		} finally {
			cursor.close();
		}
		return call;
	}
	
	/**
	 * Test calls
	 * @return List of test calls
	 */
	private ArrayList<Chargeable> getTestCalls(MsisdnTypeService msisdnTypeService) {
		ArrayList<Chargeable> calls = new ArrayList<Chargeable>();
		Contact alice = new Contact("601000000", "Alicia", msisdnTypeService.getMsisdnType("601000000", "ES"));
		Contact bob = new Contact("602000000", "Bob", msisdnTypeService.getMsisdnType("602000000", "ES"));
		Contact charly = new Contact("603000000", "Charly", msisdnTypeService.getMsisdnType("603000000", "ES"));
		Contact daniel = new Contact("604000000", "Daniel", msisdnTypeService.getMsisdnType("604000000", "ES"));
		Contact ebano = new Contact("605000000", "Esteban", msisdnTypeService.getMsisdnType("605000000", "ES"));
		Contact free = new Contact("123", null, msisdnTypeService.getMsisdnType("123", "ES"));
		Contact land = new Contact("910000000", null, msisdnTypeService.getMsisdnType("910000000", "ES"));
		
		calls.add(new Call(
				Call.CALL_TYPE_SENT,
				(long) (7 * 60 + 34),
		        alice,
		        getTestDate(2009, 8, 3, 20, 58)));
		calls.add(new Call(
				Call.CALL_TYPE_SENT,
				(long) (2 * 60 + 39),
		        land,
		        getTestDate(2009, 8, 7, 20, 31)));
		calls.add(new Call(
				Call.CALL_TYPE_SENT,
				(long) (3 * 60 + 58),
				bob,
		        getTestDate(2009, 8, 8, 23, 32)));
		calls.add(new Call(
				Call.CALL_TYPE_SENT,
				(long) (1 * 60 + 41),
		        daniel,
		        getTestDate(2009, 8, 10, 18, 36)));
		calls.add(new Call(
				Call.CALL_TYPE_SENT,
				(long) (1 * 60 + 0),
		        charly,
		        getTestDate(2009, 8, 11, 19, 48)));
		calls.add(new Call(
				Call.CALL_TYPE_SENT,
				(long) (1 * 60 + 0),
		        daniel,
		        getTestDate(2009, 8, 15, 18, 8)));
		calls.add(new Call(
				Call.CALL_TYPE_SENT,
				(long) (1 * 60 + 0),
		        ebano,
		        getTestDate(2009, 8, 15, 22, 48)));
		calls.add(new Call(
				Call.CALL_TYPE_SENT,
				(long) (1 * 60 + 0),
		        ebano,
		        getTestDate(2009, 8, 16, 18, 23)));
		calls.add(new Call(
				Call.CALL_TYPE_SENT,
				(long) (0 * 60 + 20),
		        daniel,
		        getTestDate(2009, 8, 17, 11, 52)));
		calls.add(new Call(
				Call.CALL_TYPE_SENT,
				(long) (1 * 60 + 0),
		        daniel,
		        getTestDate(2009, 8, 17, 21, 56)));
		calls.add(new Call(
				Call.CALL_TYPE_SENT,
				(long) (3 * 60 + 13),
		        alice,
		        getTestDate(2009, 8, 18, 21, 01)));
		calls.add(new Call(
				Call.CALL_TYPE_SENT,
				(long) (6 * 60 + 48),
		        charly,
		        getTestDate(2009, 8, 20, 20, 45)));
		calls.add(new Call(
				Call.CALL_TYPE_SENT,
				(long) (6 * 60 + 23),
				bob,
		        getTestDate(2009, 8, 21, 21, 53)));
		calls.add(new Call(
				Call.CALL_TYPE_SENT,
				(long) (1 * 60 + 0),
				alice,
		        getTestDate(2009, 8, 23, 12, 13)));
		calls.add(new Call(
				Call.CALL_TYPE_SENT,
				(long) (1 * 60 + 49),
		        ebano,
		        getTestDate(2009, 8, 23, 12, 24)));
		calls.add(new Call(
				Call.CALL_TYPE_SENT,
				(long) (4 * 60 + 4),
				alice,
		        getTestDate(2009, 8, 27, 21, 4)));
		calls.add(new Call(
				Call.CALL_TYPE_SENT,
				(long) (4 * 60 + 4),
		        free,
		        getTestDate(2009, 8, 27, 21, 4)));
		return calls;
	}

	private Calendar getTestDate(int year, int month, int date, int hourOfDay, int minute) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month - 1, date, hourOfDay, minute);
		return calendar;
	}

}
