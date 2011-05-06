package com.conzebit.myplan.android.store;

import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.conzebit.myplan.core.msisdn.IMsisdnTypeStore;
import com.conzebit.myplan.core.msisdn.MsisdnType;
import com.conzebit.util.StringUtil;

public class AndroidMsisdnTypeStore implements IMsisdnTypeStore {
	
	private final static String MSISDN_PREFERENCE = "msisdn_preference";
	
	private SharedPreferences sharedPreferences;
	private HashMap<String, MsisdnType> map = new HashMap<String, MsisdnType>(); 
	
	public AndroidMsisdnTypeStore(Context context) {
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		this.load();
	}
	
	private void load() {
		String data = sharedPreferences.getString(MSISDN_PREFERENCE, "");
		if (!StringUtil.isEmpty(data)) {
			try {
				String[] splitArray = data.split(",");
				for (String split: splitArray) {
					String []keyValue = split.split("=");
					map.put(keyValue[0], MsisdnType.fromString(keyValue[1]));
				}
			} catch (Exception e) {
				// shit! need to reset
				SharedPreferences.Editor editor = this.sharedPreferences.edit();
				editor.putString(MSISDN_PREFERENCE, "");
				editor.commit();
			}
		}
	}
	
	private void store() {
		StringBuffer sb = new StringBuffer();
		for (String key : map.keySet()) {
			sb.append(key).append("=").append(map.get(key)).append(",");
		}
		String data = sb.toString();
		data.substring(0, data.length());
		SharedPreferences.Editor editor = this.sharedPreferences.edit();
		editor.putString(MSISDN_PREFERENCE, data);
		editor.commit();
	}
	
	public MsisdnType getMsisdnType(String msisdn) {
	    return map.get(msisdn);
    }

	public void setMsisdnType(String msisdn, MsisdnType msisdnType) {
	    map.put(msisdn, msisdnType);
	    this.store();
    }

}
