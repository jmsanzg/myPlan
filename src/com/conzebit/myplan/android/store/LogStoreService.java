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
