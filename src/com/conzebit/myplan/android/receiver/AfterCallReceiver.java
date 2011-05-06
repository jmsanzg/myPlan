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