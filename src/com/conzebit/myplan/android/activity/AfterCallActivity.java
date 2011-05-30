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