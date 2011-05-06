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
package com.conzebit.myplan.android.util;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

import com.conzebit.myplan.R;
import com.conzebit.util.StringUtil;

public class Settings {

	public enum Setting {
		BILLINGDAY("billingday_preference"),
        OPERATOR("operator_preference"),
		MYPLAN("myplan_preference"),
		BILLING_START_DATE("billing_start_date"),
		BILLING_END_DATE("billing_end_date"),
		TERMS_AND_CONDITIONS_SHOWN("terms_and_conditions_shown"),
		PLANS_SORTING("plans_sorting"),
		COUNT_SMS("count_sms"),
		VAT("vat"),
		TRAP_AFTER_CALL("trap_after_call"),
		APP_VERSION("app_version"),
		CRASH_REPORT_DISABLED("acra.disable"),
		PLAN_CONFIG("plan.config"),
		SHOW_PLANS_CONFIG("show.plans.config");

		private String value;
		private Setting(String value) {
			this.value = value;
		}

		public String toString() {
			return value;
		}
	}
	
    public static boolean isMandatoryConfigSet(Context context) {
    	String billingDay = getBillingDay(context);
    	String operator = getOperator(context);
    	String myPlan = getMyPlan(context);
    	return StringUtil.isNotEmpty(billingDay) && StringUtil.isNotEmpty(operator) && StringUtil.isNotEmpty(myPlan);
    }

    public static Boolean isVAT(Context context) {
    	return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(Setting.VAT.toString(), false);
    }

    public static Boolean isCountSms(Context context) {
    	return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(Setting.COUNT_SMS.toString(), true);
    }
    
    public static String getBillingDay(Context context) {
    	return PreferenceManager.getDefaultSharedPreferences(context).getString(Setting.BILLINGDAY.toString(), "1");
    }

    public static String getOperator(Context context) {
    	return PreferenceManager.getDefaultSharedPreferences(context).getString(Setting.OPERATOR.toString(), "");
    }
    
    public static String getMyPlan(Context context) {
    	return PreferenceManager.getDefaultSharedPreferences(context).getString(Setting.MYPLAN.toString(), "");
    }
    
    public static Date getBillingStartDate(Context context) {
		Integer startBillingDay = Integer.valueOf(getBillingDay(context));
		Calendar ret = Calendar.getInstance();
		if (ret.get(Calendar.DAY_OF_MONTH) < startBillingDay) {
			ret.add(Calendar.MONTH, -1);
		}
		ret.set(Calendar.DAY_OF_MONTH, startBillingDay);
		ret.set(Calendar.HOUR_OF_DAY, 0);
		ret.set(Calendar.MINUTE, 0);
		ret.set(Calendar.SECOND, 0);
		ret.set(Calendar.MILLISECOND, 0);
		return ret.getTime();
    }
    
    public static Date getBillingEndDate(Context context) {
		Calendar now = Calendar.getInstance();
		Calendar ret = Calendar.getInstance();
		ret.setTime(getBillingStartDate(context));
		ret.add(Calendar.MONTH, 1);
		if (ret.after(now)) {
			return now.getTime();
		} else {
			ret.set(Calendar.HOUR_OF_DAY, 24);
			ret.set(Calendar.MINUTE, 59);
			ret.set(Calendar.SECOND, 59);
			ret.set(Calendar.MILLISECOND, 999);
			return ret.getTime();
		}
    }
    
    public static void previousMonth(Context context) {
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(Settings.getCurrentlyViewingStartDate(context));
		cal.add(Calendar.MONTH, -1);
		Settings.setCurrentlyViewingStartDate(context, cal.getTime());
		
		cal.add(Calendar.MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.add(Calendar.MILLISECOND, -1);
		Settings.setCurrentlyViewingEndDate(context, cal.getTime());
    }
    
    public static boolean nextMonth(Context context) {
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(Settings.getCurrentlyViewingStartDate(context));
		cal2.add(Calendar.MONTH, 1);
		Settings.setCurrentlyViewingStartDate(context, cal2.getTime());
		cal2.add(Calendar.MONTH, 1);
		cal2.set(Calendar.HOUR, 23);
		cal2.set(Calendar.MINUTE, 59);
		cal2.set(Calendar.SECOND, 59);
		Calendar now = Calendar.getInstance();
		if (cal2.compareTo(now) >= 0) {
			Settings.setCurrentlyViewingEndDate(context, now.getTime());
			return false;
		} else {
			cal2.set(Calendar.HOUR_OF_DAY, 0);
			cal2.set(Calendar.MINUTE, 0);
			cal2.set(Calendar.SECOND, 0);
			cal2.set(Calendar.MILLISECOND, 0);
			cal2.add(Calendar.SECOND, -1);
			Settings.setCurrentlyViewingEndDate(context, cal2.getTime());
			return true;
		}
    }
    
    public static Date getCurrentlyViewingStartDate(Context context) {
    	return new Date(PreferenceManager.getDefaultSharedPreferences(context).getLong(Setting.BILLING_START_DATE.toString(), getBillingStartDate(context).getTime()));
    }

    public static void setCurrentlyViewingStartDate(Context context, Date value) {
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor ed = prefs.edit();
        ed.putLong(Setting.BILLING_START_DATE.toString(), value.getTime());
        ed.commit();
    }
    
    public static Date getCurrentlyViewingEndDate(Context context) {
    	return new Date(PreferenceManager.getDefaultSharedPreferences(context).getLong(Setting.BILLING_END_DATE.toString(), getBillingEndDate(context).getTime()));
    }

    public static void setCurrentlyViewingEndDate(Context context, Date value) {
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor ed = prefs.edit();
        ed.putLong(Setting.BILLING_END_DATE.toString(), value.getTime());
        ed.commit();
    }
    
    public static String getTermsShown(Context context) {
    	return PreferenceManager.getDefaultSharedPreferences(context).getString(Setting.TERMS_AND_CONDITIONS_SHOWN.toString(), "no");
    }
    
    public static void setTermsShown(Context context, String value) {
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor ed = prefs.edit();
        ed.putString(Setting.TERMS_AND_CONDITIONS_SHOWN.toString(), value);
        ed.commit();
    }
    
    public static String getPlansSorting(Context context) {
    	return PreferenceManager.getDefaultSharedPreferences(context).getString(Setting.PLANS_SORTING.toString(), context.getString(R.string.settings_plansorting_price));
    }
    
	public static Boolean isTestMode(Context context) {
		return "sdk".equals(Build.PRODUCT) || "google_sdk".equals(Build.PRODUCT);
    }
    
    public static double getVATValue() {
    	return 1.18;
    }

    public static Boolean isTrapAfterCall(Context context) {
    	return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(Setting.TRAP_AFTER_CALL.toString(), false);
    }
    
/*    public static Boolean isCrashReportDisabled(Context context) {
    	return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(Setting.CRASH_REPORT_DISABLED.toString(), false);
    }*/
    
    @SuppressWarnings("unchecked")
    public static HashMap<String, Object> getAllConfig(Context context) {
    	return (HashMap<String, Object>) PreferenceManager.getDefaultSharedPreferences(context).getAll();
    }

	public static boolean showPlan(Context context, String operator, String planName) {
		if (isMyOperatorAndPlan(context, operator, planName)) {
			return true;
		}
		String key = "showplan_" + operator + "_" + planName;
		return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(key, true);
    }

	public static void setShowPlan(Context context, String operator, String planName) {
		String key = "showplan_" + operator + "_" + planName;
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor ed = prefs.edit();
        ed.putBoolean(key, true);
        ed.commit();
    }
	
	public static boolean isMyOperatorAndPlan(Context context, String operator, String planName) {
		return operator.equalsIgnoreCase(Settings.getOperator(context)) &&
			   planName.equalsIgnoreCase(Settings.getMyPlan(context));
	}
}
