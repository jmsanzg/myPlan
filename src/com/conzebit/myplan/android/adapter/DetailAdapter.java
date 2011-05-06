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
package com.conzebit.myplan.android.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.conzebit.myplan.R;
import com.conzebit.myplan.android.util.AndroidResourcesUtil;
import com.conzebit.myplan.android.util.Settings;
import com.conzebit.myplan.core.Chargeable;
import com.conzebit.myplan.core.call.Call;
import com.conzebit.myplan.core.message.ChargeableMessage;
import com.conzebit.myplan.core.plan.PlanChargeable;
import com.conzebit.myplan.core.sms.Sms;
import com.conzebit.util.Formatter;

public class DetailAdapter extends ArrayAdapter<PlanChargeable> {

    public DetailAdapter(Context context, int textViewResourceId, List<PlanChargeable> planCalls) { 
	    super(context, textViewResourceId, planCalls);
    }

    public boolean isEnabled(int position) {
    	PlanChargeable pc = super.getItem(position);
    	return pc != null && pc.getChargeable() != null && pc.getChargeable().getChargeableType() == Chargeable.CHARGEABLE_TYPE_CALL;
    }
	
    public View getView(int position, View convertView, ViewGroup parent) { 
        PlanChargeable planCall = super.getItem(position);
        return new PlanCallAdapterView(super.getContext(), planCall);
   }
    
    class PlanCallAdapterView extends LinearLayout {        
        public PlanCallAdapterView(Context context, PlanChargeable planCall) {
            super(context);
            
            View view = super.inflate(context, R.layout.detail_line, this);
            ImageView operatorLogo = (ImageView) view.findViewById(R.id.detail_line_operatorlogo);
            TextView contactName = (TextView) view.findViewById(R.id.detail_line_contactname);
            TextView msisdn = (TextView) view.findViewById(R.id.detail_line_msisdn);
            TextView dateText = (TextView) view.findViewById(R.id.detail_line_dateandduration);

            Chargeable chargeable = planCall.getChargeable();
            if (chargeable != null) {
	            if (chargeable.getChargeableType() == Chargeable.CHARGEABLE_TYPE_CALL) {
	            	Call call = (Call) chargeable;
	            	
	            	int imageID = AndroidResourcesUtil.getMsisdnTypeResourceImage(call.getContact().getMsisdnType());
	            	operatorLogo.setImageResource(imageID);
	            	contactName.setText(call.getContact().getContactName());
	            	msisdn.setText(call.getContact().getMsisdn());
	            	StringBuffer text = new StringBuffer();
	            	text.append(Formatter.formatFullDate(call.getDate()));
	            	text.append(' ');
	            	text.append(Formatter.formatDurationAsString(call.getDuration()));
	            	dateText.setText(text.toString());
	            } else if (chargeable.getChargeableType() == Chargeable.CHARGEABLE_TYPE_SMS) {
	            	Sms sms = (Sms) chargeable;
	            	operatorLogo.setImageResource(R.drawable.logo_sms);
	            	contactName.setText(sms.getContact().getContactName());
	            	msisdn.setText(sms.getContact().getMsisdn());
	            	dateText.setText("(SMS) " + Formatter.formatFullDate(sms.getDate()));
	            } else if (chargeable.getChargeableType() == Chargeable.CHARGEABLE_TYPE_MESSAGE) {
	            	ChargeableMessage message = (ChargeableMessage) chargeable;
	            	int resourceID = getResources().getIdentifier(message.getMessage(), "string", context.getPackageName());	            	
	            	contactName.setText(resourceID);
	            }
            } else {
            	
            }
            
            // Plan total amount
            TextView planAmountTextView = (TextView) view.findViewById(R.id.detail_line_totalamount);
            boolean vat = Settings.isVAT(context);
    		String totalValue = Formatter.formatDecimal(planCall.getPrice() * (vat?Settings.getVATValue():1)) + " " + planCall.getCurrency();
			planAmountTextView.setText(totalValue);
        }
	}
}