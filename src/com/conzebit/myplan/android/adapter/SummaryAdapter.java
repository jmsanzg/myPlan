package com.conzebit.myplan.android.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.conzebit.myplan.android.helper.PlanSummaryHelper;
import com.conzebit.myplan.core.plan.PlanSummary;

public class SummaryAdapter extends ArrayAdapter<PlanSummary> {

    public SummaryAdapter(Context context, int textViewResourceId, ArrayList<PlanSummary> planSummaries) {
    	super(context, textViewResourceId, planSummaries);
    }
	
    public View getView(int position, View convertView, ViewGroup parent) { 
        PlanSummary planSummary = super.getItem(position);
        return new PlanSummaryAdapterView(super.getContext(), planSummary);
    }
    
	class PlanSummaryAdapterView extends LinearLayout {        
        public PlanSummaryAdapterView(Context context, PlanSummary planSummary) {
            super(context);

            PlanSummaryHelper.getPlanSummaryView(context, this, planSummary);
            
        }
	}
}