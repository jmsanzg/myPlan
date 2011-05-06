package com.conzebit.myplan.android.helper;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.conzebit.myplan.R;
import com.conzebit.myplan.android.util.AndroidResourcesUtil;
import com.conzebit.myplan.android.util.Settings;
import com.conzebit.myplan.core.plan.PlanService;
import com.conzebit.myplan.core.plan.PlanSummary;
import com.conzebit.util.Formatter;

public class PlanSummaryHelper {


	public final static void getPlanSummaryView(Context context, LinearLayout parent) {
    	String operatorValue = Settings.getOperator(context);
    	String planNameValue = Settings.getMyPlan(context);
        PlanSummary planSummary = PlanService.getInstance().getPlanSummary(operatorValue, planNameValue);
        getPlanSummaryView(context, parent, planSummary);
	}
	
	public final static void getPlanSummaryView(Context context, LinearLayout parent, PlanSummary planSummary) {
		
        LinearLayout view = (LinearLayout) LinearLayout.inflate(context, R.layout.summary_line, parent);
        
        ImageView operatorLogo = (ImageView) view.findViewById(R.id.summary_line_operatorlogo);
      	operatorLogo.setImageResource(AndroidResourcesUtil.getOperatorResourceImage(planSummary.getPlan().getOperator()));
        
        TextView planName = (TextView) view.findViewById(R.id.summary_line_planname);
		planName.setText(planSummary.getPlan().getScreenPlanName());
        
        TextView totalAmount = (TextView) view.findViewById(R.id.summary_line_totalamount);
        boolean vat = Settings.isVAT(context);
		String totalValue = Formatter.formatDecimal(planSummary.getTotalPrice() * (vat?Settings.getVATValue():1)) + " " + planSummary.getPlan().getCurrency();
        totalAmount.setText(totalValue);
	}
	
	public final static void getBillingPeriod(Activity activity) {
		
    	TextView billingPeriod = (TextView) activity.findViewById(R.id.billingperiod);
        StringBuffer text = new StringBuffer();
        text.append(activity.getText(R.string.plan_billingperiod));
        text.append(' ');
        text.append(Formatter.formatDate(Settings.getCurrentlyViewingStartDate(activity)));
        text.append(" - ");
        text.append(Formatter.formatDate(Settings.getCurrentlyViewingEndDate(activity)));
        billingPeriod.setText(text);
	}
}
