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
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.conzebit.myplan.R;
import com.conzebit.myplan.android.MyPlanApp;
import com.conzebit.myplan.android.adapter.DetailAdapter;
import com.conzebit.myplan.android.helper.PlanSummaryHelper;
import com.conzebit.myplan.core.Chargeable;
import com.conzebit.myplan.core.call.Call;
import com.conzebit.myplan.core.msisdn.MsisdnType;
import com.conzebit.myplan.core.plan.PlanChargeable;
import com.conzebit.myplan.core.plan.PlanService;
import com.conzebit.myplan.core.plan.PlanSummary;

public class PlanDetailActivity extends ListActivity {

	private final static int MENU_VIEW_PLAN_DATA = 1;
	
	private AlertDialog alert = null;
	
	private String operator;
	private String planName;
	private String planURL;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		if (planURL != null) {
			MenuItem menuItem = menu.add(0, MENU_VIEW_PLAN_DATA, 0, R.string.menu_planurl);
			menuItem.setIcon(R.drawable.menu_globe);
		}
		menu.add(1, 2, 1, R.string.settings_plan_settings);

		return true;
    }
	
	@Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch(item.getItemId()) {
			case MENU_VIEW_PLAN_DATA:
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(planURL));
				this.startActivity(i);
				break;
			case  2:
				startActivity(new Intent(this, SettingsActivity.class));
			
		}
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.setContentView(R.layout.detail);
        this.getListView().setCacheColorHint(0);
        
        Bundle bundle = this.getIntent().getExtras();
        operator = bundle.getString("operator");
        planName = bundle.getString("planname");

        MyPlanApp myPlanApp = (MyPlanApp) this.getApplication();
        PlanService planService = myPlanApp.getPlanService();
        ArrayList<PlanChargeable> planCalls = planService.getPlanCalls(operator, planName);
        
        LinearLayout header = (LinearLayout) this.findViewById(R.id.detail_summary);
        PlanSummary planSummary = planService.getPlanSummary(operator, planName);
        this.planURL = planSummary.getPlan().getPlanURL();
        PlanSummaryHelper.getPlanSummaryView(this, header, planSummary);
        PlanSummaryHelper.getBillingPeriod(this);
        
        DetailAdapter detailAdapter = new DetailAdapter(this, 0, planCalls);
        setListAdapter(detailAdapter);
    }
    
	@Override
	protected void onListItemClick(ListView l, View v, final int position, long id) {
		final CharSequence[] items = MsisdnType.getNames();
		final DetailAdapter detailAdapter = (DetailAdapter) getListAdapter();
		final Activity activity = this;
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setItems(items, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
		    	if (detailAdapter.getItem(position).getChargeable() == null) {
		    		return;
		    	}
		    	
		    	// Save the msisdn type
		    	String msisdn = null;
		    	Chargeable chargeable = detailAdapter.getItem(position).getChargeable();
		    	if (chargeable.getChargeableType() == Chargeable.CHARGEABLE_TYPE_CALL) {
		    		Call call = (Call) chargeable;
		    		msisdn = call.getContact().getMsisdn();
		    	}
		        MsisdnType msisdnType = MsisdnType.fromString((String) items[item]);
		        MyPlanApp myPlanApp = (MyPlanApp) activity.getApplication();
		        myPlanApp.getMsisdnTypeService().setMsisdnType(msisdn, msisdnType);
		        
		        myPlanApp.getPlanService().updateMisdnTypeAndProcess(msisdn, msisdnType);
		        ArrayList<PlanChargeable> planCalls = myPlanApp.getPlanService().getPlanCalls(operator, planName);
		        detailAdapter.clear();
		        for (PlanChargeable pc : planCalls) {
		        	detailAdapter.add(pc);
		        }
		        detailAdapter.notifyDataSetChanged();
		        PlanSummaryHelper.getBillingPeriod(activity);
		        
		        Intent resultIntent = new Intent();
		        resultIntent.putExtra("ret", 1);
		        setResult(Activity.RESULT_OK, resultIntent);		        
		    }
		});
		alert = builder.create();
		alert.show();
	}
}