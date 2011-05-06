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
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */
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
