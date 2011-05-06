package com.conzebit.myplan.android.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.conzebit.myplan.R;
import com.conzebit.myplan.android.store.AndroidMsisdnTypeStore;
import com.conzebit.myplan.android.store.LogStoreService;
import com.conzebit.myplan.android.util.Settings;
import com.conzebit.myplan.core.Chargeable;
import com.conzebit.myplan.core.msisdn.MsisdnTypeService;
import com.conzebit.myplan.core.plan.PlanChargeable;
import com.conzebit.myplan.core.plan.PlanService;
import com.conzebit.myplan.core.plan.PlanSummary;
import com.conzebit.util.Formatter;

public class AfterCallActivity extends Activity {
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	this.setContentView(R.layout.aftercall);
    	
    	AndroidMsisdnTypeStore androidMsisdnTypeStore = new AndroidMsisdnTypeStore(this);
		MsisdnTypeService.getInstance(androidMsisdnTypeStore);
		PlanService planService = PlanService.getInstance();

		ArrayList<Chargeable> data = LogStoreService.getInstance().get(this, Settings.getBillingStartDate(this), Settings.getBillingEndDate(this));
		String operator = Settings.getOperator(this);
		String planName = Settings.getMyPlan(this);

		PlanSummary summary = planService.process(data, operator, planName);
		ArrayList<PlanChargeable> planCalls = summary.getPlanCalls();
		PlanChargeable last = null;
		for (int i = planCalls.size() - 1; i >= 0; i--) {
			last = summary.getPlanCalls().get(i);
			if (last.getChargeable() != null && last.getChargeable().getChargeableType() == Chargeable.CHARGEABLE_TYPE_CALL) {
				break;
			}
		}
		
		TextView callCostTextView = (TextView) this.findViewById(R.id.lastcallcost);
		callCostTextView.setText(Formatter.formatDecimal(last.getPrice()) + " " + last.getCurrency());
		
		TextView totalCostTextView = (TextView) this.findViewById(R.id.total_cost);
		totalCostTextView.setText(Formatter.formatDecimal(summary.getTotalPrice()) + " " + last.getCurrency());
		
		TextView operatorTextView = (TextView) this.findViewById(R.id.operator);
		operatorTextView.setText(operator);

		TextView planNameTextView = (TextView) this.findViewById(R.id.planname);
		planNameTextView.setText(planName);
    }

}
