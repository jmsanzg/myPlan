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
import java.util.Collections;
import java.util.Date;

import android.content.Context;

import com.conzebit.myplan.core.Chargeable;
import com.conzebit.myplan.core.call.Call;

public class LogStoreService {

	private static LogStoreService instance = null;
	
	private CallLogStore callLogStore;
	
	private SMSLogStore smsLogStore;
	
	private LogStoreService() {
		callLogStore = new CallLogStore();
		smsLogStore = new SMSLogStore();
	}
	
	public static LogStoreService getInstance() {
		if (instance == null) {
			instance = new LogStoreService();
		}
		return instance;
	}
	
	public ArrayList<Chargeable> get(Context context, Date startBillingDate, Date endBillingDate) {
		ArrayList<Chargeable> ret = callLogStore.getCalls(context, startBillingDate, endBillingDate);
		ret.addAll(smsLogStore.getSMS(context, startBillingDate, endBillingDate));
		Collections.sort(ret);
		return ret;
	}
	
	public Call getLastCall(Context context) {
		return callLogStore.getLastCall(context);
	}
}
