package com.conzebit.myplan.android.activity;

import java.util.ArrayList;

import android.app.ListActivity;
import android.os.Bundle;

import com.conzebit.myplan.R;
import com.conzebit.myplan.android.adapter.StatisticsAdapter;
import com.conzebit.myplan.android.helper.StatisticsHelper;
import com.conzebit.myplan.core.call.CallStat;

public class StatisticsDetailActivity extends ListActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        super.setContentView(R.layout.statistics);
        this.getListView().setCacheColorHint(0);
        
        Bundle bundle = this.getIntent().getExtras();
        Integer stat = bundle.getInt("stat");
        
        ArrayList<CallStat> callStats = StatisticsHelper.getStatisticsData(this, null, null, stat);
        
        StatisticsAdapter statsAdapter = new StatisticsAdapter(this, 0, callStats);
        setListAdapter(statsAdapter);
    }
}