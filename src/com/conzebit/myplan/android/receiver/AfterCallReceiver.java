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
import com.conzebit.myplan.android.activity.PlanDetailActivity;
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
	private static NotificationManager notificationManager = null;
	
	@Override
    public void onReceive(Context context, Intent intent) {
    	if (!Settings.isTrapAfterCall(context)) {
    		return;
    	}
    	
    	if (notificationManager == null) {
    		notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);;
    	}

    	Bundle extras = intent.getExtras();
		if (extras != null) {
			String state = extras.getString(TelephonyManager.EXTRA_STATE);
			if (TelephonyManager.EXTRA_STATE_RINGING.equals(state)) {
				incomingCall = true;
			} else if (TelephonyManager.EXTRA_STATE_IDLE.equals(state)) {
            	if (!incomingCall) {
            		AndroidMsisdnTypeStore androidMsisdnTypeStore = new AndroidMsisdnTypeStore(context);
            		MsisdnTypeService.getInstance(androidMsisdnTypeStore);
            		Call lastCall = LogStoreService.getInstance().getLastCall(context);
            		if (lastCall != null && lastCall.getDuration() > 0 && lastCall.getType() == Call.CALL_TYPE_SENT) {
            			notifyUser(context);
            		}
            	}
            	incomingCall = false;
			}
		}    	
    }
	
	private void notifyUser(Context context) {
		ArrayList<Chargeable> data = LogStoreService.getInstance().get(context, Settings.getBillingStartDate(context), Settings.getBillingEndDate(context));
		String operator = Settings.getOperator(context);
		String planName = Settings.getMyPlan(context);
		PlanService planService = PlanService.getInstance();
		PlanSummary summary = planService.process(data, operator, planName);
		ArrayList<PlanChargeable> planCalls = summary.getPlanCalls();
		PlanChargeable last = null;
		for (int i = planCalls.size() - 1; i >= 0; i--) {
			last = summary.getPlanCalls().get(i);
			if (last.getChargeable() != null && last.getChargeable().getChargeableType() == Chargeable.CHARGEABLE_TYPE_CALL) {
				break;
			}
		}
		String lastCallPrice = Formatter.formatDecimal(last.getPrice()) + " " + last.getCurrency();
		String totalPrice = Formatter.formatDecimal(summary.getTotalPrice()) + " " + last.getCurrency();
		String text = lastCallPrice + " / " + totalPrice;
		Notification notification = new Notification(R.drawable.app_icon, text, System.currentTimeMillis());
		
		Intent notificationIntent = new Intent(context, PlanDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("operator", operator);
        bundle.putString("planname", planName);
        notificationIntent.putExtras(bundle);
		
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
		notification.setLatestEventInfo(context, context.getString(R.string.app_name), text, contentIntent);
		notificationManager.notify(NOTIFICATION_ID, notification);
	}
	
}