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
package com.conzebit.myplan.android.receiver;

import java.util.ArrayList;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;

import com.conzebit.myplan.R;
import com.conzebit.myplan.android.store.AndroidMsisdnTypeStore;
import com.conzebit.myplan.android.store.LogStoreService;
import com.conzebit.myplan.android.util.Settings;
import com.conzebit.myplan.core.Chargeable;
import com.conzebit.myplan.core.call.Call;
import com.conzebit.myplan.core.msisdn.MsisdnTypeService;
import com.conzebit.myplan.core.plan.PlanChargeable;
import com.conzebit.myplan.core.plan.PlanService;
import com.conzebit.myplan.core.plan.PlanSummary;
import com.conzebit.util.Formatter;

public class AfterCallReceiver extends BroadcastReceiver {

	private static boolean incomingCall = false;
	private static final int NOTIFICATION_ID = 1;
	
	@Override
    public void onReceive(Context context, Intent intent) {
    	if (!Settings.isTrapAfterCall(context)) {
    		return;
    	}
    	
   		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

    	Bundle extras = intent.getExtras();
		if (extras != null) {
			String state = extras.getString(TelephonyManager.EXTRA_STATE);
			if (TelephonyManager.EXTRA_STATE_RINGING.equals(state)) {
				incomingCall = true;
			} else if (TelephonyManager.EXTRA_STATE_IDLE.equals(state)) {
            	if (!incomingCall) {
            		// Wait 5 seconds to give some time to android to log last call
            		try {
            			synchronized (context) {
            				context.wait(5000);
            			}
            		} catch (Exception e) {}
                    AndroidMsisdnTypeStore androidMsisdnTypeStore = new AndroidMsisdnTypeStore(context);
            		MsisdnTypeService.getInstance(androidMsisdnTypeStore);
            		Call lastCall = LogStoreService.getInstance().getLastCall(context);
            		if (lastCall != null && lastCall.getDuration() > 0 && lastCall.getType() == Call.CALL_TYPE_SENT) {
            			notifyUser(context, notificationManager);
            		}
            	}
            	incomingCall = false;
			}
		}    	
    }
	
	private void notifyUser(Context context, NotificationManager notificationManager) {
		ArrayList<Chargeable> data = LogStoreService.getInstance().get(context, Settings.getBillingStartDate(context), Settings.getBillingEndDate(context));
		String operator = Settings.getOperator(context);
		String planName = Settings.getMyPlan(context);
		PlanService planService = PlanService.getInstance();
		PlanSummary summary = planService.process(data, operator, planName);
		PlanChargeable last = null;
		//long totalDuration = 0;
		//long totalCalls = 0;
		
		if (summary != null) {
			for (PlanChargeable planChargeable : summary.getPlanCalls()) {
				if (planChargeable.getChargeable() != null && planChargeable.getChargeable().getChargeableType() == Chargeable.CHARGEABLE_TYPE_CALL) {
					//totalCalls++;
					//Call call = (Call) planChargeable.getChargeable();
					//totalDuration += call.getDuration();
					last = planChargeable;
				}
			}
			//totalDuration = (long) totalDuration / 60; // show the value in minutes
			String lastCallPrice = Formatter.formatDecimal(last.getPrice()) + " " + last.getCurrency();
			String totalPrice = Formatter.formatDecimal(summary.getTotalPrice()) + " " + last.getCurrency();
			String text = lastCallPrice + " / " + totalPrice;
			
			Notification notification = new Notification(R.drawable.app_icon, text, System.currentTimeMillis());
			notification.flags = Notification.FLAG_AUTO_CANCEL;
			
			
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(), 0);
			
			notification.setLatestEventInfo(context, context.getString(R.string.app_name), text, pendingIntent);
			notificationManager.notify(NOTIFICATION_ID, notification);
		}

		boolean vat = Settings.isVAT(context);
		String lastCallPrice = Formatter.formatDecimal(last.getPrice() * (vat?Settings.getVATValue():1)) + " " + last.getCurrency();
		String totalPrice = Formatter.formatDecimal(summary.getTotalPrice() * (vat?Settings.getVATValue():1)) + " " + last.getCurrency();
		String text = lastCallPrice + " / " + totalPrice;
		
		Notification notification = new Notification(R.drawable.app_icon, text, System.currentTimeMillis());
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		
		
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(), 0);
		
		notification.setLatestEventInfo(context, context.getString(R.string.app_name), text, pendingIntent);
		notificationManager.notify(NOTIFICATION_ID, notification);
	}
	
}