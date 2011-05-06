package com.conzebit.myplan.android;

import android.app.Application;

import com.conzebit.myplan.android.store.AndroidMsisdnTypeStore;
import com.conzebit.myplan.android.util.Settings;
import com.conzebit.myplan.core.msisdn.MsisdnTypeService;
import com.conzebit.myplan.core.plan.PlanService;

public class MyPlanApp extends Application {
	
	private MsisdnTypeService msisdnTypeService = null;
	private PlanService planService = null;

	@Override
    public void onCreate() {
	    super.onCreate();

	    Settings.setCurrentlyViewingStartDate(this, Settings.getBillingStartDate(this));
    	Settings.setCurrentlyViewingEndDate(this, Settings.getBillingEndDate(this));

		msisdnTypeService = MsisdnTypeService.getInstance(new AndroidMsisdnTypeStore(this));
    	planService = PlanService.getInstance();
    	planService.setUserConfig(Settings.getAllConfig(this));
    }
	
	public PlanService getPlanService() {
		return this.planService;
	}

	public MsisdnTypeService getMsisdnTypeService() {
		return this.msisdnTypeService;
	}
}
