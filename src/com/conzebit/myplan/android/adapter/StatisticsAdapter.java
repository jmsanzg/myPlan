package com.conzebit.myplan.android.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.conzebit.myplan.R;
import com.conzebit.myplan.core.call.CallStat;

public class StatisticsAdapter extends ArrayAdapter<CallStat> {

    public StatisticsAdapter(Context context, int textViewResourceId, ArrayList<CallStat> callStats) {
    	super(context, textViewResourceId, callStats);
    }
	
    public View getView(int position, View convertView, ViewGroup parent) { 
        CallStat callStat = super.getItem(position);
        return new StatisticsAdapterView(super.getContext(), callStat);
    }
    
	class StatisticsAdapterView extends LinearLayout {        
        public StatisticsAdapterView(Context context, CallStat callStat) {
            super(context);

            View view = super.inflate(context, R.layout.statistics_line, this);
            
            TextView name = (TextView) view.findViewById(R.id.statistics_name);
            name.setText(callStat.getConcept());
            
            TextView value = (TextView) view.findViewById(R.id.statistics_value);
            value.setText(callStat.getValue());
        }
	}
}