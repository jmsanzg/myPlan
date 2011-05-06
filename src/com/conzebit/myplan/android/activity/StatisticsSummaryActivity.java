package com.conzebit.myplan.android.activity;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.conzebit.myplan.R;
import com.conzebit.myplan.android.adapter.StatisticsAdapter;
import com.conzebit.myplan.android.helper.StatisticsHelper;
import com.conzebit.myplan.core.call.CallStat;

public class StatisticsSummaryActivity extends ListActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        super.setContentView(R.layout.statistics);
        this.getListView().setCacheColorHint(0);
        
        ArrayList<CallStat> callStats = StatisticsHelper.getStatisticsData(this, null, null, -1);
        
        StatisticsAdapter statsAdapter = new StatisticsAdapter(this, 0, callStats);
        setListAdapter(statsAdapter);
    }
    
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(this, StatisticsDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("stat", position);
        intent.putExtras(bundle);
        startActivityForResult(intent, 1);
	}
    
}