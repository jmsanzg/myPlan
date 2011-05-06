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
package com.conzebit.myplan.android.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;

import android.app.Activity;

import com.conzebit.myplan.R;
import com.conzebit.myplan.android.store.LogStoreService;
import com.conzebit.myplan.core.Chargeable;
import com.conzebit.myplan.core.call.Call;
import com.conzebit.myplan.core.call.CallStat;
import com.conzebit.myplan.core.contact.Contact;
import com.conzebit.myplan.core.contact.ContactValue;
import com.conzebit.util.Formatter;

public class StatisticsHelper {

	private static ArrayList<Chargeable> calls = null;
	public static ArrayList<CallStat> getStatisticsData(Activity activity, Date startBillingDate, Date endBillingDate, int order) {
		if (calls == null) {
			calls = LogStoreService.getInstance().get(activity, startBillingDate, endBillingDate);
		}
		
		switch (order) {
			case 0:
				return getSummary(activity);
			case 1:
				return getTopCallersByCalls();
			case 2:
				return getTopCallersByMinutes();
		}

		ArrayList<CallStat> ret = new ArrayList<CallStat>();
		ret.add(new CallStat(activity.getString(R.string.stats_menu_general_data), ""));
		ret.add(new CallStat(activity.getString(R.string.stats_menu_top_caller_calls), ""));
		ret.add(new CallStat(activity.getString(R.string.stats_menu_top_caller_duration), ""));
		return ret;
	}
	
	/**
	 * Obtain call statistics
	 * 
	 * @param activity
	 *            caller activity
	 * @param startBillingDay
	 *            starting billing day
	 * @return ArrayList with all calls made
	 */
	private static ArrayList<CallStat> getSummary(Activity activity) {
        
        
        long totalMissed = 0;
        long totalAnswered = 0;
        long totalInbound = 0;
        long totalOutbound = 0;
        long totalCalls = 0;
        
        long totalInboundSeconds = 0;
        long totalOutboundSeconds = 0;
        
        long shortestCallSeconds = -1;
        long longestCallSeconds = -1;
        long averageCallSeconds = 0;
        
        long callsToCountAverage = 0;
        
        for (Chargeable chargeable : calls) {
        	Call call = (Call) chargeable;
        	long duration = call.getDuration();
        	totalCalls++;
        	if (longestCallSeconds == -1 || duration > longestCallSeconds) {
        		longestCallSeconds = duration;
        	}
        	if (duration > 0 && (shortestCallSeconds == -1 || duration < shortestCallSeconds)) {
        		shortestCallSeconds = duration;
        	}
        	switch (call.getType()) {
	        	case Call.CALL_TYPE_RECEIVED:
	        		totalInbound++;
	        		totalAnswered++;
	        		totalInboundSeconds += duration;
	        		averageCallSeconds += duration;
	        		callsToCountAverage++;
	        		break;
	        	case Call.CALL_TYPE_RECEIVED_MISSED:
	        		totalInbound++;
	        		totalMissed++;
	        		break;
	        	case Call.CALL_TYPE_SENT:
	        		totalOutbound++;
	        		totalAnswered++;
	        		totalOutboundSeconds += duration;
	        		averageCallSeconds += duration;
	        		callsToCountAverage++;
	        		break;
	        	case Call.CALL_TYPE_SENT_MISSED:
	        		totalOutbound++;
	        		totalMissed++;
	        		break;
        	}
        }
        
        if (callsToCountAverage > 0) {
        	averageCallSeconds = averageCallSeconds / callsToCountAverage;
        }
        
        ArrayList<CallStat> ret = new ArrayList<CallStat>();
        ret.add(new CallStat(activity.getString(R.string.stats_missed_calls), "" + totalMissed));
        ret.add(new CallStat(activity.getString(R.string.stats_answered_calls), "" + totalAnswered));
        ret.add(new CallStat(activity.getString(R.string.stats_inbound_calls), "" + totalInbound));
        ret.add(new CallStat(activity.getString(R.string.stats_outbound_calls), "" + totalOutbound));
        ret.add(new CallStat(activity.getString(R.string.stats_total_calls), "" + calls.size()));

        ret.add(new CallStat(activity.getString(R.string.stats_inbound_seconds), Formatter.formatDurationAsString(totalInboundSeconds)));
        ret.add(new CallStat(activity.getString(R.string.stats_outbound_seconds), Formatter.formatDurationAsString(totalOutboundSeconds)));
        
        ret.add(new CallStat(activity.getString(R.string.stats_shortest_call), Formatter.formatDurationAsString(shortestCallSeconds)));
        ret.add(new CallStat(activity.getString(R.string.stats_longest_call), Formatter.formatDurationAsString(longestCallSeconds)));
        ret.add(new CallStat(activity.getString(R.string.stats_average_call), Formatter.formatDurationAsString(averageCallSeconds)));
        return ret;
	}

	/**
	 * Obtain top callers by minutes
	 * 
	 * @param activity
	 *            caller activity
	 * @param startBillingDay
	 *            starting billing day
	 * @return ArrayList with all calls made
	 */
	private static ArrayList<CallStat> getTopCallersByMinutes() {

        LinkedList<ContactValue> data = new LinkedList<ContactValue>();
        
        for (Chargeable chargeable : calls) {
        	Call call = (Call) chargeable;
        	Contact contact = call.getContact();
        	if (data.contains(contact)) {
        		ContactValue contactValue = data.get(data.indexOf(contact));
        		contactValue.incValue(call.getDuration());
        	} else {
        		ContactValue contactValue = new ContactValue(contact, call.getDuration());
        		data.add(contactValue);
        	}
        }
        
        Collections.sort(data);

        ArrayList<CallStat> ret = new ArrayList<CallStat>();
        int i = 0;
        for (ContactValue cv : data) {
        	ret.add(new CallStat(cv.getContact().getContactName(), Formatter.formatDurationAsString(cv.getValue())));
        	i++;
        	if (i > 20) {
        		break;
        	}
        }
        return ret;
	}
	
	/**
	 * Obtain top callers by calls.
	 * 
	 * @param activity
	 *            caller activity
	 * @param startBillingDay
	 *            starting billing day
	 * @return ArrayList with all calls made
	 */
	private static ArrayList<CallStat> getTopCallersByCalls() {

        LinkedList<ContactValue> data = new LinkedList<ContactValue>();
        
        for (Chargeable chargeable : calls) {
        	Call call = (Call) chargeable;
        	Contact contact = call.getContact();
        	if (data.contains(contact)) {
        		ContactValue contactValue = data.get(data.indexOf(contact));
        		contactValue.incValue((long)1);
        	} else {
        		ContactValue contactValue = new ContactValue(contact, (long)1);
        		data.add(contactValue);
        	}
        }
        
        Collections.sort(data);

        ArrayList<CallStat> ret = new ArrayList<CallStat>();
        int i = 0;
        for (ContactValue cv : data) {
        	ret.add(new CallStat(cv.getContact().getContactName(), String.valueOf(cv.getValue())));
        	i++;
        	if (i > 20) {
        		break;
        	}
        }
        return ret;
	}
}