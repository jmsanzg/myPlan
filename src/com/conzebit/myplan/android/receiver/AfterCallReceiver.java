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
package com.conzebit.myplan.android.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import com.conzebit.myplan.android.activity.AfterCallActivity;
import com.conzebit.myplan.android.store.AndroidMsisdnTypeStore;
import com.conzebit.myplan.android.store.LogStoreService;
import com.conzebit.myplan.android.util.Settings;
import com.conzebit.myplan.core.call.Call;
import com.conzebit.myplan.core.msisdn.MsisdnTypeService;

public class AfterCallReceiver extends BroadcastReceiver {

	private static boolean incomingCall = false;
	
	@Override
    public void onReceive(Context context, Intent intent) {
    	if (!Settings.isTrapAfterCall(context)) {
    		return;
    	}
		
    	TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    	
    	switch(tm.getCallState()) {
    		case TelephonyManager.CALL_STATE_OFFHOOK:
    			break;
    		case TelephonyManager.CALL_STATE_IDLE:
            	if (!incomingCall) {
            		AndroidMsisdnTypeStore androidMsisdnTypeStore = new AndroidMsisdnTypeStore(context);
            		MsisdnTypeService.getInstance(androidMsisdnTypeStore);
            		Call lastCall = LogStoreService.getInstance().getLastCall(context);
            		if (lastCall != null && lastCall.getDuration() > 0 && lastCall.getType() == Call.CALL_TYPE_SENT) {
	            		Intent i = new Intent(context, AfterCallActivity.class);
	            		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
	            		context.startActivity(i);
            		}
            	}
            	incomingCall = false;
            	break;
    		case TelephonyManager.CALL_STATE_RINGING:
            	incomingCall = true;
            	break;
    	}
    }
}