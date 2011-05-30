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
package com.conzebit.myplan.android.activity;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceChangeListener;

import com.conzebit.myplan.R;
import com.conzebit.myplan.android.MyPlanApp;
import com.conzebit.myplan.android.util.Settings;
import com.conzebit.myplan.core.plan.PlanConfig;
import com.conzebit.myplan.core.plan.PlanConfigElement;
import com.conzebit.myplan.core.plan.PlanService;

public class SettingsActivity extends PreferenceActivity {
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent resultIntent = new Intent();
        resultIntent.putExtra("ret", 3);
        setResult(Activity.RESULT_OK, resultIntent);
        
        super.setPreferenceScreen(createPreferenceHierarchy());
    }

    private PreferenceScreen createPreferenceHierarchy() {
    	
        // Root
        PreferenceScreen root = getPreferenceManager().createPreferenceScreen(this);
        
        // Plan Preferences
        PreferenceCategory preferenceCategory = new PreferenceCategory(this);
        preferenceCategory.setTitle(R.string.settings_myplan);
        root.addPreference(preferenceCategory);

        // Billing Day
        ListPreference billingDayPref = new ListPreference(this);
        String [] billingDayList = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};
        billingDayPref.setEntries(billingDayList);
        billingDayPref.setEntryValues(billingDayList);
        billingDayPref.setDialogTitle(R.string.settings_billingday);
        billingDayPref.setKey(Settings.Setting.BILLINGDAY.toString());
        billingDayPref.setTitle(R.string.settings_billingday);
        billingDayPref.setSummary(Settings.getBillingDay(this));
        
        billingDayPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				preference.setSummary(newValue.toString());
				Calendar cal = Calendar.getInstance();
				cal.setTime(Settings.getCurrentlyViewingStartDate(preference.getContext()));
				cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(newValue.toString()));
				Settings.setCurrentlyViewingStartDate(preference.getContext(), cal.getTime());

				cal.add(Calendar.MONTH, 1);
				Calendar now = Calendar.getInstance();
				if (cal.compareTo(now) >= 0) {
					Settings.setCurrentlyViewingEndDate(preference.getContext(), now.getTime());
				} else {
					Settings.setCurrentlyViewingEndDate(preference.getContext(), cal.getTime());
				}
				return true;
			}
        });
        preferenceCategory.addPreference(billingDayPref);

        // Operator
        ListPreference operatorPref = new ListPreference(this);
        final MyPlanApp myPlanApp = (MyPlanApp) this.getApplication();
        String [] operatorList = myPlanApp.getPlanService().getOperatorsAsStringArray();
        operatorPref.setEntries(operatorList);
        operatorPref.setEntryValues(operatorList);
        operatorPref.setDialogTitle(R.string.settings_operator);
        operatorPref.setKey(Settings.Setting.OPERATOR.toString());
        operatorPref.setTitle(R.string.settings_operator);
        if ("".equals(Settings.getOperator(this))) {
        	operatorPref.setSummary(getText(R.string.settings_operator_notselected).toString());
        } else {
        	operatorPref.setSummary(Settings.getOperator(this));
        }
        operatorPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
		  	public boolean onPreferenceChange(Preference preference, Object newValue) {
			  	preference.setSummary(newValue.toString());
			  	ListPreference planPref = (ListPreference) preference.getPreferenceManager().findPreference(Settings.Setting.MYPLAN.toString());
			    planPref.setSummary(getText(R.string.settings_planname_notselected).toString());
			    String []operatorList = myPlanApp.getPlanService().getPlansAsStringArray(newValue.toString());
			    planPref.setEntries(operatorList);
			    planPref.setEntryValues(operatorList);
			    planPref.setEnabled(true);
			    planPref.setValue("");
				return true;
			}
        });
        preferenceCategory.addPreference(operatorPref);
        
        // My Plan
        ListPreference planPref = new ListPreference(this);
        String [] planList = null;
        if ("".equals(Settings.getOperator(this))) {
        	planList = myPlanApp.getPlanService().getPlansAsStringArray();
        } else {
        	planList = myPlanApp.getPlanService().getPlansAsStringArray(Settings.getOperator(this));
        }
        planPref.setEntries(planList);
        planPref.setEntryValues(planList);
        planPref.setDialogTitle(R.string.settings_planname);
        planPref.setKey(Settings.Setting.MYPLAN.toString());
        planPref.setTitle(R.string.settings_planname);
        if ("".equals(Settings.getMyPlan(this))) {
        	planPref.setSummary(getText(R.string.settings_planname_notselected).toString());
        } else {
        	planPref.setSummary(Settings.getMyPlan(this));
        }

        final Activity activity = this;
        planPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
			  	preference.setSummary(newValue.toString());
			  	ListPreference operatorPrefrence = (ListPreference) preference.getPreferenceManager().findPreference(Settings.Setting.OPERATOR.toString());			  	
			  	PreferenceScreen myPlanRoot = (PreferenceScreen) preference.getPreferenceManager().findPreference(Settings.Setting.PLAN_CONFIG.toString());
		        populatePlanPreferences(myPlanApp.getPlanService(), myPlanRoot, operatorPrefrence.getSummary().toString(), newValue.toString());
		        Settings.setShowPlan(activity, operatorPrefrence.getSummary().toString(), newValue.toString());
		        PreferenceScreen showPlansRoot = (PreferenceScreen) preference.getPreferenceManager().findPreference(Settings.Setting.SHOW_PLANS_CONFIG.toString());
		        populateShowPlansPreferences(myPlanApp.getPlanService(), showPlansRoot, operatorPrefrence.getSummary().toString(), newValue.toString());
		        return true;
			}
        });
        planPref.setEnabled(!Settings.getOperator(this).equals(getString(R.string.settings_operator_notselected)));
        preferenceCategory.addPreference(planPref);
        
        // Plan specific configuration
        PreferenceScreen myPlanRoot = getPreferenceManager().createPreferenceScreen(this);
        myPlanRoot.setKey(Settings.Setting.PLAN_CONFIG.toString());
        myPlanRoot.setTitle(R.string.settings_plan_settings);
        populatePlanPreferences(myPlanApp.getPlanService(), myPlanRoot, Settings.getOperator(this), Settings.getMyPlan(this));
        preferenceCategory.addPreference(myPlanRoot);

        // General Preferences
        preferenceCategory = new PreferenceCategory(this);
        preferenceCategory.setTitle(R.string.settings_general);
        root.addPreference(preferenceCategory);

        // Show Plans
        PreferenceScreen showPlansRoot = getPreferenceManager().createPreferenceScreen(this);
        showPlansRoot.setKey(Settings.Setting.SHOW_PLANS_CONFIG.toString());
        showPlansRoot.setTitle(R.string.settings_show_plans);
        showPlansRoot.setSummary(R.string.settings_show_plans_detail);
        populateShowPlansPreferences(myPlanApp.getPlanService(), showPlansRoot, Settings.getOperator(this), Settings.getMyPlan(this));
        preferenceCategory.addPreference(showPlansRoot);
        
        // Plans sorting
        ListPreference plansSortingPref = new ListPreference(this);
        String [] planSortingList = {getString(R.string.settings_plansorting_price), getString(R.string.settings_plansorting_operator_price)};
        plansSortingPref.setEntries(planSortingList);
        plansSortingPref.setEntryValues(planSortingList);
        plansSortingPref.setDialogTitle(R.string.settings_plansorting);
        plansSortingPref.setKey(Settings.Setting.PLANS_SORTING.toString());
        plansSortingPref.setTitle(R.string.settings_plansorting);
        plansSortingPref.setSummary(Settings.getPlansSorting(this));
        
        plansSortingPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				preference.setSummary(newValue.toString());
				return true;
			}
        });
        preferenceCategory.addPreference(plansSortingPref);

        // Count SMS
        CheckBoxPreference countSmsPref = new CheckBoxPreference(this);
        countSmsPref.setChecked(Settings.isCountSms(this));
        countSmsPref.setKey(Settings.Setting.COUNT_SMS.toString());
        countSmsPref.setTitle(R.string.settings_count_sms);
        preferenceCategory.addPreference(countSmsPref);

        // VAT
        CheckBoxPreference vatPref = new CheckBoxPreference(this);
        vatPref.setChecked(Settings.isVAT(this));
        vatPref.setKey(Settings.Setting.VAT.toString());
        vatPref.setTitle(R.string.settings_vat);
        vatPref.setSummary("18%");
        preferenceCategory.addPreference(vatPref);

        // Trap after call
        CheckBoxPreference trapAfterCallPref = new CheckBoxPreference(this);
        trapAfterCallPref.setChecked(Settings.isTrapAfterCall(this));
        trapAfterCallPref.setKey(Settings.Setting.TRAP_AFTER_CALL.toString());
        trapAfterCallPref.setTitle(R.string.settings_trap_after_call);
        trapAfterCallPref.setSummary(R.string.settings_trap_after_call_summary);
        preferenceCategory.addPreference(trapAfterCallPref);
        

        // Disable stacktrace sending
        /*
        CheckBoxPreference crashReportPref = new CheckBoxPreference(this);
        crashReportPref.setChecked(Settings.isCrashReportDisabled(this));
        crashReportPref.setKey(Settings.Setting.CRASH_REPORT_DISABLED.toString());
        crashReportPref.setTitle(R.string.settings_crash_report_disabled);
        crashReportPref.setSummaryOff(R.string.settings_crash_report_disabled_summary);
        preferenceCategory.addPreference(crashReportPref);
        */
        return root;
    }
    
    @SuppressWarnings("unchecked")
    private void populatePlanPreferences(final PlanService planService, PreferenceScreen myPlanRoot, final String operator, final String planName) {
    	myPlanRoot.removeAll();
    	final PlanConfig planConfig = planService.getPlanConfig(operator, planName);
    	myPlanRoot.setEnabled(planConfig != null);
    	if (planConfig != null) {
    		for (PlanConfigElement pce : planConfig.getPlanConfigElements()) {
    			Object valueObject = pce.getValue();
    			if (valueObject instanceof Integer) {
    				Integer value = (Integer) valueObject;
    				Integer minValue = (Integer) pce.getMinValue();
    				Integer maxValue = (Integer) pce.getMaxValue();
    				ListPreference listPreference = new ListPreference(this);
    				String[] elementList = this.createList(minValue, maxValue);
    		        listPreference.setEntries(elementList);
    		        listPreference.setEntryValues(elementList);
    		        int resourceID = getResources().getIdentifier(pce.getId(), "string", getPackageName());
    		        listPreference.setDialogTitle(resourceID);
    		        listPreference.setKey(pce.getId());
    		        listPreference.setTitle(resourceID);
    		        String sValue = getPreferenceManager().getSharedPreferences().getString(pce.getId(), String.valueOf(value));
    		        listPreference.setSummary(sValue);
    		        listPreference.setValue(sValue);
    		        listPreference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
    					public boolean onPreferenceChange(Preference preference, Object newValue) {
    						preference.setSummary(newValue.toString());
    				        PlanConfigElement pce = planConfig.getPlanConfigElement(preference.getKey());
    				        pce.setValue(Integer.valueOf(newValue.toString()));
    						return true;
    					}
    		        });
    		        myPlanRoot.addPreference(listPreference);
    			} else if (valueObject instanceof String) {
    				// TODO ver cuando es String
    			}
    		}
    	}
    }
    
    private String[] createList(int min, int max) {
    	String[] aux = new String[max - min + 1];
    	for (int i = min; i <= max; i++) {
    		aux[i - min] = String.valueOf(i);
    	}
    	return aux;
    }
    
    private void populateShowPlansPreferences(final PlanService planService, PreferenceScreen showPlansRoot, final String myOperator, final String myPlanName) {
    	showPlansRoot.removeAll();
    	for (String operator : planService.getOperatorsAsStringArray()) {
    		PreferenceCategory preferenceCategory = new PreferenceCategory(this);
            preferenceCategory.setTitle(operator);
            showPlansRoot.addPreference(preferenceCategory);
            for (String planName : planService.getPlansAsStringArray(operator)) {
                CheckBoxPreference showPlanPreference = new CheckBoxPreference(this);
                String key = "showplan_" + operator + "_" + planName;
                showPlanPreference.setChecked(Settings.showPlan(this, operator, planName));
                showPlanPreference.setEnabled(!(operator.equalsIgnoreCase(myOperator) && planName.equalsIgnoreCase(myPlanName)));
                showPlanPreference.setKey(key);
                showPlanPreference.setTitle(planName);
                preferenceCategory.addPreference(showPlanPreference);
            }
    	}
    }
    
}
