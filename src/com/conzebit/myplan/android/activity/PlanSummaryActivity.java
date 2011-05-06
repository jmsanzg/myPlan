package com.conzebit.myplan.android.activity;

import java.util.ArrayList;
import java.util.Comparator;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.conzebit.myplan.R;
import com.conzebit.myplan.android.MyPlanApp;
import com.conzebit.myplan.android.adapter.SummaryAdapter;
import com.conzebit.myplan.android.helper.PlanSummaryHelper;
import com.conzebit.myplan.android.store.LogStoreService;
import com.conzebit.myplan.android.util.Settings;
import com.conzebit.myplan.core.plan.PlanService;
import com.conzebit.myplan.core.plan.PlanSummary;

public class PlanSummaryActivity extends ListActivity {
	
	private final static int MENU_PREVIOUS_MONTH = 1;
	private final static int MENU_NEXT_MONTH = 2;

	MenuItem followingMonth = null;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		MenuItem item = menu.add(0, MENU_PREVIOUS_MONTH, 0, R.string.menu_previous_period);
		item.setIcon(android.R.drawable.ic_menu_month);

		followingMonth = menu.add(0, MENU_NEXT_MONTH, 0, R.string.menu_following_period);
		followingMonth.setIcon(android.R.drawable.ic_menu_month);
		followingMonth.setEnabled(false); // By default latest period is shown

		return true;
    }

	@Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch(item.getItemId()) {
			case MENU_PREVIOUS_MONTH:
				Settings.previousMonth(this);
				followingMonth.setEnabled(true);
		        loadMyPlanData();
		        break;
			case MENU_NEXT_MONTH:
				followingMonth.setEnabled(Settings.nextMonth(this));
		        loadMyPlanData();
		        break;
		}
		return true;
    }

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        super.setContentView(R.layout.summary);
        this.getListView().setCacheColorHint(0);
        
        ArrayList<PlanSummary> planSummaries = obtainPlanSummaries(); // TODO check this call before loadMyPlanData();
        loadMyPlanData();
        SummaryAdapter summaryAdapter = new SummaryAdapter(this, 0, planSummaries);
        setListAdapter(summaryAdapter);
    }

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(this, PlanDetailActivity.class);
        Bundle bundle = new Bundle();
        PlanSummary planSummary = (PlanSummary) getListAdapter().getItem(position);
        bundle.putString("operator", planSummary.getPlan().getOperator());
        bundle.putString("planname", planSummary.getPlan().getPlanName());
        intent.putExtras(bundle);
        startActivityForResult(intent, 1);
	}

	private ArrayList<PlanSummary> obtainPlanSummaries() {
		MyPlanApp myPlanApp = (MyPlanApp) this.getApplication();		
        PlanService planService = myPlanApp.getPlanService();
      	planService.process(LogStoreService.getInstance().get(this, Settings.getCurrentlyViewingStartDate(this), Settings.getCurrentlyViewingEndDate(this)));
        
        String planSortingValue = Settings.getPlansSorting(this);
        ArrayList<PlanSummary> planSummaries = null;
        if (getString(R.string.settings_plansorting_price).equals(planSortingValue)) {
	        planSummaries = planService.getPlanSummaries(null, new Comparator<PlanSummary>() {
				public int compare(PlanSummary arg0, PlanSummary arg1) {
					double arg0Total = arg0.getTotalPrice(); 
					double arg1Total = arg1.getTotalPrice();
					if (arg0Total < arg1Total) {
						return -1;
					} else if (arg0Total > arg1Total) {
						return 1;
					}
					return 0;
				}
	        });
        } else {
	        planSummaries = planService.getPlanSummaries(null, new Comparator<PlanSummary>() {
				public int compare(PlanSummary arg0, PlanSummary arg1) {
					String operator1 = arg0.getPlan().getOperator();
					String operator2 = arg1.getPlan().getOperator();
					int compareOperator = operator1.compareTo(operator2);
					if (compareOperator == 0) {
						double total0 = arg0.getTotalPrice(); 
						double total1 = arg1.getTotalPrice();
						if (total0 < total1) {
							return -1;
						} else if (total0 > total1) {
							return 1;
						}
						return 0;
					}
					return compareOperator;
				}
	        });
        }
        ArrayList<PlanSummary> ret = new ArrayList<PlanSummary>();
        for (PlanSummary ps : planSummaries) {
        	if (Settings.showPlan(this, ps.getPlan().getOperator(), ps.getPlan().getPlanName())) {
        		ret.add(ps);
        	}
        }
        return ret;
	}
	
    private void loadMyPlanData() {
		SummaryAdapter summaryAdapter = (SummaryAdapter) getListAdapter();
		if (summaryAdapter != null) {
			summaryAdapter.clear();
			ArrayList<PlanSummary> planSummaries = obtainPlanSummaries();
	        for (PlanSummary ps : planSummaries) {
	        	summaryAdapter.add(ps);
	        }
	        summaryAdapter.notifyDataSetChanged();
		}
    	LinearLayout header = (LinearLayout) this.findViewById(R.id.summary_myplan);
    	final Activity activity = this;
    	header.setOnClickListener(new View.OnClickListener() {
						
			public void onClick(View v) {
		        Intent intent = new Intent(activity, PlanDetailActivity.class);
		        Bundle bundle = new Bundle();
		    	String operatorValue = Settings.getOperator(activity);
		    	String planNameValue = Settings.getMyPlan(activity);
		        bundle.putString("operator", operatorValue);
		        bundle.putString("planname", planNameValue);
		        intent.putExtras(bundle);
		        startActivityForResult(intent, 1);
			}
		});
        PlanSummaryHelper.getPlanSummaryView(this, header);
        
        PlanSummaryHelper.getBillingPeriod(this);
    }
	
}